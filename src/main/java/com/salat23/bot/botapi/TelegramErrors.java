package com.salat23.bot.botapi;

import lombok.Getter;

@Getter
public enum TelegramErrors {
    BLOCKED_BY_USER("Error sending message: [403] Forbidden: bot was blocked by the user");

    private final String error;

    TelegramErrors(String error) {
        this.error = error;
    }
}
