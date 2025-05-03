package com.demo.app.interfaces.controller.account;

import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.interfaces.common.convertor.WebEntityConvertor;
import com.demo.app.interfaces.common.entity.CommonResult;
import com.demo.app.interfaces.common.entity.CommonResultCode;
import com.demo.app.interfaces.controller.account.request.AccountPageQuery;
import com.demo.app.interfaces.controller.account.request.CardPageQuery;
import com.demo.app.interfaces.controller.account.request.ChangeAccountStatusRequest;
import com.demo.app.interfaces.controller.account.request.CreateAccountRequest;
import com.demo.app.interfaces.controller.account.response.AccountStatusResult;
import com.demo.app.interfaces.controller.account.response.AccountVO;
import com.demo.app.interfaces.controller.card.response.CardVO;
import com.demo.app.service.common.entity.request.CreateAccountCommand;
import com.demo.app.service.common.entity.request.UpdateAccountCommand;
import com.demo.app.service.service.AccountCardBizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * Account Controller handles account-related operations
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {
    private final AccountCardBizService accountCardBizService;
    private final WebEntityConvertor webEntityConvertor;

    /**
     * Get account details by ID
     *
     * @param id Account ID
     * @return Account details
     */
    @GetMapping("/{id}")
    CommonResult<AccountVO> account(@PathVariable("id") @Positive Long id) {
        return accountCardBizService.getAccountById(id)
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.success(null));
    }

    /**
     * Create new account
     *
     * @param request Account creation data
     * @return Created account details
     */
    @PostMapping
    CommonResult<AccountVO> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return Optional.ofNullable(accountCardBizService.createAccount(
                        CreateAccountCommand.builder()
                                .email(request.getEmail())
                                .info(request.getInfo())
                                .build()))
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.failed(CommonResultCode.ERROR));
    }

    /**
     * Update account status
     *
     * @param id      the id
     * @param request Status change request
     * @return Updated account details
     */
    @PatchMapping("/{id}/status")
    CommonResult<AccountStatusResult> updateAccountStatus(@PathVariable("id") @Positive Long id,
                                                          @Valid @RequestBody ChangeAccountStatusRequest request) {
        var status = accountCardBizService.updateAccountStatus(
                UpdateAccountCommand.builder()
                        .id(id)
                        .status(request.getStatus())
                        .build());
        return CommonResult.success(AccountStatusResult.builder()
                .id(id)
                .status(status)
                .build());
    }

    /**
     * Paginated account search with card association
     *
     * @param query Search criteria and pagination
     * @return Paginated account results
     */
    @GetMapping
    public CommonResult<CommonPage<AccountVO>> pageAccountWithCards(@Valid @ModelAttribute AccountPageQuery query) {
        var pageData = accountCardBizService.getAccountPage(new CommonPageInfo(query.getPage(), query.getSize()),
                query.getWithCards());
        return CommonResult.success(pageData.convert(webEntityConvertor::toVO));
    }

    /**
     * Get paginated cards for specific account
     *
     * @param id    the id
     * @param query Pagination parameters
     * @return Paginated card results
     */
    @GetMapping("/{id}/cards")
    public CommonResult<CommonPage<CardVO>> pageAccountCards(@PathVariable("id") @Positive Long id,
                                                             @Valid @ModelAttribute CardPageQuery query) {
        var pageData = accountCardBizService.getCardByAccountId(id,
                new CommonPageInfo(query.getPage(), query.getSize()));
        return CommonResult.success(pageData.convert(webEntityConvertor::toVO));
    }

}