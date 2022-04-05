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
public class GenderAskSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public GenderAskSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ASK_GENDER);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        user.setState(UserState.ENTERING_GENDER);
        userRepository.save(user);

        //Ask the user about his gender
        String askGenderText = messageBuilder.getMessageTextByType(ResponseTemplateTypes.GENDER_ENTER_ASK);
        askGenderText = messageBuilder.buildAssembleText(new MessageContext(user), askGenderText);
        SendMessage sendMessage = messageBuilder.createMessage(user, askGenderText);
        sendMessage.setReplyMarkup(messageBuilder.getGenderKeyboard());

        Bot.getInstance().execute(sendMessage);

        return false;
    }
}
