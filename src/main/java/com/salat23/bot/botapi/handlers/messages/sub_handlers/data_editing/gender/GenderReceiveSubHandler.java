package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.gender;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.models.GenderEnum;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class GenderReceiveSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public GenderReceiveSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ENTERING_GENDER);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        if (message.equals(GenderEnum.MAN.getShortName())) {
            user.setGender(GenderEnum.MAN);
            user.setTarget(GenderEnum.WOMAN);
            user.setState(UserState.ASK_AGE);
            userRepository.save(user);
        } else if (message.equals(GenderEnum.WOMAN.getShortName())) {
            user.setGender(GenderEnum.WOMAN);
            user.setTarget(GenderEnum.MAN);
            user.setState(UserState.ASK_AGE);
            userRepository.save(user);
        } else {

            user.setState(UserState.ASK_GENDER);
            userRepository.save(user);
        }

        return true;
    }
}
