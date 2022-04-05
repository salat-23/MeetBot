package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.gender;

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
public class GenderAskConfirmationSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public GenderAskConfirmationSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ASK_CONFIRMING_GENDER);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        user.setState(UserState.CONFIRMING_GENDER);
        userRepository.save(user);

        //Send the message asking user to confirm its gender
        String confirmMessage = messageBuilder.getMessageTextByType(ResponseTemplateTypes.GENDER_ENTER_CONFIRM, user.getGender());
        confirmMessage = messageBuilder.buildAssembleText(new MessageContext(user), confirmMessage);
        SendMessage confirmSendMessage = messageBuilder.createMessage(user, confirmMessage);
        //Set the reply keyboard to YES\NO
        confirmSendMessage.setReplyMarkup(messageBuilder.getConfirmKeyboard());

        Bot.getInstance().execute(confirmSendMessage);

        return false;
    }
}
