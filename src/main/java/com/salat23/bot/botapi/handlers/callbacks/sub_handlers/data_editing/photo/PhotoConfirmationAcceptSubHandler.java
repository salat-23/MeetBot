package com.salat23.bot.botapi.handlers.callbacks.sub_handlers.data_editing.photo;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.callbacks.Callbacks;
import com.salat23.bot.botapi.handlers.callbacks.sub_handlers.ICallbackSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class PhotoConfirmationAcceptSubHandler implements ICallbackSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public PhotoConfirmationAcceptSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public Callbacks getSupportedCallback() {
        return Callbacks.ACCEPT;
    }

    @Override
    public List<UserState> getSupportedUserStates() {
        return List.of(UserState.CONFIRMING_PHOTO);
    }

    @Override
    public boolean handle(User user) throws TelegramApiException {
        user.setState(UserState.COMPLETE_REGISTRATION);
        userRepository.save(user);

        String response = messageBuilder.getMessageTextByType(ResponseTemplateTypes.PHOTO_ENTER_AFTER_CONFIRM);
        response = messageBuilder.buildAssembleText(new MessageContext(user), response);
        Bot.getInstance().execute(messageBuilder.createMessage(user, response));

        return true;
    }
}