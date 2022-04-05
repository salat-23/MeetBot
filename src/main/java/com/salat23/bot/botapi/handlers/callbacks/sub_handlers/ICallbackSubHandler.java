package com.salat23.bot.botapi.handlers.callbacks.sub_handlers;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.callbacks.Callbacks;
import com.salat23.bot.models.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public interface ICallbackSubHandler {

    Callbacks getSupportedCallback();
    List<UserState> getSupportedUserStates();

    boolean handle(User user) throws TelegramApiException;

}
