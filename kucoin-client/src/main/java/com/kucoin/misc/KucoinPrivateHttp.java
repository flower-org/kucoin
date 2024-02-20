package com.kucoin.misc;

import com.kucoin.model.auth.AuthenticationInterceptor;
import com.kucoin.model.response.WebsocketTokenResponse;
import com.kucoin.model.retrofit.AccountAPIRetrofit;
import com.kucoin.model.retrofit.LoanAPIRetrofit;
import com.kucoin.model.retrofit.MarginAPIRetrofit;
import com.kucoin.model.retrofit.OrderAPIRetrofit;
import com.kucoin.model.retrofit.WebsocketAuthAPIRetrofit;
import com.kucoin.model.retrofit.WithdrawalAPIRetrofit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static com.kucoin.misc.KucoinHttp.API_KEY_VERSION;
import static com.kucoin.misc.KucoinHttp.BASE_URL;
import static com.kucoin.misc.KucoinHttp.JACKSON_CONVERTER_FACTORY;
import static com.kucoin.misc.KucoinHttp.buildHttpClient;
import static com.kucoin.misc.KucoinHttp.executeSync;

public class KucoinPrivateHttp {
    public static WebsocketTokenResponse getPrivateToken(WebsocketAuthAPIRetrofit authApi) throws IOException {
        return executeSync(authApi.getPrivateToken());
    }

    private static OkHttpClient buildPrivateHttpClient(String apiKey, String secret, String passPhrase, Integer authApiKeyVersion) {
        return buildHttpClient(new AuthenticationInterceptor(apiKey, secret, passPhrase, authApiKeyVersion));
    }

    static ConcurrentHashMap<String, PrivateAPIPack> apiPackCache = new ConcurrentHashMap<>();

    public static PrivateAPIPack getAPIPack(String apiKey, String secret, String passPhrase) {
        String packId = apiKey + secret + passPhrase;

        PrivateAPIPack apiPack = apiPackCache.get(packId);
        while (apiPack == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JACKSON_CONVERTER_FACTORY)
                    .client(buildPrivateHttpClient(apiKey, secret, passPhrase, API_KEY_VERSION))
                    .build();

            apiPack = ImmutablePrivateAPIPack.builder()
                    .authAPI(retrofit.create(WebsocketAuthAPIRetrofit.class))
                    .accountAPI(retrofit.create(AccountAPIRetrofit.class))
                    .marginAPI(retrofit.create(MarginAPIRetrofit.class))
                    .loanAPI(retrofit.create(LoanAPIRetrofit.class))
                    .withdrawalAPI(retrofit.create(WithdrawalAPIRetrofit.class))
                    .orderAPI(retrofit.create(OrderAPIRetrofit.class))
                    .build();
            PrivateAPIPack previousPack = apiPackCache.putIfAbsent(packId, apiPack);
            if (previousPack == null) {
                break;
            } else {
                apiPack = previousPack;
            }
        }

        return apiPack;
    }
}
