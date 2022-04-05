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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class MutualLikeSubHandler implements ICallbackSubHandler {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ViewRepository viewRepository;
    private final MessageBuilder messageBuilder;

    public MutualLikeSubHandler(MatchRepository matchRepository,
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
        return Callbacks.ACCEPT;
    }

    @Override
    public List<UserState> getSupportedUserStates() {
        return List.of(UserState.LIKES_BROWSING);
    }

    @Override
    public boolean handle(User user) throws TelegramApiException {

        User lastSuggested = user.getLastSuggestion();

        //Create and save match and view
        Match match = new Match(user, lastSuggested);
        View view = new View(user, lastSuggested);

        viewRepository.save(view);
        matchRepository.save(match);

        if (matchRepository.isLikedBy(user.getId(), lastSuggested.getId())) {
            //Then send notification to mutualed user
            String mutualNotification = messageBuilder
                    .getMessageTextByType(
                            ResponseTemplateTypes.NOTIFICATION_MUTUAL_RECEIVED,
                            lastSuggested.getGender());
            mutualNotification = messageBuilder
                    .buildAssembleText(new MessageContext(lastSuggested, user), mutualNotification);

            Notification notification =
                    new Notification(lastSuggested, NotificationType.MUTUAL_LIKE_RECEIVED, mutualNotification);
            NotificationManager.getInstance().sendNotification(notification);

            String mutualMessage = messageBuilder
                    .getMessageTextByType(
                            ResponseTemplateTypes.NOTIFICATION_MUTUAL_RECEIVED,
                            user.getGender());
            mutualMessage = messageBuilder
                    .buildAssembleText(new MessageContext(user, lastSuggested), mutualMessage);
            SendMessage sendMessage = messageBuilder.createMessage(user, mutualMessage);
            sendMessage.enableMarkdown(true);
            Bot.getInstance().execute(sendMessage);

            user.setState(UserState.LIKES_NEXT);
            userRepository.save(user);
            return true;
        }

        //TODO: Refactor this! Probably cant execute the code below anyway so you can get rid of it) Test first tho

        //Then send notification to liked user
        String notificationMessage = messageBuilder
                .getMessageTextByType(
                        ResponseTemplateTypes.NOTIFICATION_LIKED_RECEIVED,
                        lastSuggested.getGender());
        notificationMessage = messageBuilder
                .buildAssembleText(new MessageContext(lastSuggested), notificationMessage);

        Notification notification =
                new Notification(lastSuggested, NotificationType.LIKE_RECEIVED, notificationMessage);
        NotificationManager.getInstance().sendNotification(notification);

        user.setState(UserState.LIKES_NEXT);
        userRepository.save(user);

        return true;
    }
}
