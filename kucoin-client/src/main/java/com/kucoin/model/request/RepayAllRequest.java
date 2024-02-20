/*
 * Copyright (c) 2019 Mek Global Limited
 */

package com.kucoin.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableRepayAllRequest.class)
@JsonDeserialize(as = ImmutableRepayAllRequest.class)
public interface RepayAllRequest {

    @Nullable
    String currency();

    @Nullable
    BigDecimal size();

    @Nullable
    RepaySeqStrategy sequence();
}
