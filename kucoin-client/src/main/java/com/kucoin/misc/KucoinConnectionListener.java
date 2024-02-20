package com.kucoin.misc;

import okhttp3.WebSocket;

import javax.annotation.Nullable;

public interface KucoinConnectionListener {
    default void onOpen(WebSocket webSocket, okhttp3.Response response) {
    }

    default void onClosing(WebSocket webSocket, int code, String reason) {
    }

    default void onClosed(WebSocket webSocket, int code, String reason) {
    }

    default void onFailure(WebSocket webSocket, Throwable t, @Nullable okhttp3.Response response) {
    }
}
