package com.salat23.bot.botapi.handlers.messages.sub_handlers;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class UnregisteredSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public UnregisteredSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.NEW_USER, UserState.RESTORED_USER);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        //Will ask the user its name and change its state to ask name
        user.setState(UserState.START_REGISTRATION);
        userRepository.save(user);

        String welcomeMessage = messageBuilder.getMessageTextByType(ResponseTemplateTypes.WELCOME_TEXT);
        welcomeMessage = messageBuilder.buildAssembleText(new MessageContext(user), welcomeMessage);
        Bot.getInstance().execute(messageBuilder.createMessage(user, welcomeMessage));

        //Send the request for repeated update processing
        return true;
    }
}
