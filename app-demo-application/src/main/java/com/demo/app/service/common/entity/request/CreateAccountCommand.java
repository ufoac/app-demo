package com.demo.app.service.common.entity.request;

import lombok.Builder;
import lombok.Data;


/**
 * The type Create account command.
 */
@Data
@Builder
public class CreateAccountCommand {
    private String email;
    private String info;
}
