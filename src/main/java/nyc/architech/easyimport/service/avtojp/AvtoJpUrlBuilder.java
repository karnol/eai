package nyc.architech.easyimport.service.avtojp;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@PropertySource("classpath:config/custom/avto_jp.properties")
public class AvtoJpUrlBuilder {

    @Value("${avto.jp.start.url}")
    private String avtoJpStartUrl;
    @Value("${avto.jp.access.code}")
    private String avtoJpAccessCode;
    @Value("${avto.jp.search.template}")
    private String avtoJpSearchTemplate;
    @Value("${avto.jp.search.count.template}")
    private String avtoJpSearchCountTemplate;
    @Value("${avto.jp.find.by.id}")
    private String avtoJpFindById;
    @Value("${avto.jp.find.by.id.in.stats}")
    private String avtoJpFindByIdInStats;
    @Value("${avto.jp.filter.template}")
    private String avtoJpFilterTemplate;

    @Value("${avto.jp.make.url}")
    private String avtoJpMakeUrl;
    @Value("${avto.jp.model.url}")
    private String avtoJpModelUrl;
    @Value("${avto.jp.auction.url}")
    private String avtoJpAuctionUrl;

    public String urlByFilterName(String filterName) {
        return getFullUrl(String.format(avtoJpFilterTemplate, filterName));
    }

    public String getAvtoJpMakeUrl() {
        return avtoJpMakeUrl;
    }

    public String getAvtoJpModelUrl() {
        return avtoJpModelUrl;
    }

    public String getAvtoJpAuctionUrl() {
        return avtoJpAuctionUrl;
    }

    public String urlToFindById(String id) {
        return getFullUrl(String.format(avtoJpFindById, id));
    }

    public String urlToFindByIdInStats(String id) {
        return getFullUrl(String.format(avtoJpFindByIdInStats, id));
    }

    public String urlToSearchCount(Map<String, Float> koefs, Map<AvtoJpParam, String> params) {
        return getFullUrl(String.format(avtoJpSearchCountTemplate, urlTo(koefs, params, null)));
    }

    public String urlToSearch(Map<String, Float> koefs, Map<AvtoJpParam, String> params, Pageable pageable) {
        return getFullUrl(String.format(avtoJpSearchTemplate, urlTo(koefs, params, pageable)));
    }

    private String urlTo(Map<String, Float> koefs, Map<AvtoJpParam, String> params, Pageable pageable) {
        StringBuilder urlBuilder = new StringBuilder();
        for (Map.Entry<AvtoJpParam, String> entry : params.entrySet()) {
            addCondition(urlBuilder, koefs, entry.getKey(), entry.getValue());
        }
        if (Objects.nonNull(pageable)) {
            addOrderAndLimit(urlBuilder, pageable);
        }

        return urlBuilder.toString();
    }

    private void addCondition(StringBuilder urlBuilder, Map<String, Float> koefs, AvtoJpParam param, String value) {
        if (Objects.nonNull(value)) {
            urlBuilder
                .append(" and ")
                .append(param.getColumnName());

            if (param.isNumeric()) {
                Assert.isTrue(value.split(",").length == 2);
                List<Integer> pair = Arrays.stream(value.split(","))
                                        .map(Integer::parseInt)
                                        .peek(i -> Assert.isTrue(i >= 0))
                                        .collect(Collectors.toList());
                urlBuilder.append(" between ")
                    .append(adjust(koefs, param, pair.get(0))).append(" and ").append(adjust(koefs, param, pair.get(1)));
            } else
                if (param.isLike()) {
                    urlBuilder.append(" like '%").append(value).append("%'");
                } else
                    if (param.isDictionary()) {
                        urlBuilder.append(param.getDictionaryCondition(value));
                    } else {
                        urlBuilder.append(" = '").append(value).append("'");
                    }
        }
    }

    private void addOrderAndLimit(StringBuilder urlBuilder, Pageable pageable) {
        if (Objects.nonNull(pageable.getSort())) {
            List<Sort.Order> orderList = pageable.getSort().get().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderList)) {
                addOrder(urlBuilder, orderList);
            }
        }
        urlBuilder.append(" Limit ").append(pageable.getOffset()).append(" , ").append(pageable.getPageSize());
    }

    private StringBuilder addOrder(StringBuilder urlBuilder, List<Sort.Order> orderList) {
        urlBuilder.append(" Order by ");
        orderList
            .forEach(order -> urlBuilder.append(order.getProperty()).append(order.isDescending() ? " desc" : "").append(", "));
        return urlBuilder.delete(urlBuilder.length() - 2, urlBuilder.length());
    }

    private Integer adjust(Map<String, Float> koefs, AvtoJpParam param, Integer value) {
        return StringUtils.isNoneBlank(param.getSettingsKoefName())
                    && Objects.nonNull(koefs.get(param.getSettingsKoefName())) ?
                                Math.round(value * koefs.get(param.getSettingsKoefName()))
                                                            :
                                (Objects.isNull(param.getKoef()) ? value : Math.round(value * param.getKoef()));
    }

    private String getFullUrl(String url) {
        return new StringBuilder(avtoJpStartUrl).append(avtoJpAccessCode).append(url).toString();
    }
}
