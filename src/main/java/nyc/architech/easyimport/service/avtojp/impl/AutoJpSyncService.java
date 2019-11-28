package nyc.architech.easyimport.service.avtojp.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.architech.easyimport.domain.Filter;
import nyc.architech.easyimport.domain.enums.FilterType;
import nyc.architech.easyimport.domain.enums.Platform;
import nyc.architech.easyimport.repository.FilterRepository;
import nyc.architech.easyimport.service.AuctionSyncService;
import nyc.architech.easyimport.service.WatchLotService;
import nyc.architech.easyimport.service.avtojp.AutoJpService;
import nyc.architech.easyimport.service.avtojp.AvtoJpUrlBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoJpSyncService implements AuctionSyncService {

    public final AutoJpService avtoJpService;
    private final FilterRepository filterRepository;
    //TODO: move it to configs
    private final RestTemplate restTemplateForPlainText = new RestTemplate();
    private final WatchLotService watchLotService;
    private final AvtoJpUrlBuilder urlBuilder;

    @Override
    public void syncFilters() {
        syncMakers();
        syncModels();
        syncAuctions();
    }

    @Override
    public void syncWatchList() {
        watchLotService.syncWithSource();
    }

    private static class AvtoJpCommonDto {
        String externalId;
        String name;
        String parentExternalId;

        static AvtoJpCommonDto fromString(String str) {
            AvtoJpCommonDto dto = new AvtoJpCommonDto();
            String[] fields = str.split(":");
            dto.externalId = fields[0];
            dto.name = fields[1];
            return dto;
        }

        static AvtoJpCommonDto fromStringWithParent(String str) {
            AvtoJpCommonDto dto = new AvtoJpCommonDto();
            String[] fields = str.split(":");
            dto.externalId = fields[1];
            dto.name = reduceCountFromName(fields[2]);
            dto.parentExternalId = fields[0];
            return dto;
        }
    }

    private void syncMakers() {
        syncCommon(urlBuilder.getAvtoJpMakeUrl(), FilterType.MAKE, AvtoJpCommonDto::fromString);
    }

    private void syncModels() {
        syncCommon(urlBuilder.getAvtoJpModelUrl(), FilterType.MODEL, AvtoJpCommonDto::fromStringWithParent);
    }

    private void syncCommon(String urlToSync, FilterType filterType, Function<String, AvtoJpCommonDto> staticFabricMethod) {

        String dtoResponse = "";
        try {
            dtoResponse = restTemplateForPlainText.getForObject(urlToSync, String.class);
        } catch (RestClientException ex) {
            log.debug("Troubles reaching avto.jp for url - {}", urlToSync, ex);
        }
        if (StringUtils.isEmpty(dtoResponse)) {
            return;
        }

        Map<String, Filter> mapFV =
            filterRepository
                .findByPlatformEqualsAndTypeEqualsOrderByNameAsc(Platform.AVTO_JP, filterType)
                .stream()
                .collect(Collectors.toMap(Filter::getExternalId, Function.identity()));

        List<Filter> newFilters =
            Stream.of(dtoResponse.split(";"))
                .filter(StringUtils::isNotBlank)
                .map(staticFabricMethod)
                .filter(dto -> Objects.isNull(mapFV.get(dto.externalId)))
                .map(dto -> Filter.builder().externalId(dto.externalId).name(dto.name).type(filterType)
                                .parentExternalId(dto.parentExternalId).platform(Platform.AVTO_JP).build())
                .collect(Collectors.toList());

        filterRepository.saveAll(newFilters);
    }

    private void syncAuctions() {
        String dtoResponse = "";
        try {
            dtoResponse = restTemplateForPlainText.getForObject(urlBuilder.getAvtoJpAuctionUrl(), String.class);
        } catch (RestClientException ex) {
            log.debug("Troubles reaching avto.jp for url - {}", urlBuilder.getAvtoJpAuctionUrl(), ex);
        }
        if (StringUtils.isEmpty(dtoResponse)) {
            return;
        }

        Map<String, Filter> mapFV =
            filterRepository
                .findByPlatformEqualsAndTypeEqualsOrderByNameAsc(Platform.AVTO_JP, FilterType.AUCTION)
                .stream()
                .collect(Collectors.toMap(Filter::getExternalId, Function.identity()));

        List<Filter> newFilters =
            Stream.of(dtoResponse.split("\n"))
                .filter(StringUtils::isNotBlank)
                .flatMap(record -> {
                    List<String> pair = Arrays.asList(record.split(":"));
                    return Stream.of(pair.get(1).split(";"))
                        .filter(StringUtils::isNotBlank)
                        .map(str -> new AbstractMap.SimpleEntry<String, String>(reduceCountFromName(str), pair.get(0)));
                })
                .collect(Collectors.groupingBy(Map.Entry::getKey,  Collectors.mapping(Map.Entry::getValue, Collectors.joining(","))))
                .entrySet()
                .stream()
                .filter(entry -> Objects.isNull(mapFV.get(entry.getKey())))
                .map(entry -> Filter.builder().name(entry.getKey()).externalId(entry.getKey()).additionalInfo(entry.getValue())
                                        .type(FilterType.AUCTION).platform(Platform.AVTO_JP).build())
                .collect(Collectors.toList());

        filterRepository.saveAll(newFilters);
    }

    private static String reduceCountFromName(String value) {
        return value.indexOf(" (") < 0 ? value : value.substring(0, value.indexOf(" ("));
    }

}
