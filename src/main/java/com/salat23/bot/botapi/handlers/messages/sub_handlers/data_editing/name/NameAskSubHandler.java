package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.name;

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
public class NameAskSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public NameAskSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ASK_NAME);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {
        //Create response message asking user's name
        String responseMessageText = messageBuilder.getMessageTextByType(ResponseTemplateTypes.NAME_ENTER_ASK);
        responseMessageText = messageBuilder.buildAssembleText(new MessageContext(user), responseMessageText);
        //Set the right user state
        user.setState(UserState.ENTERING_NAME);
        userRepository.save(user);
        //Send the message to user
        Bot.getInstance().execute(messageBuilder.createMessage(user, responseMessageText));
        return false;
    }
}
