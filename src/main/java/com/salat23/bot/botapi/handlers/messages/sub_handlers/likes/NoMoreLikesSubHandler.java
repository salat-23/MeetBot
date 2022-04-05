package com.salat23.bot.botapi.handlers.messages.sub_handlers.likes;

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
public class NoMoreLikesSubHandler implements IMessageSubHandler {

    private final UserRepository userRepository;
    private final MessageBuilder messageBuilder;

    public NoMoreLikesSubHandler(UserRepository userRepository, MessageBuilder messageBuilder) {
        this.userRepository = userRepository;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.LIKES_MO_MORE);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {
        user.setState(UserState.FORM_NEXT);
        userRepository.save(user);

        String noMoreFormsText = messageBuilder
                .getMessageTextByType(ResponseTemplateTypes.LIKES_ENDED, user.getGender());
        noMoreFormsText = messageBuilder.buildAssembleText(new MessageContext(user), noMoreFormsText);
        Bot.getInstance().execute(messageBuilder.createMessage(user,noMoreFormsText));

        return true;
    }
}
