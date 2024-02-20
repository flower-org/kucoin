package com.kucoin.misc;

import com.kucoin.model.retrofit.CurrencyAPIRetrofit;
import com.kucoin.model.retrofit.KucoinAPIRetrofit;
import com.kucoin.model.retrofit.SymbolAPIRetrofit;
import org.immutables.value.Value;

@Value.Immutable
public interface PublicAPIPack {
    CurrencyAPIRetrofit currencyAPI();
    SymbolAPIRetrofit symbolAPI();
    KucoinAPIRetrofit kucoinAPI();
}
