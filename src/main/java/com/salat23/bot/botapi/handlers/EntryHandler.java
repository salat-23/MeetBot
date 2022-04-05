package com.salat23.bot.botapi.handlers;

import com.salat23.bot.repository.BoostRepository;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EntryHandler extends Handler {

    public EntryHandler(UserRepository userRepository, BoostRepository boostRepository) {
        super(userRepository, boostRepository);
    }

    @Override
    public void handle(Update update) {
        nextHandler.handle(update);
    }
}
