package com.demo.app.infrastructure.common.convertor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;

import java.util.function.Function;

/**
 * PageConvertor
 *
 * @author cao
 * @since 2025 /4/24
 */
public class PageConvertor {

    /**
     * To common page
     *
     * @param <T>         the type parameter
     * @param <R>         the type parameter
     * @param mybatisPage the mybatis page
     * @param converter   the converter
     * @return the common page
     */
    public static <T, R> CommonPage<R> toCommonPage(Page<T> mybatisPage, Function<T, R> converter) {
        var pageInfo = new CommonPageInfo(mybatisPage.getCurrent(), mybatisPage.getSize());
        return CommonPage.<R>builder()
                .info(pageInfo)
                .totalPage(mybatisPage.getPages())
                .total(mybatisPage.getTotal())
                .list(mybatisPage.getRecords()
                        .stream()
                        .map(converter)
                        .toList())
                .build();
    }
}
