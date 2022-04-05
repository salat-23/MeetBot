package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.photo;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class PhotoAskSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public PhotoAskSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ASK_PHOTO);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        user.setState(UserState.ENTERING_PHOTO);
        userRepository.save(user);

        String askPhoto = messageBuilder
                .getMessageTextByType(ResponseTemplateTypes.PHOTO_ENTER_ASK, user.getGender());
        askPhoto = messageBuilder.buildAssembleText(new MessageContext(user), askPhoto);
        Bot.getInstance().execute(messageBuilder.createMessage(user, askPhoto));

        return false;
    }
}
