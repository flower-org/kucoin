/**
 * Copyright 2019 Mek Global Limited.
 */
package com.kucoin.model.retrofit;

import com.kucoin.model.request.StopOrderCreateRequest;
import com.kucoin.model.response.KucoinResponse;
import com.kucoin.model.response.OrderCancelResponse;
import com.kucoin.model.response.OrderCreateResponse;
import com.kucoin.model.response.Pagination;
import com.kucoin.model.response.StopOrderResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface StopOrderAPIRetrofit {

    @POST("api/v1/stop-order")
    Call<KucoinResponse<OrderCreateResponse>> createStopOrder(@Body StopOrderCreateRequest request);

    @DELETE("api/v1/stop-order/{orderId}")
    Call<KucoinResponse<OrderCancelResponse>> cancelStopOrder(@Path("orderId") String orderId);

    @DELETE("api/v1/stop-order/cancel")
    Call<KucoinResponse<OrderCancelResponse>> cancelStopOrders(@QueryMap Map<String, String> params);

    @GET("api/v1/stop-order/{orderId}")
    Call<KucoinResponse<StopOrderResponse>> getStopOrder(@Path("orderId") String orderId);

    @GET("api/v1/stop-order")
    Call<KucoinResponse<Pagination<StopOrderResponse>>> queryStopOrders(@QueryMap Map<String, String> params);

    @DELETE("api/v1/stop-order/cancelOrderByClientOid")
    Call<KucoinResponse<OrderCancelResponse>> cancelStopOrderByClientOid(String clientOid);

    @GET("api/v1/stop-order/queryOrderByClientOid")
    Call<KucoinResponse<StopOrderResponse>> getStopOrderByClientOid(String clientOid);
}
