package com.salat23.bot.botapi.handlers.messages.sub_handlers;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.models.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public interface IMessageSubHandler {

    List<UserState> getSupportedStates();

    boolean handle(User user, String message) throws TelegramApiException;

}
