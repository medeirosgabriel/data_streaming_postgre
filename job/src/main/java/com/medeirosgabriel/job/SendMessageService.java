package com.medeirosgabriel.job;

public interface SendMessageService<T> {
    void sendMessage(T message);
}
