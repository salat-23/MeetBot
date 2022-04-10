package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.photo;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class PhotoReceiveSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public PhotoReceiveSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ENTERING_PHOTO);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {


        //TODO: get rid of hardcoded strings
        if (message.equals("Оставить текущее") && user.getPhoto() != null) {
            user.setState(UserState.ASK_CONFIRMING_PHOTO);
            userRepository.save(user);
            return true;
        }

        //Set user state
        user.setState(UserState.ASK_PHOTO);
        userRepository.save(user);

        //Returning true because we need to switch the state
        return true;
    }

}
