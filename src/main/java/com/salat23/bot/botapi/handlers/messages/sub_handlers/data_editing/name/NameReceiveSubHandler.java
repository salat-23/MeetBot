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
public class NameReceiveSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public NameReceiveSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ENTERING_NAME);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        if (!isNameValid(message)) {

            String negativeResponse = messageBuilder
                    .buildAssembleText(new MessageContext(user), messageBuilder
                            .getMessageTextByType(ResponseTemplateTypes.NAME_WRONG));

            user.setState(UserState.ASK_NAME);
            userRepository.save(user);

            Bot.getInstance().execute(messageBuilder.createMessage(user, negativeResponse));
        } else {

            //Set the name and user state
            user.setName(message);
            user.setState(UserState.ASK_CONFIRMING_NAME);
            userRepository.save(user);
        }
        //Returning true because we need to switch the state
        return true;
    }

    private boolean isNameValid(String name) {
        return true;
    }
}
