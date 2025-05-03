package com.demo.app.service.common.entity.request;

import com.demo.app.domain.model.account.AccountStatus;
import lombok.Builder;
import lombok.Data;


/**
 * The type Update account command.
 */
@Data
@Builder
public class UpdateAccountCommand {
    private Long id;
    private AccountStatus status;
}
