package com.kucoin.model.retrofit;

import com.kucoin.model.response.CurrencyExtendedInfoResponse;
import com.kucoin.model.response.KucoinResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KucoinAPIRetrofit {

    @GET(value = "_api/pool-info/currency/info")
    Call<KucoinResponse<CurrencyExtendedInfoResponse>> getExtendedInfo(@Query("symbol") String symbol);

}
