package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.age;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.botapi.utility.AgeConfirmer;
import com.salat23.bot.botapi.utility.AgeStatus;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class AgeReceiveSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;
    private final AgeConfirmer ageConfirmer;

    public AgeReceiveSubHandler(MessageBuilder messageBuilder, UserRepository userRepository, AgeConfirmer ageConfirmer) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
        this.ageConfirmer = ageConfirmer;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ENTERING_AGE);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        AgeStatus ageStatus = ageConfirmer.confirmAge(message);

        switch (ageStatus) {
            case WRONG_FORMAT: {
                user.setState(UserState.ASK_AGE);
                userRepository.save(user);

                /*String responseText = messageBuilder.getMessageTextByType(ResponseTemplateTypes.AGE_WRONG,
                        user.getGender());
                responseText = messageBuilder.buildAssembleText(new MessageContext(user), responseText);
                Bot.getInstance().execute(messageBuilder.createMessage(user, responseText));*/
                break;
            }
            case TOO_HIGH: {
                user.setState(UserState.ASK_AGE);
                userRepository.save(user);

                String ageTooLow = messageBuilder
                        .getMessageTextByType(ResponseTemplateTypes.AGE_TOO_HIGH, user.getGender());
                ageTooLow = messageBuilder.buildAssembleText(new MessageContext(user), ageTooLow);
                Bot.getInstance().execute(messageBuilder.createMessage(user, ageTooLow));
                break;
            }
            case TOO_LOW: {
                user.setState(UserState.ASK_AGE);
                userRepository.save(user);

                String ageTooLow = messageBuilder
                        .getMessageTextByType(ResponseTemplateTypes.AGE_TOO_LOW, user.getGender());
                ageTooLow = messageBuilder.buildAssembleText(new MessageContext(user), ageTooLow);
                Bot.getInstance().execute(messageBuilder.createMessage(user, ageTooLow));
                break;
            }
            case OK: {
                user.setAge(ageConfirmer.getLastCheckedAge());
                user.setState(UserState.ASK_CONFIRMING_AGE);
                userRepository.save(user);

                break;
            }

        }

        return true;
    }
}
