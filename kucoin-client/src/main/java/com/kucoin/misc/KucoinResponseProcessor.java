package com.kucoin.misc;

import com.kucoin.model.websocket.event.AccountChangeEvent;
import com.kucoin.model.websocket.event.KucoinEvent;
import com.kucoin.model.websocket.event.Level2ChangeEvent;
import com.kucoin.model.websocket.event.Level2Event;
import com.kucoin.model.websocket.event.Level3ChangeEvent;
import com.kucoin.model.websocket.event.Level3Event;
import com.kucoin.model.websocket.event.MatchExcutionChangeEvent;
import com.kucoin.model.websocket.event.OrderActivateEvent;
import com.kucoin.model.websocket.event.OrderChangeEvent;
import com.kucoin.model.websocket.event.SnapshotEvent;
import com.kucoin.model.websocket.event.StopOrderEvent;
import com.kucoin.model.websocket.event.TickerChangeEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;
//
public class KucoinResponseProcessor extends WebSocketListener {
    final static Logger LOGGER = LoggerFactory.getLogger(KucoinResponseProcessor.class);

    private static final String TYPE = "type";
    private static final String MESSAGE = "message";
    private static final String TOPIC = "topic";

    private static final String TICKER = "/market/ticker:";
    private static final String LEVEL2 = "/market/level2:";
    private static final String MATCH = "/market/match:";
    private static final String MARKET_LEVEL3 = "/market/level3:";
    private static final String SNAPSHOT = "/market/snapshot:";
    private static final String SPOT_LEVEL3 = "/spotMarket/level3:";
    private static final String LEVEL2_DEPTH5 = "/spotMarket/level2Depth5:";
    private static final String LEVEL2_DEPTH50 = "/spotMarket/level2Depth50:";

    private static final String BALANCE = "/account/balance";
    private static final String TRADE_ORDERS = "/spotMarket/tradeOrders";
    private static final String ADVANCED_ORDERS = "/spotMarket/advancedOrders";
    private static final String SUBJECT = "subject";
    private static final String STOP_ORDER = "stopOrder";

    @Nullable
    final KucoinListenerPublic kucoinListenerPublic;
    @Nullable
    final KucoinListenerPrivate kucoinListenerPrivate;
    final KucoinConnectionListener connectionListener;

    public KucoinResponseProcessor(KucoinListenerPublic kucoinListenerPublic, KucoinConnectionListener connectionListener) {
        this.kucoinListenerPublic = kucoinListenerPublic;
        this.kucoinListenerPrivate = null;
        this.connectionListener = connectionListener;
    }

    public KucoinResponseProcessor(KucoinListenerPrivate kucoinListenerPrivate, KucoinConnectionListener connectionListener) {
        this.kucoinListenerPrivate = kucoinListenerPrivate;
        this.kucoinListenerPublic = null;
        this.connectionListener = connectionListener;
    }

    private JsonNode tree(String text) {
        try {
            return KucoinHttp.OBJECT_MAPPER.readTree(text);
        } catch (IOException ie) {
            throw new RuntimeException("Failed to deserialize message: " + text, ie);
        }
    }

    private <T> T deserialize(String text, TypeReference<T> typeReference) {
        try {
            return KucoinHttp.OBJECT_MAPPER.readValue(text, typeReference);
        } catch (IOException ie) {
            throw new RuntimeException("Failed to deserialize message: " + text, ie);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            JsonNode jsonObject = this.tree(text);
            String type = jsonObject.get(TYPE).asText();

            if (!type.equals(MESSAGE)) {
                if (kucoinListenerPublic != null) {
                    kucoinListenerPublic.otherCallback(this.deserialize(text, new TypeReference<KucoinEvent>() {
                    }));
                } else if (kucoinListenerPrivate != null) {
                    kucoinListenerPrivate.otherCallback(this.deserialize(text, new TypeReference<KucoinEvent>() {
                    }));
                }
            } else {
                String topic = jsonObject.get(TOPIC).asText();

                if (kucoinListenerPublic != null) {
                    //Public
                    if (topic.contains(TICKER)) {
                        kucoinListenerPublic.tickerCallback(this.deserialize(text, new TypeReference<KucoinEvent<TickerChangeEvent>>() {
                        }));
                    } else if (topic.contains(LEVEL2)) {
                        kucoinListenerPublic.level2Callback(this.deserialize(text, new TypeReference<KucoinEvent<Level2ChangeEvent>>() {
                        }));
                    } else if (topic.contains(MATCH)) {
                        kucoinListenerPublic.matchDataCallback(this.deserialize(text, new TypeReference<KucoinEvent<MatchExcutionChangeEvent>>() {
                        }));
                    } else if (topic.contains(SNAPSHOT)) {
                        kucoinListenerPublic.snapshotCallback(this.deserialize(text, new TypeReference<KucoinEvent<SnapshotEvent>>() {
                        }));
                    } else if (topic.contains(LEVEL2_DEPTH5)) {
                        kucoinListenerPublic.level2Depth5Callback(this.deserialize(text, new TypeReference<KucoinEvent<Level2Event>>() {
                        }));
                    } else if (topic.contains(LEVEL2_DEPTH50)) {
                        kucoinListenerPublic.level2Depth50Callback(this.deserialize(text, new TypeReference<KucoinEvent<Level2Event>>() {
                        }));
                    } else
                        //Deprecated?
                        if (topic.contains(MARKET_LEVEL3)) {
                            kucoinListenerPublic.level3Callback(this.deserialize(text, new TypeReference<KucoinEvent<Level3ChangeEvent>>() {
                            }));
                        } else if (topic.contains(SPOT_LEVEL3)) {
                            kucoinListenerPublic.level3V2Callback(this.deserialize(text, new TypeReference<KucoinEvent<Level3Event>>() {
                            }));
                        }
                } else if (kucoinListenerPrivate != null) {
                    //Private
                    if (topic.contains(BALANCE)) {
                        kucoinListenerPrivate.accountChangeCallback(this.deserialize(text, new TypeReference<KucoinEvent<AccountChangeEvent>>() {
                        }));
                    } else if (topic.contains(TRADE_ORDERS)) {
                        kucoinListenerPrivate.orderChangeCallback(this.deserialize(text, new TypeReference<KucoinEvent<OrderChangeEvent>>() {
                        }));
                    } else if (topic.contains(ADVANCED_ORDERS)) {
                        String subject = jsonObject.get(SUBJECT).asText();
                        if (Objects.equals(subject, STOP_ORDER)) {
                            kucoinListenerPrivate.advancedOrderCallback(this.deserialize(text, new TypeReference<KucoinEvent<StopOrderEvent>>() {
                            }));
                        } else {
                            //TODO: what other advancedOrders messages can arrive ???
                            LOGGER.info("Unrecognized advanced order message arrived, ignored: {}", text);
                        }
                    } else
                        //Deprecated?
                        if (topic.contains(MARKET_LEVEL3)) {
                            kucoinListenerPrivate.orderActivateCallback(this.deserialize(text, new TypeReference<KucoinEvent<OrderActivateEvent>>() {
                            }));
                        }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error processing message {}", text, e);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, okhttp3.Response response) {
        connectionListener.onOpen(webSocket, response);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        connectionListener.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        connectionListener.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable okhttp3.Response response) {
        connectionListener.onFailure(webSocket, t, response);
    }
}
