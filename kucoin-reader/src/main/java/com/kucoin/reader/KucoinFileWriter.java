package com.kucoin.reader;

import com.kucoin.model.response.TickerResponse;
import com.kucoin.model.websocket.event.KucoinEvent;
import com.kucoin.model.websocket.event.TickerChangeEvent;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class KucoinFileWriter {
    final ConcurrentHashMap<String, Pair<String, FileWriter>> fileWriters = new ConcurrentHashMap<>();
    final ConcurrentHashMap<String, AtomicReference<AskAndBid>> lastAskAndBidMap = new ConcurrentHashMap<>();

    static String recordToString(String ticker, TickerResponse resp) {
        return ticker + "," + resp.sequence() + "," + resp.bestAsk() + "," + resp.bestBid() + "," + resp.size() +
                "," + resp.price() + "," + resp.bestAskSize() + "," + resp.bestBidSize() + "," + resp.time();
    }

    static String getTicker(String mixed) {
        if (mixed.startsWith("/market/ticker:all:")) {
            return mixed.substring("/market/ticker:all:".length());
        } else if (mixed.startsWith("/market/ticker:") && mixed.endsWith(":trade.ticker")) {
            return mixed.substring("/market/ticker:".length(), mixed.indexOf(":trade.ticker"));
        }
        return mixed;
    }

    static String getPstDate(long ms) {
        Date date = new java.util.Date(ms);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("PST"));
        return sdf.format(date);
    }

    private boolean isQuoteUpdated(String ticker, TickerChangeEvent ev) {
        AtomicReference<AskAndBid> last = lastAskAndBidMap.get(ticker);
        while (last == null) {
            lastAskAndBidMap.putIfAbsent(ticker, new AtomicReference<>(null));
            last = lastAskAndBidMap.get(ticker);
        }

        while (true) {
            AskAndBid lastAskAndBid = last.get();
            if (lastAskAndBid == null || !lastAskAndBid.isTheSame(ev.bestAsk(), ev.bestBid())) {
                if (last.compareAndSet(lastAskAndBid, new AskAndBid(ev.bestAsk(), ev.bestBid()))) {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    void saveMetric(String ticker, TickerChangeEvent ev) throws IOException {
        String pstDate = getPstDate(ev.time());

        File dir = new File(pstDate);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String fileName = pstDate + "/" + ticker + "_PST_" + pstDate;

        Pair<String, FileWriter> bwPair = fileWriters.get(ticker);
        if (bwPair != null && !bwPair.getLeft().equals(fileName)) {
            bwPair.getRight().close();
            fileWriters.remove(ticker, bwPair);

            bwPair = fileWriters.get(ticker);
        }

        while (bwPair == null) {
            File f = new File(fileName);
            if (!f.exists()) {
                f.createNewFile();
            }

            bwPair = Pair.of(fileName, new FileWriter(fileName, true));
            fileWriters.put(ticker, bwPair);
        }

        FileWriter bw = bwPair.getRight();
        bw.write(recordToString(ticker, ev) + "\n");
        bw.flush();
    }

    public void tickerPriceChanged(KucoinEvent<TickerChangeEvent> event) {
        String ticker = getTicker(event.topic() + ":" + event.subject());
        TickerChangeEvent ev = Preconditions.checkNotNull(event.data());

        if (isQuoteUpdated(ticker, ev)) {
            //LOGGER.info(toString(ticker, ev));
            try {
                saveMetric(ticker, ev);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
