package com.salat23.bot.botapi.notifications;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.models.GenderEnum;
import com.salat23.bot.models.Notification;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class NotificationManager {


    private static NotificationManager instance;
    private final UserRepository userRepository;
    private final MessageBuilder messageBuilder;

    public NotificationManager(UserRepository userRepository, MessageBuilder messageBuilder) {
        this.userRepository = userRepository;
        this.messageBuilder = messageBuilder;
        if (instance == null) {
            instance = this;
        }
    }

    public static NotificationManager getInstance() {
        return instance;
    }

    public void sendNotification(Notification notification) {
        try {
            if (notification.getType() == NotificationType.PROMO_MESSAGE) {
                SendMessage sendMessage = messageBuilder
                        .createMessage(notification.getRecipient(), notification.getText());
                sendMessage.enableMarkdown(true);
                Bot.getInstance().execute(sendMessage);
            }
            else if (isAcceptableState(notification.getRecipient().getState())) {
                SendMessage sendMessage = messageBuilder
                        .createMessage(notification.getRecipient(), notification.getText());
                if (notification.getType() == NotificationType.LIKE_RECEIVED)
                    sendMessage.setReplyMarkup(messageBuilder.getLikeReceivedKeyboard());
                sendMessage.enableMarkdown(true);
                Bot.getInstance().execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void notifyAll(List<UserState> acceptableStates, String text) {

        final int size = 100;

        for(int i = 0;;i++) {
            Pageable pageRequest = PageRequest.of(i, size);
            Page<User> users = userRepository.findAll(pageRequest);
            users.get().forEach(user -> {
                if (acceptableStates.contains(user.getState())) {
                    Notification notification =
                            new Notification(user,
                                    NotificationType.RESTORED_MESSAGE, text);
                    sendNotification(notification);
                }
            });
            if (users.getTotalElements() < size) break;
        }
    }

    public void notifyTarget(GenderEnum target, String message) {
        final int size = 100;

        for(int i = 0;;i++) {
            Pageable pageRequest = PageRequest.of(i, size);
            Page<User> users = userRepository.findAll(pageRequest);
            users.get().forEach(user -> {
                if (target == user.getGender()) {
                    Notification notification =
                            new Notification(user,
                                    NotificationType.PROMO_MESSAGE, message);
                    sendNotification(notification);
                }
            });
            if (users.getTotalElements() < size) break;
        }
    }


    private boolean isAcceptableState(UserState state) {
        switch (state) {
            case IN_MENU:
            case FORM_BROWSING:
            case FORM_NEXT:
            case FORM_CONTINUE:
            case RESTORED_USER:
                return true;

            default: return false;
        }
    }




}
