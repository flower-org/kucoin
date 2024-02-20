/**
 * Copyright 2019 Mek Global Limited.
 */
package com.kucoin.model.retrofit;

import com.kucoin.model.response.AllTickersResponse;
import com.kucoin.model.response.KucoinResponse;
import com.kucoin.model.response.SymbolResponse;
import com.kucoin.model.response.SymbolTickResponse;
import com.kucoin.model.response.TickerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

/**
 * Created by chenshiwei on 2019/1/11.
 */
public interface SymbolAPIRetrofit {

    @GET("api/v1/symbols")
    Call<KucoinResponse<List<SymbolResponse>>> getSymbols();

    @GET("api/v1/market/orderbook/level1")
    Call<KucoinResponse<TickerResponse>> getTicker(@Query("symbol") String symbol);

    @GET("api/v1/market/stats")
    Call<KucoinResponse<SymbolTickResponse>> getMarketStats(@Query("symbol") String symbol);

    @GET("api/v1/market/allTickers")
    Call<KucoinResponse<AllTickersResponse>> getAllTickers();

    @GET("api/v1/markets")
    Call<KucoinResponse<List<String>>> getMarketList();
}
