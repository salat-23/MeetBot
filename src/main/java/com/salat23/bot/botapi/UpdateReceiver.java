package com.salat23.bot.botapi;

import com.salat23.bot.botapi.handlers.EntryHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;

@Component
public class UpdateReceiver {

    private final EntryHandler entryHandler;

    public UpdateReceiver(@Qualifier("entryHandlerSetup") EntryHandler entryHandler) {
        this.entryHandler = entryHandler;
    }

    public void handle(Update update) {
        entryHandler.handle(update);
    }

}
