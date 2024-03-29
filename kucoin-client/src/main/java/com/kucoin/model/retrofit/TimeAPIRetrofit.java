/**
 * Copyright 2019 Mek Global Limited.
 */
package com.kucoin.model.retrofit;

import com.kucoin.model.response.KucoinResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chenshiwei on 2019/1/15.
 */
public interface TimeAPIRetrofit {

    @GET("api/v1/timestamp")
    Call<KucoinResponse<Long>> getServerTimeStamp();

}
