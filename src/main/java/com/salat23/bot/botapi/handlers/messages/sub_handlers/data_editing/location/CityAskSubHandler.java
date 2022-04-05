package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.location;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class CityAskSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public CityAskSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ASK_CITY);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {
        //Create response message asking user's city
        String responseMessageText = messageBuilder
                .getMessageTextByType(ResponseTemplateTypes.CITY_ENTER_ASK, user.getGender());
        responseMessageText = messageBuilder.buildAssembleText(new MessageContext(user), responseMessageText);
        //Set the right user state
        user.setState(UserState.ENTERING_CITY);
        userRepository.save(user);

        SendMessage sendMessage = messageBuilder.createMessage(user, responseMessageText);
        sendMessage.setReplyMarkup(messageBuilder.getLocationKeyboard());

        //Send the message to user
        Bot.getInstance().execute(sendMessage);
        return false;
    }
}
