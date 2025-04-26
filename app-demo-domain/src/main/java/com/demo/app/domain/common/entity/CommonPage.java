package com.demo.app.domain.common.entity;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * The type Common page.
 *
 * @param <T> the type parameter
 */
@Data
@Builder
public class CommonPage<T> {
    private CommonPageInfo info;
    private Long total;
    private Long totalPage;
    private List<T> list;

    /**
     * Convert common page.
     *
     * @param <R>       the type parameter
     * @param converter the converter
     * @return the common page
     */
    public <R> CommonPage<R> convert(Function<T, R> converter) {
        var convertedData = this.list.stream()
                .map(converter)
                .collect(Collectors.toList());
        return CommonPage.<R>builder()
                .list(convertedData)
                .total(total)
                .totalPage(totalPage)
                .info(info)
                .build();

    }
}
