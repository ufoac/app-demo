package com.demo.app.interfaces.controller.card;

import com.demo.app.interfaces.common.convertor.WebEntityConvertor;
import com.demo.app.interfaces.common.entity.CommonResult;
import com.demo.app.interfaces.common.entity.CommonResultCode;

import com.demo.app.interfaces.controller.card.request.AssignCardRequest;
import com.demo.app.interfaces.controller.card.request.ChangeCardStatusRequest;
import com.demo.app.interfaces.controller.card.request.CreateCardRequest;
import com.demo.app.interfaces.controller.card.response.CardVO;
import com.demo.app.service.dto.request.*;
import com.demo.app.service.service.AccountCardBizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * Card Controller handles card-related operations
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
public class CardController {
    private final AccountCardBizService accountCardBizService;
    private final WebEntityConvertor webEntityConvertor;

    /**
     * Create new card
     *
     * @param request Card creation data
     * @return Created card details
     */
    @PostMapping("/card/create")
    public CommonResult<CardVO> createCard(@Valid @RequestBody CreateCardRequest request) {
        return Optional.ofNullable(accountCardBizService.createCard(
                        CreateCardCommand.builder()
                                .info(request.getInfo())
                                .build()))
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.failed(CommonResultCode.ERROR));
    }

    /**
     * Update card status
     *
     * @param request Status change request
     * @return Updated card details
     */
    @PostMapping("/card/status")
    public CommonResult<CardVO> updateCardStatus(@Valid @RequestBody ChangeCardStatusRequest request) {
        return Optional.ofNullable(accountCardBizService.updateCardStatus(
                        UpdateCardCommand.builder()
                                .id(request.getId())
                                .status(request.getStatus())
                                .build()))
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.failed(CommonResultCode.ERROR));
    }


    /**
     * Assign card to account
     *
     * @param request Assignment details
     * @return Updated card details
     */
    @PostMapping("/card/assign")
    public CommonResult<CardVO> assignCard(@Valid @RequestBody AssignCardRequest request) {
        return Optional.ofNullable(accountCardBizService.assignCard(
                        AssignCardCommand.builder()
                                .accountId(request.getAccountId())
                                .cardId(request.getCardId())
                                .build()))
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.failed(CommonResultCode.ERROR));
    }

    /**
     * Get card details by ID
     *
     * @param id Card ID
     * @return Card details
     */
    @GetMapping("/detail/{id}")
    public CommonResult<CardVO> card(@PathVariable("id") @Positive Long id) {
        return accountCardBizService.getCardById(id)
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.success(null));
    }

}
