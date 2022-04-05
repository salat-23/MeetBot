package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.target_age;

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
public class TargetAgeMaxAskSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public TargetAgeMaxAskSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ASK_TARGET_AGE_MAX);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        user.setState(UserState.ENTERING_TARGET_AGE_MAX);
        userRepository.save(user);

        String response = messageBuilder
                .getMessageTextByType(ResponseTemplateTypes.TARGET_AGE_MAX_ASK, user.getGender());
        response = messageBuilder.buildAssembleText(new MessageContext(user), response);

        Bot.getInstance().execute(messageBuilder.createMessage(user, response));

        return false;
    }
}
