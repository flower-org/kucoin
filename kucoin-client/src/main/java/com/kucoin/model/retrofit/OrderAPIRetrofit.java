/**
 * Copyright 2019 Mek Global Limited.
 */
package com.kucoin.model.retrofit;


import com.kucoin.model.request.MultiOrderCreateRequest;
import com.kucoin.model.request.OrderCreateApiRequest;
import com.kucoin.model.response.ActiveOrderResponse;
import com.kucoin.model.response.KucoinResponse;
import com.kucoin.model.response.MultiOrderCreateResponse;
import com.kucoin.model.response.OrderCancelResponse;
import com.kucoin.model.response.OrderCreateResponse;
import com.kucoin.model.response.OrderResponse;
import com.kucoin.model.response.Pagination;
import com.kucoin.model.response.UserFeeResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by chenshiwei on 2019/1/10.
 */
public interface OrderAPIRetrofit {

    @POST("api/v1/orders")
    Call<KucoinResponse<OrderCreateResponse>> createOrder(@Body OrderCreateApiRequest opsRequest);

    @POST("api/v1/orders/multi")
    Call<KucoinResponse<MultiOrderCreateResponse>> createMultipleOrders(@Body MultiOrderCreateRequest multiOrderCreateRequest);

    @DELETE("api/v1/orders/{orderId}")
    Call<KucoinResponse<OrderCancelResponse>> cancelOrder(@Path("orderId") String orderId);

    @DELETE("api/v1/order/client-order/{clientOid}")
    Call<KucoinResponse<OrderCancelResponse>> cancelOrderByClientOid(@Path("clientOid") String clientOid);

    @DELETE("api/v1/orders")
    Call<KucoinResponse<OrderCancelResponse>> cancelOrders(@Query("symbol") String symbol,
                                                           @Query("tradeType") String tradeType);


    @GET("api/v1/orders/{orderId}")
    Call<KucoinResponse<OrderResponse>> getOrder(@Path("orderId") String orderId);

    @GET("api/v1/order/client-order/{clientOid}")
    Call<KucoinResponse<ActiveOrderResponse>> getOrderByClientOid(@Path("clientOid") String clientOid);

    enum TradeType {
        /** Spot Trading */
        TRADE,
        /** Cross Margin Trading */
        MARGIN_TRADE,
        /** Isolated Margin Trading */
        MARGIN_ISOLATED_TRADE
    }

    /**
     * @param symbol [Optional] Only list orders for a specific symbol.
     * @param side [Optional] buy or sell
     * @param type [Optional] limit, market, limit_stop or market_stop
     * @param tradeType The type of trading:TRADE-Spot Trading, MARGIN_TRADE-Cross Margin Trading, MARGIN_ISOLATED_TRADE-Isolated Margin Trading.
     * @param status [Optional] active or done(done as default), Only list orders with a specific status .
     * @param startAt [Optional] Start time (milisecond)
     * @param endAt [Optional] End time (milisecond)
     * @param pageSize Page size
     * @param currentPage Current page
     */
    @GET("api/v1/orders")
    Call<KucoinResponse<Pagination<OrderResponse>>> queryOrders(@Nullable @Query("symbol") String symbol,
                                                                @Nullable @Query("side") String side,
                                                                @Nullable @Query("type") String type,
                                                                @Query("tradeType") TradeType tradeType,
                                                                @Nullable @Query("status") String status,
                                                                @Nullable @Query("startAt") Long startAt,
                                                                @Nullable @Query("endAt") Long endAt,
                                                                @Nullable @Query("pageSize") Integer pageSize,
                                                                @Nullable @Query("currentPage") Integer currentPage);

    @GET("api/v1/trade-fees")
    Call<KucoinResponse<List<UserFeeResponse>>> getUserTradeFees(@Query("symbols") String symbols);

}
