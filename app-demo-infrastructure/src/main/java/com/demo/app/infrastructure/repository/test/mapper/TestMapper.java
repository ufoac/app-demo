package com.demo.app.infrastructure.repository.test.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;


/**
 * The interface Test mapper.
 */
@Mapper
public interface TestMapper {
    /**
     * Now local date time.
     *
     * @return the local date time
     */
    LocalDateTime now();

    /**
     * Timezone string.
     *
     * @return the string
     */
    String timezone();
}
