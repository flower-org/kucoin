package com.kucoin.misc;

import com.kucoin.model.request.AccountTransferV2Request;
import com.kucoin.model.request.MarginOrderCreateRequest;
import com.kucoin.model.request.SubMasterTransferV2Request;
import com.kucoin.model.request.WithdrawApplyRequest;
import com.kucoin.model.response.AccountBalancesResponse;
import com.kucoin.model.response.AllTickersResponse;
import com.kucoin.model.response.CurrencyDetailV2Response;
import com.kucoin.model.response.CurrencyExtendedInfoResponse;
import com.kucoin.model.response.CurrencyResponse;
import com.kucoin.model.response.IsolatedMarginAccountInfo;
import com.kucoin.model.response.IsolatedMarginBorrowRequest;
import com.kucoin.model.response.IsolatedMarginQuickRepayRequest;
import com.kucoin.model.response.MarginAccountResponse;
import com.kucoin.model.response.MarginConfigResponse;
import com.kucoin.model.response.MarginOrderCreateResponse;
import com.kucoin.model.response.OrderResponse;
import com.kucoin.model.response.Pagination;
import com.kucoin.model.response.SubAccountBalanceResponse;
import com.kucoin.model.response.SubAccountUserInfoV1;
import com.kucoin.model.response.WithdrawApplyResponse;
import com.kucoin.model.retrofit.AccountAPIRetrofit;
import com.kucoin.model.retrofit.CurrencyAPIRetrofit;
import com.kucoin.model.retrofit.KucoinAPIRetrofit;
import com.kucoin.model.retrofit.MarginAPIRetrofit;
import com.kucoin.model.retrofit.OrderAPIRetrofit;
import com.kucoin.model.retrofit.SymbolAPIRetrofit;
import com.kucoin.model.retrofit.WithdrawalAPIRetrofit;
import com.kucoin.model.websocket.event.ImmutableKucoinEvent;
import com.kucoin.model.websocket.event.KucoinEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.WebSocket;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kucoin.misc.KucoinHttp.executeSync;
import static com.kucoin.misc.KucoinHttp.executeSyncNullable;

public class KucoinRequester {
    @Nullable
    public static String ping(WebSocket webSocket, String requestId) {
        KucoinEvent<Object> ping = ImmutableKucoinEvent.builder()
                .id(requestId)
                .type("ping")
                .build();
        return webSocket.send(serialize(ping)) ? requestId : null;
    }

    @Nullable
    static String subscribeInternal(WebSocket webSocket, String topic, boolean privateChannel, boolean response) {
        String uuid = UUID.randomUUID().toString();
        KucoinEvent<Object> subscribe = ImmutableKucoinEvent.builder()
                .id(uuid)
                .type("subscribe")
                .topic(topic)
                .privateChannel(privateChannel)
                .response(response)
                .build();
        return webSocket.send(serialize(subscribe)) ? uuid : null;
    }

    @Nullable
    public static String subscribeOnTicker(WebSocket webSocket, String... symbols) {
        String topic = "/market/ticker:" + Arrays.stream(symbols).collect(Collectors.joining(","));
        return subscribeInternal(webSocket, topic, false, true);
    }

    @Nullable
    public static String subscribeOnPrivateTicker(WebSocket webSocket) {
        String topic = "/spotMarket/tradeOrders";
        return subscribeInternal(webSocket, topic, true, true);
    }


    static String serialize(Object o) {
        try {
            return KucoinHttp.OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException("Failure serializing object", var3);
        }
    }

    /** Transfer to subaccount */
    public static Map<String, String> transferBetweenSubAndMasterV2(AccountAPIRetrofit accountApi, SubMasterTransferV2Request request) throws IOException {
        return executeSync(accountApi.transferBetweenSubAndMasterV2(request));
    }

    /** Get all subaccounts */
    public static List<SubAccountUserInfoV1> getUserInfoForAllSubAccounts(AccountAPIRetrofit accountApi) throws IOException {
        return executeSync(accountApi.getUserInfoForAllSubAccounts());
    }

    /** Currencies */
    public static List<CurrencyResponse> getCurrencies(CurrencyAPIRetrofit currencyApi) throws IOException {
        return executeSync(currencyApi.getCurrencies());
    }

