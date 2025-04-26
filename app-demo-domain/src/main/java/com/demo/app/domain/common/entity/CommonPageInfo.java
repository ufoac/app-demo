package com.demo.app.domain.common.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Common page info.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonPageInfo {
    private Long pageNum;
    private Long pageSize;
}
