package com.salat23.bot.botapi.handlers.messages.sub_handlers;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
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
public class MenuSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public MenuSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.IN_MENU);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        String menuResponse = messageBuilder
                .getMessageTextByType(ResponseTemplateTypes.MENU_OPTIONS_SUGGEST, user.getGender());
        menuResponse = messageBuilder.buildAssembleText(new MessageContext(user), menuResponse);
        SendMessage sendMessage = messageBuilder.createMessage(user, menuResponse);
        sendMessage.setReplyMarkup(messageBuilder.getMenuKeyboard());

        Bot.getInstance().execute(sendMessage);

        return false;
    }
}