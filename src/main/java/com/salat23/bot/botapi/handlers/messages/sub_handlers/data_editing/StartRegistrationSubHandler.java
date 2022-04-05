package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class StartRegistrationSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public StartRegistrationSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.START_REGISTRATION);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        user.setState(UserState.ASK_NAME);
        user.setIsSearchable(false);
        userRepository.save(user);

        return true;
    }
}
