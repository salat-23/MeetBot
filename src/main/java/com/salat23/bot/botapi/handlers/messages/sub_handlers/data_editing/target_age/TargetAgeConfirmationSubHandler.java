package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.target_age;

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
public class TargetAgeConfirmationSubHandler implements IMessageSubHandler {

    private final IConfirmationUtility confirmationUtility;
    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public TargetAgeConfirmationSubHandler(IConfirmationUtility confirmationUtility,
                                           MessageBuilder messageBuilder,
                                           UserRepository userRepository) {
        this.confirmationUtility = confirmationUtility;
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.CONFIRMING_TARGET_AGE);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {
        Boolean isAccept = confirmationUtility.doesConfirm(message);

        //Set user state to ask target and send post-conformational message
        if (isAccept) {
            user.setState(UserState.ASK_CITY);
            userRepository.save(user);

            //Create and send the message
            String postConfirmMessage = messageBuilder
                    .getMessageTextByType(ResponseTemplateTypes.TARGET_AGE_CONFIRM, user.getGender());
            postConfirmMessage = messageBuilder.buildAssembleText(new MessageContext(user), postConfirmMessage);
            Bot.getInstance().execute(messageBuilder.createMessage(user, postConfirmMessage));

            //After finishing re-process update
            return true;
        }

        user.setState(UserState.ASK_TARGET_AGE_MIN);
        userRepository.save(user);

        //Create and send the message
        String postConfirmMessage = messageBuilder
                .getMessageTextByType(ResponseTemplateTypes.TARGET_AGE_AFTER_DECLINE);
        postConfirmMessage = messageBuilder.buildAssembleText(new MessageContext(user), postConfirmMessage);
        Bot.getInstance().execute(messageBuilder.createMessage(user, postConfirmMessage));

        return true;
    }
}
