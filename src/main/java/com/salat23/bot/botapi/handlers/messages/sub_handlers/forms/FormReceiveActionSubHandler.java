package com.salat23.bot.botapi.handlers.messages.sub_handlers.forms;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
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
public class FormReceiveActionSubHandler implements IMessageSubHandler {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ViewRepository viewRepository;
    private final MessageBuilder messageBuilder;

    public FormReceiveActionSubHandler(MatchRepository matchRepository,
                                       ViewRepository viewRepository,
                                       MessageBuilder messageBuilder,
                                       UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.viewRepository = viewRepository;
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.FORM_BROWSING, UserState.LIKES_BROWSING, UserState.FORM_MY_VIEWING);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        if (user.getState() == UserState.FORM_BROWSING) {
            //Like
            if (message.equals("\uD83D\uDC4D")) {
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

                    user.setState(UserState.FORM_NEXT);
                    userRepository.save(user);
                    return true;
                }

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

                user.setState(UserState.FORM_NEXT);
                userRepository.save(user);

                return true;
            }

            //Dislike
            if (message.equals("\uD83D\uDC4E")) {
                User lastSuggested = user.getLastSuggestion();

                //Create and save view
                View view = new View(user, lastSuggested);

                viewRepository.save(view);

                user.setState(UserState.FORM_NEXT);
                userRepository.save(user);

                return true;
            }
        }

        if (user.getState() == UserState.LIKES_BROWSING) {
            //Like
            if (message.equals("\uD83D\uDC4D")) {
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

            //Dislike
            if (message.equals("\uD83D\uDC4E")) {
                User lastSuggested = user.getLastSuggestion();

                //Create and save view
                View view = new View(user, lastSuggested);
                viewRepository.save(view);

                user.setState(UserState.LIKES_NEXT);
                userRepository.save(user);

                return true;
            }
        }

        if (message.equals("Назад в меню")) {
            user.setState(UserState.IN_MENU);
            userRepository.save(user);

            return true;
        }

        return false;
    }

}
