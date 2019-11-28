package nyc.architech.easyimport.service.avtojp.impl;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.architech.easyimport.domain.Settings;
import nyc.architech.easyimport.service.AuctionService;
import nyc.architech.easyimport.service.SettingsService;
import nyc.architech.easyimport.service.DictionaryService;
import nyc.architech.easyimport.service.avtojp.AutoJpService;
import nyc.architech.easyimport.service.dto.auction.AvtoJpLotDTO;
import nyc.architech.easyimport.service.dto.auction.AuctionSearchDTO;
import nyc.architech.easyimport.service.dto.auction.DictionaryDTO;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import nyc.architech.easyimport.service.avtojp.AvtoJpParam;
import nyc.architech.easyimport.service.avtojp.AvtoJpUrlBuilder;
import nyc.architech.easyimport.service.mapper.AvtoJpMapper;
import nyc.architech.easyimport.service.util.OptionalUtil;
import nyc.architech.easyimport.service.util.StreamUtil;
import org.apache.commons.compress.utils.Lists;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AvtoJpAuctionService implements AutoJpService {

    private final RestTemplate restTemplate;
    private final AvtoJpUrlBuilder urlBuilder;
    private final SettingsService settingsService;
    private final AvtoJpMapper avtoJpMapper;

    @Override
    public List<LotDTO> find(Integer count) {
        return search(new AuctionSearchDTO(), PageRequest.of(0, count)).getContent();
    }

    @Override
    public Page<LotDTO> search(AuctionSearchDTO paramsDTO, Pageable pageable) {
        Map<AvtoJpParam, String> params = getParamsFromDTO(paramsDTO);

        Map<String, Float> koefs = getSettingsKoefs();
        String urlTotalRows = urlBuilder.urlToSearchCount(koefs, params);

        //at first get totalRowsCount
        List<Map.Entry<String, String>> responseCountMap = restTemplate
                            .exchange(urlTotalRows, HttpMethod.GET, null,
                                      new ParameterizedTypeReference<List<Map.Entry<String, String>>>() {}).getBody();
        int totalRowsCount = StreamUtil.emptyIfNull(responseCountMap)
                                .findFirst()
                                .map(e -> Integer.parseInt(e.getValue()))
                                .orElse(0);

        List<AvtoJpLotDTO> auctionLotDTOList = (pageable.getOffset() >= totalRowsCount) ? Lists.newArrayList()
                                                        :
                            restTemplate
                                .exchange(urlBuilder.urlToSearch(koefs, params, pageable), HttpMethod.GET, null,
                                    new ParameterizedTypeReference<List<AvtoJpLotDTO>>() {}).getBody();

        List<LotDTO> lotDTOList = avtoJpMapper.avtoJpLotDTOListToLotDTOList(auctionLotDTOList);
        //calculate USD-prices
        lotDTOList
            .forEach(dto -> recalculatePrices(dto, koefs.get(Settings.USD_TO_JPY_RATE)));
        return new PageImpl<>(lotDTOList, pageable, totalRowsCount);
    }

    @Override
    public Optional<LotDTO> findById(String externalId) {
        return OptionalUtil
            .orGet(getLotByUrl(urlBuilder.urlToFindById(externalId)),
                   () -> this.getLotByUrl(urlBuilder.urlToFindByIdInStats(externalId)));

    }

    private Optional<LotDTO> getLotByUrl(String url) {
        Optional<AvtoJpLotDTO> dtoOptional =
            StreamUtil.emptyIfNull(restTemplate.exchange(url, HttpMethod.GET, null,
                                                   new ParameterizedTypeReference<List<AvtoJpLotDTO>>() {}).getBody())
                .findFirst();
        //TODO: move recalculatePrices to AvtoJpMapper!!
//        return dtoOptional.map(avtoJpMapper::avtoJpLotDTOToLotDTO);
        Optional<LotDTO> lotDtoOptional = dtoOptional.map(avtoJpMapper::avtoJpLotDTOToLotDTO);
        lotDtoOptional.ifPresent(dto -> recalculatePrices(dto, getSettingsKoefs().get(Settings.USD_TO_JPY_RATE)));
        return lotDtoOptional;
    }

    private Map<String, Float> getSettingsKoefs() {
        return ImmutableMap.of(Settings.USD_TO_JPY_RATE, Float.parseFloat(settingsService.getSettingsByName(Settings.USD_TO_JPY_RATE).getValue()));
    }

    private Map<AvtoJpParam, String> getParamsFromDTO(AuctionSearchDTO searchDTO) {
        Map<AvtoJpParam, String> params = new HashMap<>();
        Stream.of(AvtoJpParam.values())
            .filter(param -> Objects.nonNull(param.getValueGetter().apply(searchDTO)))
            .forEach(param -> params.put(param, param.getValueGetter().apply(searchDTO)));
        return params;
    }

    private void recalculatePrices(LotDTO lotDTO, Float rate) {
        BigDecimal bdRate = BigDecimal.valueOf(rate);
        lotDTO.setPrice(Objects.isNull(lotDTO.getPriceOriginal()) ?
            BigDecimal.ZERO : lotDTO.getPriceOriginal().divide(bdRate, 2, 1));
        lotDTO.setFinalPrice(Objects.isNull(lotDTO.getFinalPriceOriginal()) ?
            BigDecimal.ZERO : lotDTO.getFinalPriceOriginal().divide(bdRate, 2, 1));
        lotDTO.setAveragePrice(Objects.isNull(lotDTO.getAveragePriceOriginal()) ?
            BigDecimal.ZERO : lotDTO.getAveragePriceOriginal().divide(bdRate, 2, 1));
        lotDTO.setPriceHistory(
            lotDTO.getPriceOriginalHistory()
                .stream()
                .map(bd -> bd.divide(bdRate, 2, 1))
                .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<DictionaryDTO> findAllDictionaries() {
        return Lists.newArrayList();
    }
}
