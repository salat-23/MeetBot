package com.salat23.bot.botapi.handlers.callbacks.sub_handlers;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.callbacks.Callbacks;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.models.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
//This handler is used if the callback command does not exist
public class UnknownCallbackHandler implements ICallbackSubHandler {

    private final MessageBuilder messageBuilder;

    public UnknownCallbackHandler(MessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    @Override
    public Callbacks getSupportedCallback() {
        return Callbacks.UNKNOWN;
    }

    @Override
    public List<UserState> getSupportedUserStates() {
        return List.of(UserState.values());
    }

    @Override
    public boolean handle(User user) {
        try {
            //Send the message
            Bot.getInstance().execute(messageBuilder
                            .createMessage(user, messageBuilder
                                    //Get the message text
                                    .getMessageTextByType(
                                            ResponseTemplateTypes.UNKNOWN_COMMAND)));
        } catch (TelegramApiException exception) {
            exception.printStackTrace();
        }
        return false;
    }

}
