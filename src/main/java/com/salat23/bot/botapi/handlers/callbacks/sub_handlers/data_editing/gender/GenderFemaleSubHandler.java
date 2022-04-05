package com.salat23.bot.botapi.handlers.callbacks.sub_handlers.data_editing.gender;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.callbacks.Callbacks;
import com.salat23.bot.botapi.handlers.callbacks.sub_handlers.ICallbackSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.models.GenderEnum;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class GenderFemaleSubHandler implements ICallbackSubHandler {

    private final UserRepository userRepository;

    public GenderFemaleSubHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Callbacks getSupportedCallback() {
        return Callbacks.FEMALE;
    }

    @Override
    public List<UserState> getSupportedUserStates() {
        return List.of(UserState.ENTERING_GENDER);
    }

    @Override
    public boolean handle(User user) throws TelegramApiException {

        user.setGender(GenderEnum.WOMAN);
        user.setTarget(GenderEnum.MAN);
        user.setState(UserState.ASK_CONFIRMING_GENDER);
        userRepository.save(user);

        return true;

    }
}
