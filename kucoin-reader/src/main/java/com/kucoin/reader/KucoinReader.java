package com.kucoin.reader;

import com.kucoin.model.websocket.event.KucoinEvent;
import com.kucoin.model.websocket.event.TickerChangeEvent;
import com.kucoin.misc.TickerCallback;
import com.kucoin.misc.TickerDataStreamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KucoinReader extends TickerDataStreamer {
    final static Logger LOGGER = LoggerFactory.getLogger(KucoinReader.class);

    public static KucoinReader createAllTopics() {
        return create(ALL_TICKERS);
    }

    public static KucoinReader create(String topic) {
        KucoinFileWriter writer = new KucoinFileWriter();
        LOGGER.info("Starting KucoinReader with topic: {}", topic);
        return new KucoinReader(topic, new TickerCallback() {
            @Override
            public void tickerCallback(KucoinEvent<TickerChangeEvent> event) {
                writer.tickerPriceChanged(event);
            }
        });
    }

    private KucoinReader(String topic, TickerCallback tickerCallback) {
        super(topic, tickerCallback);
    }
}
