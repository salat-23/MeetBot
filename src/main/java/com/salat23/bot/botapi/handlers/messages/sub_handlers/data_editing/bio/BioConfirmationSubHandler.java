package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.bio;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.botapi.utility.IConfirmationUtility;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class BioConfirmationSubHandler implements IMessageSubHandler {

    private final IConfirmationUtility confirmationUtility;
    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public BioConfirmationSubHandler(IConfirmationUtility confirmationUtility, MessageBuilder messageBuilder, UserRepository userRepository) {
        this.confirmationUtility = confirmationUtility;
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }


    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.CONFIRMING_BIO);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        boolean isPositive = confirmationUtility.doesConfirm(message);
        if (isPositive) {
            //If user has confirmed its name

            //Set the state to entering gender
            user.setState(UserState.ASK_PHOTO);
            userRepository.save(user);
            String responseText = messageBuilder.getMessageTextByType(ResponseTemplateTypes.BIO_ENTER_AFTER_CONFIRM);
            responseText = messageBuilder.buildAssembleText(new MessageContext(user), responseText);
            Bot.getInstance().execute(messageBuilder.createMessage(user, responseText));
        } else {
            //If user has declined the chosen name

            //Set state to ask the user its name again
            user.setState(UserState.ASK_BIO);
            userRepository.save(user);
            //Send decline message
            String responseText = messageBuilder.getMessageTextByType(ResponseTemplateTypes.BIO_ENTER_AFTER_DECLINE);
            responseText = messageBuilder.buildAssembleText(new MessageContext(user), responseText);
            Bot.getInstance().execute(messageBuilder.createMessage(user, responseText));
        }
        return true;
    }

}
