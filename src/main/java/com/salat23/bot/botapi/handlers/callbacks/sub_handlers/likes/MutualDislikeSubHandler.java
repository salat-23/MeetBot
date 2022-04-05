package com.salat23.bot.botapi.handlers.callbacks.sub_handlers.likes;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.callbacks.Callbacks;
import com.salat23.bot.botapi.handlers.callbacks.sub_handlers.ICallbackSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.botapi.notifications.NotificationManager;
import com.salat23.bot.botapi.notifications.NotificationType;
import com.salat23.bot.models.Match;
import com.salat23.bot.models.Notification;
import com.salat23.bot.models.User;
import com.salat23.bot.models.View;
import com.salat23.bot.repository.MatchRepository;
import com.salat23.bot.repository.UserRepository;
import com.salat23.bot.repository.ViewRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class MutualDislikeSubHandler implements ICallbackSubHandler {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ViewRepository viewRepository;
    private final MessageBuilder messageBuilder;

    public MutualDislikeSubHandler(MatchRepository matchRepository,
                                   UserRepository userRepository,
                                   ViewRepository viewRepository,
                                   MessageBuilder messageBuilder) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.viewRepository = viewRepository;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public Callbacks getSupportedCallback() {
        return Callbacks.DECLINE;
    }

    @Override
    public List<UserState> getSupportedUserStates() {
        return List.of(UserState.LIKES_BROWSING);
    }

    @Override
    public boolean handle(User user) throws TelegramApiException {

        User lastSuggested = user.getLastSuggestion();

        //Create and save view
        View view = new View(user, lastSuggested);
        viewRepository.save(view);

        user.setState(UserState.LIKES_NEXT);
        userRepository.save(user);

        return true;
    }
}
