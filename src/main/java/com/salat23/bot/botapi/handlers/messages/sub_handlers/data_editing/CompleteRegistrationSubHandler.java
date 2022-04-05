package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.List;

@Component
public class CompleteRegistrationSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public CompleteRegistrationSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.COMPLETE_REGISTRATION);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        user.setRegistrationDate(LocalDate.now());
        user.setState(UserState.IN_MENU);
        user.setIsSearchable(true);
        userRepository.save(user);

        return true;
    }
}
