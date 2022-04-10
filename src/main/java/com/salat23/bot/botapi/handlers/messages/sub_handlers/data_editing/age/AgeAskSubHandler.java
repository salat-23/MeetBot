package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.age;

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
public class AgeAskSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public AgeAskSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ASK_AGE);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        user.setState(UserState.ENTERING_AGE);
        userRepository.save(user);

        String ageString = messageBuilder.getMessageTextByType(ResponseTemplateTypes.AGE_ENTER_ASK, user.getGender());
        ageString = messageBuilder.buildAssembleText(new MessageContext(user), ageString);
        SendMessage sendMessage = messageBuilder.createMessage(user, ageString);

        if (user.getAge() != null)
            sendMessage.setReplyMarkup(messageBuilder.getPreviousOptionKeyboard(user.getAge().toString()));
        Bot.getInstance().execute(sendMessage);

        return false;
    }
}
