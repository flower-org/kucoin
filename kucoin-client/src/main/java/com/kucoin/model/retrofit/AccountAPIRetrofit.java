/**
 * Copyright 2019 Mek Global Limited.
 */
package com.kucoin.model.retrofit;

import com.kucoin.model.request.AccountCreateRequest;
import com.kucoin.model.request.AccountTransferV2Request;
import com.kucoin.model.request.SubMasterTransferV2Request;
import com.kucoin.model.response.AccountBalanceResponse;
import com.kucoin.model.response.AccountBalancesResponse;
import com.kucoin.model.response.AccountDetailResponse;
import com.kucoin.model.response.KucoinResponse;
import com.kucoin.model.response.Pagination;
import com.kucoin.model.response.SubAccountBalanceResponse;
import com.kucoin.model.response.SubAccountUserInfoV1;
import com.kucoin.model.response.TransferableBalanceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by chenshiwei on 2019/1/10.
 */
public interface AccountAPIRetrofit {
    /**
     * Get a list of accounts.
     * Please deposit funds to the main account firstly, then transfer the funds to the trade account via Inner Transfer before transaction.
     *
     * @param currency [Optional] Currency
     * @param type [Optional] Account type: main, trade, margin
     * @return Account balances
     */
    @GET("api/v1/accounts")
    Call<KucoinResponse<List<AccountBalancesResponse>>> getAccountList(
            @Query("currency") @Nullable String currency, @Query("type") @Nullable String type);

    @GET("api/v1/accounts/{accountId}")
    Call<KucoinResponse<AccountBalanceResponse>> getAccount(@Path("accountId") String accountId);

    @POST("api/v1/accounts")
    Call<KucoinResponse<Map<String, String>>> addAccount(@Body AccountCreateRequest request);

    @GET("api/v1/accounts/ledgers")
    Call<KucoinResponse<Pagination<AccountDetailResponse>>> getAccountLedgers(
            @Query("currency") String currency,
            @Query("direction") String direction,
            @Query("bizType") String bizType,
            @Query("currentPage") int currentPage,
            @Query("pageSize") int pageSize,
            @Query("startAt") long startAt,
            @Query("endAt") long endAt);

    /** Inner Transfer */
    @POST("api/v2/accounts/inner-transfer")
    Call<KucoinResponse<Map<String, String>>> applyTransfer2(@Body AccountTransferV2Request request);

    @GET("api/v1/sub-accounts")
    Call<KucoinResponse<List<SubAccountBalanceResponse>>> getSubAccountList();

    @GET("api/v1/sub-accounts/{subUserId}")
    Call<KucoinResponse<SubAccountBalanceResponse>> getSubAccount(@Path("subUserId") String subUserId);

    @POST("api/v2/accounts/sub-transfer")
    Call<KucoinResponse<Map<String, String>>> transferBetweenSubAndMasterV2(@Body SubMasterTransferV2Request request);

    @GET("api/v1/accounts/transferable")
    Call<KucoinResponse<TransferableBalanceResponse>> transferable(@Query("currency") String currency, @Query("type") String type);

    @GET("api/v1/sub/user")
    Call<KucoinResponse<List<SubAccountUserInfoV1>>> getUserInfoForAllSubAccounts();
}
