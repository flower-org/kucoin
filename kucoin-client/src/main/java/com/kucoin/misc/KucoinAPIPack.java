package com.kucoin.misc;

import com.kucoin.model.retrofit.KucoinAPIRetrofit;
import org.immutables.value.Value;

@Value.Immutable
public interface KucoinAPIPack {
    KucoinAPIRetrofit kucoinAPI();
}
