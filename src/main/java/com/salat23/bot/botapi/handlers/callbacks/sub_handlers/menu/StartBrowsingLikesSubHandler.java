package com.salat23.bot.botapi.handlers.callbacks.sub_handlers.menu;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.callbacks.Callbacks;
import com.salat23.bot.botapi.handlers.callbacks.sub_handlers.ICallbackSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class StartBrowsingLikesSubHandler implements ICallbackSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public StartBrowsingLikesSubHandler(MessageBuilder messageBuilder, UserRepository userRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public Callbacks getSupportedCallback() {
        return Callbacks.BROWSE_LIKES;
    }

    @Override
    public List<UserState> getSupportedUserStates() {
        return List.of(UserState.IN_MENU);
    }

    @Override
    public boolean handle(User user) throws TelegramApiException {

        user.setState(UserState.LIKES_NEXT);
        userRepository.save(user);

        return true;
    }
}
