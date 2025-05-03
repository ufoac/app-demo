package com.demo.app.service.common.entity.request;

import lombok.Builder;
import lombok.Data;


/**
 * The type Create card command.
 */
@Data
@Builder
public class CreateCardCommand {
    private String info;
}
