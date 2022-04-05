package com.salat23.bot.botapi.handlers.images;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.Handler;
import com.salat23.bot.botapi.handlers.images.sub_handlers.IImageSubHandler;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.BoostRepository;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Component
public class ImageHandler extends Handler {

    private final List<IImageSubHandler> subHandlers;

    protected ImageHandler(UserRepository userRepository, BoostRepository boostRepository, List<IImageSubHandler> subHandlers) {
        super(userRepository, boostRepository);
        this.subHandlers = subHandlers;
    }

    @Override
    public void handle(Update update) {

        //Handle the update if it has a message
        if (update.hasMessage() && update.getMessage().getPhoto() != null) {

            //Will be used to imitate user message for multiple times processing
            boolean imitateMessage;

            List<PhotoSize> photos = update.getMessage().getPhoto();

            //If user does not exist - create new entry in database and return it
            Long userId = update.getMessage().getFrom().getId();

            User user = userRepository.findById(userId)
                    .orElseGet(() -> userNotFound(userId));

            //Getting the correct message handler
            IImageSubHandler imageSubHandler = getCorrectHandler(user.getState());


            try {
                imitateMessage = (imageSubHandler.handle(user, photos));
                if (imitateMessage) {
                    if (nextHandler != null) nextHandler.handle(imitateMessage(user, update));
                    return;
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

        if (nextHandler != null) nextHandler.handle(update);
    }

    private Update imitateMessage(User user, Update update) {
        Message message = new Message();
        org.telegram.telegrambots.meta.api.objects.User telegramUser = new org.telegram.telegrambots.meta.api.objects.User();
        telegramUser.setId(user.getId());
        message.setFrom(telegramUser);
        message.setText("");
        update.setMessage(message);
        return update;
    }

    private IImageSubHandler getCorrectHandler(UserState userState) {
        //We will get one of available sub-handlers for our user state.
        Optional<IImageSubHandler> optionalIMessageSubHandler =
                subHandlers
                        .stream()
                        //Filter only supported handlers.
                        .filter(iCallbackHandler ->
                                iCallbackHandler
                                        .getSupportedStates()
                                        .contains(userState))
                        //Make sure every message handler has its unique user state
                        .findAny();
        //Otherwise, return the unknown callback handler.
        return optionalIMessageSubHandler.orElseThrow(
                () -> new RuntimeException(
                        String.format("Could not found correct handler for state: %s", userState.toString())));
    }


}
