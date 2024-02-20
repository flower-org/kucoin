package com.kucoin.reader;

import java.math.BigDecimal;

public class AskAndBid {
    public final BigDecimal bestAsk;
    public final BigDecimal bestBid;

    public AskAndBid(BigDecimal bestAsk, BigDecimal bestBid) {
        this.bestAsk = bestAsk;
        this.bestBid = bestBid;
    }

    boolean isTheSame(BigDecimal newBestAsk, BigDecimal newBestBid) {
        return newBestAsk.compareTo(bestAsk) == 0 && newBestBid.compareTo(bestBid) == 0;
    }
}
