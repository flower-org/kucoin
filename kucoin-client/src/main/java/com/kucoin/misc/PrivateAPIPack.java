package com.kucoin.misc;

import com.kucoin.model.retrofit.AccountAPIRetrofit;
import com.kucoin.model.retrofit.LoanAPIRetrofit;
import com.kucoin.model.retrofit.MarginAPIRetrofit;
import com.kucoin.model.retrofit.OrderAPIRetrofit;
import com.kucoin.model.retrofit.WebsocketAuthAPIRetrofit;
import com.kucoin.model.retrofit.WithdrawalAPIRetrofit;
import org.immutables.value.Value;

@Value.Immutable
public interface PrivateAPIPack {
    WebsocketAuthAPIRetrofit authAPI();
    AccountAPIRetrofit accountAPI();
    MarginAPIRetrofit marginAPI();
    LoanAPIRetrofit loanAPI();
    WithdrawalAPIRetrofit withdrawalAPI();
    OrderAPIRetrofit orderAPI();
}
