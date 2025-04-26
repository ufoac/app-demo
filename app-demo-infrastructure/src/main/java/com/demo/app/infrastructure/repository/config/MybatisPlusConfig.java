package com.demo.app.infrastructure.repository.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * The type Mybatis plus config.
 */
@Configuration
@MapperScan({"com.demo.app.infrastructure.repository.**.mapper.**"})
public class MybatisPlusConfig {

    /**
     * Mybatis plus interceptor mybatis plus interceptor.
     *
     * @return the mybatis plus interceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        var interceptor = new MybatisPlusInterceptor();
        // For Optimistic Lock
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // For Pagination
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }
}
