/**
 * Copyright 2019 Mek Global Limited.
 */
package com.kucoin.model.websocket.event;

import com.kucoin.model.response.TickerResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Created by chenshiwei on 2019/1/23.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableTickerChangeEvent.class)
@JsonDeserialize(as = ImmutableTickerChangeEvent.class)
public interface TickerChangeEvent extends TickerResponse {
}
