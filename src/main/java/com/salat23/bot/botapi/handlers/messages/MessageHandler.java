package com.salat23.bot.botapi.handlers.messages;

import com.salat23.bot.botapi.TelegramErrors;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.Handler;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.BoostRepository;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.List;
import java.util.Optional;

@Component
public class MessageHandler extends Handler {

    private final List<IMessageSubHandler> subHandlers;

    protected MessageHandler(UserRepository userRepository, BoostRepository boostRepository, List<IMessageSubHandler> subHandlers) {
        super(userRepository, boostRepository);
        this.subHandlers = subHandlers;
    }

    @Override
    public void handle(Update update) {

        //Handle the update if it has a message
        if (update.hasMessage() && update.getMessage() != null) {

            Message message = update.getMessage();
            String messageText = message.getText();

            //If user does not exist - create new entry in database and return it
            Long userId = message.getFrom().getId();
            boolean doReprocess = true;
            while (doReprocess) {
                User user = userRepository.findById(userId)
                        .orElseGet(() -> userNotFound(userId));

                //Getting the correct message handler
                IMessageSubHandler messageSubHandler = getCorrectHandler(user.getState());
                try {
                    doReprocess = (messageSubHandler.handle(user, messageText));
                    messageText = "";
                } catch (TelegramApiException e) {
                    if (e.getMessage().equals(TelegramErrors.BLOCKED_BY_USER.getError()))
                        freezeUser(user);
                }
            }
        }

        if (nextHandler != null) nextHandler.handle(update);
    }

    private IMessageSubHandler getCorrectHandler(UserState userState) {
        //We will get one of available sub-handlers for our user state.
        Optional<IMessageSubHandler> optionalIMessageSubHandler =
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