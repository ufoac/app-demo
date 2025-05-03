package com.demo.app.interfaces.controller.account.response;

import com.demo.app.domain.model.account.AccountStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


/**
 * The type Account dto.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountStatusResult {
    private Long id;
    private AccountStatus status;
}