    /** Currency Detail */
    public static CurrencyDetailV2Response getCurrencyDetailV2(CurrencyAPIRetrofit currencyApi, String currency) throws IOException {
        return executeSync(currencyApi.getCurrencyDetailV2(currency, null));
    }

    /** Currency Detail
     * @return*/
    public static CurrencyExtendedInfoResponse getCurrencyExtendedInfo(KucoinAPIRetrofit kucoinApi, String currency) throws IOException {
        return executeSync(kucoinApi.getExtendedInfo(currency));
    }

    /** Subaccount balances */
    public static SubAccountBalanceResponse getSubAccount(AccountAPIRetrofit accountApi, String subUserId) throws IOException {
        return executeSync(accountApi.getSubAccount(subUserId));
    }

    public static WithdrawApplyResponse applyWithdraw(WithdrawalAPIRetrofit withdrawalApi, WithdrawApplyRequest request) throws IOException {
        return executeSync(withdrawalApi.applyWithdraw(request));
    }

    /** Account list
     *
     * @param currency [Optional] Currency
     * @param type [Optional] Account type: main, trade, margin
     */
    public static List<AccountBalancesResponse> getAccountList(AccountAPIRetrofit accountApi, @Nullable String currency, @Nullable String type) throws IOException {
        return executeSync(accountApi.getAccountList(currency, type));
    }

    public static Pagination<OrderResponse> getOrderList(OrderAPIRetrofit orderApi, OrderAPIRetrofit.TradeType tradeType, String status, @Nullable Integer pageSize, @Nullable Integer currentPage) throws IOException {
        return executeSync(orderApi.queryOrders(null, null, null, tradeType, status, null, null, pageSize, currentPage));
    }

    public static Map<String, String> applyTransfer2(AccountAPIRetrofit accountApi, AccountTransferV2Request request) throws IOException {
        return executeSync(accountApi.applyTransfer2(request));
    }

    public static AllTickersResponse getAllTickers(SymbolAPIRetrofit symbolAPI) throws IOException {
        return executeSync(symbolAPI.getAllTickers());
    }

    public static IsolatedMarginAccountInfo getIsolatedMarginAccountInfo(MarginAPIRetrofit marginApi, @Nullable String balanceCurrency) throws IOException {
        return executeSync(marginApi.getIsolatedMarginAccounts(balanceCurrency));
    }

    public static IsolatedMarginAccountInfo.Asset getIsolatedMarginAccount(MarginAPIRetrofit marginApi, String symbol) throws IOException {
        return executeSync(marginApi.getIsolatedMarginAccount(symbol));
    }

    public static Object isolatedMarginBorrow(MarginAPIRetrofit marginApi, IsolatedMarginBorrowRequest request) throws IOException {
        return executeSync(marginApi.isolatedMarginBorrow(request));
    }

    public static void isolatedMarginQuickRepay(MarginAPIRetrofit marginApi, IsolatedMarginQuickRepayRequest request) throws IOException {
        executeSyncNullable(marginApi.isolatedMarginQuickRepay(request));
    }

    public static MarginOrderCreateResponse createMarginOrder(MarginAPIRetrofit marginApi, MarginOrderCreateRequest request) throws IOException {
        return executeSync(marginApi.createMarginOrder(request));
    }

    public static MarginAccountResponse getMarginAccount(MarginAPIRetrofit marginApi) throws IOException {
        return executeSync(marginApi.getMarginAccount());
    }

    public static MarginConfigResponse getMarginConfig(MarginAPIRetrofit marginApi) throws IOException {
        return executeSync(marginApi.getMarginConfig());
    }
}



/*
    Unused attempts to get isolated margin account info



    public static List<MarginPriceStrategy> getMarginPriceStrategy(MarginAPIRetrofit marginApi, String marginModel) throws IOException {
        return executeSync(marginApi.getMarginPriceStrategy(marginModel));
    }

    public static List<IsolatedMarginPriceStrategy> getIsolatedMarginPriceStrategies(MarginAPIRetrofit marginApi) throws IOException {
        return executeSync(marginApi.getIsolatedMarginPriceStrategies());
    }

    public static void isolatedMarginSingleRepay(MarginAPIRetrofit marginApi, IsolatedMarginSingleRepayRequest request) throws IOException {
        executeSyncNullable(marginApi.isolatedMarginSingleRepay(request));
    }

*/
