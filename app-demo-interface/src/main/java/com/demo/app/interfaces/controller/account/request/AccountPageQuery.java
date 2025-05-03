package com.demo.app.interfaces.controller.account.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;


/**
 * The type Account page query.
 */
@Data
public class AccountPageQuery {
    @Min(value = 1)
    private Long page = 1L;
    @Min(value = 1)
    @Max(value = 100)
    private Long size = 10L;
    private Boolean withCards = true;
}
