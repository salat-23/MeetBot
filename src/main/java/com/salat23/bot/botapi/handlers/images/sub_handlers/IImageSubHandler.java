package com.salat23.bot.botapi.handlers.images.sub_handlers;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.models.User;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public interface IImageSubHandler {

    List<UserState> getSupportedStates();
    boolean handle(User user, List<PhotoSize> photos) throws TelegramApiException;

}
