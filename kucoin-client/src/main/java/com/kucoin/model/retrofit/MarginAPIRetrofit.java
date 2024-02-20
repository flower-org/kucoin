/**
 * Copyright 2019 Mek Global Limited.
 */
package com.kucoin.model.retrofit;

import com.kucoin.model.request.MarginOrderCreateRequest;
import com.kucoin.model.response.IsolatedMarginAccountInfo;
import com.kucoin.model.response.IsolatedMarginBorrowRequest;
import com.kucoin.model.response.IsolatedMarginPriceStrategy;
import com.kucoin.model.response.IsolatedMarginQuickRepayRequest;
import com.kucoin.model.response.IsolatedMarginSingleRepayRequest;
import com.kucoin.model.response.KucoinResponse;
import com.kucoin.model.response.MarginAccountResponse;
import com.kucoin.model.response.MarginConfigResponse;
import com.kucoin.model.response.MarginOrderCreateResponse;
import com.kucoin.model.response.MarginPriceStrategy;
import com.kucoin.model.response.MarkPriceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by ezreal on 2020/12/08.
 */
public interface MarginAPIRetrofit {

    @GET("api/v1/mark-price/{symbol}/current")
    Call<KucoinResponse<MarkPriceResponse>> getMarkPrice(@Path("symbol") String symbol);

    @GET("api/v1/margin/config")
    Call<KucoinResponse<MarginConfigResponse>> getMarginConfig();

    @GET("api/v1/margin/account")
    Call<KucoinResponse<MarginAccountResponse>> getMarginAccount();

    @POST("api/v1/margin/order")
    Call<KucoinResponse<MarginOrderCreateResponse>> createMarginOrder(@Body MarginOrderCreateRequest request);

    /**
     * This endpoint can query the cross/isolated margin risk limit.
     * @param marginModel The type of marginModel : cross（cross margin）, isolated (isolated margin). Default is cross.
     */
    @GET("api/v1/risk/limit/strategy")
    Call<KucoinResponse<List<MarginPriceStrategy>>> getMarginPriceStrategy(@Query("marginModel") String marginModel);

    @GET("api/v1/isolated/symbols")
    Call<KucoinResponse<List<IsolatedMarginPriceStrategy>>> getIsolatedMarginPriceStrategies();

    @GET("api/v1/isolated/accounts")
    Call<KucoinResponse<IsolatedMarginAccountInfo>> getIsolatedMarginAccounts(@Query("balanceCurrency") @Nullable String balanceCurrency);

    @GET("api/v1/isolated/account/{symbol}")
    Call<KucoinResponse<IsolatedMarginAccountInfo.Asset>> getIsolatedMarginAccount(@Path("symbol") String symbol);

    @POST("api/v1/isolated/borrow")
    Call<KucoinResponse<Object>> isolatedMarginBorrow(@Body IsolatedMarginBorrowRequest request);

    @POST("api/v1/isolated/repay/single")
    Call<KucoinResponse<Void>> isolatedMarginSingleRepay(@Body IsolatedMarginSingleRepayRequest request);

    @POST("api/v1/isolated/repay/all")
    Call<KucoinResponse<Void>> isolatedMarginQuickRepay(@Body IsolatedMarginQuickRepayRequest request);
}
