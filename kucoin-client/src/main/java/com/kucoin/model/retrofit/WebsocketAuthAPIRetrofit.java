/**
 * Copyright 2019 Mek Global Limited.
 */
package com.kucoin.model.retrofit;

import com.kucoin.model.response.KucoinResponse;
import com.kucoin.model.response.WebsocketTokenResponse;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by chenshiwei on 2019/1/15.
 */
public interface WebsocketAuthAPIRetrofit {

    @POST("api/v1/bullet-private")
    Call<KucoinResponse<WebsocketTokenResponse>> getPrivateToken();

}
