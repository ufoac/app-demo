package com.demo.app.service.common.entity.request;

import lombok.Builder;
import lombok.Data;


/**
 * The type Assign card command.
 */
@Data
@Builder
public class AssignCardCommand {
    private Long accountId;
    private Long cardId;
}
