package com.salat23.bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface IHandler {

    IHandler setNext(IHandler handler);

    void handle(Update update);

}
