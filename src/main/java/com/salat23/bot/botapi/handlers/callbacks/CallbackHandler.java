package com.salat23.bot.botapi.handlers.callbacks;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.Handler;
import com.salat23.bot.botapi.handlers.callbacks.sub_handlers.ICallbackSubHandler;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.BoostRepository;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Component
public class CallbackHandler extends Handler {

    private final List<ICallbackSubHandler> callbackHandlers;

    public CallbackHandler(UserRepository userRepository, BoostRepository boostRepository, List<ICallbackSubHandler> callbackHandlers) {
        super(userRepository, boostRepository);
        this.callbackHandlers = callbackHandlers;
    }

    @Override
    public void handle(Update update) {

        //Will be used to imitate user message for multiple times processing
        boolean imitateMessage = false;

        //Handle the callback query if exists
        if (update.hasCallbackQuery()) {
            String callback = update.getCallbackQuery().getData();
            Long userId = update.getCallbackQuery().getFrom().getId();

            //If user does not exist - create new entry in database and return it
            User user = userRepository.findById(userId)
                    .orElseGet(() -> userNotFound(userId));

            /*
            Get the correct sub-handler for your callback and handle it
            You don't have to pass the callback itself, because each handler handles only one type
            of callback, so it will do just the thing associated with its command
            */
            ICallbackSubHandler callbackHandler = getCorrectHandler(callback, user.getState());
            AnswerCallbackQuery answerCallback = new AnswerCallbackQuery();
            answerCallback.setCallbackQueryId(update.getCallbackQuery().getId());
            try {
                imitateMessage = callbackHandler.handle(user);
                Bot.getInstance().execute(answerCallback);
                //If needed reprocessing
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

    private ICallbackSubHandler getCorrectHandler(String callback, UserState userState) {
        //We will get one of available sub-handlers for our callback.
        Optional<ICallbackSubHandler> optionalICallbackHandler =
                callbackHandlers
                        .stream()
                        //Filter only supported handlers.
                        .filter(iCallbackHandler ->
                                //Has the right type
                                iCallbackHandler
                                        .getSupportedCallback()
                                        .getCallback().equals(callback)
                                //And operates the right user state
                                && iCallbackHandler
                                        .getSupportedUserStates()
                                        .contains(userState))
                        /*
                        And get any of the present handlers.
                        You will have to make sure that you only use
                        one type of callback type for each sub-handler!!!
                          */
                        .findAny();
        //Otherwise, return the unknown callback handler with unregistered user state.
        return optionalICallbackHandler
                .orElseGet(() -> getCorrectHandler(Callbacks.UNKNOWN.getCallback(), UserState.NEW_USER));
    }

}
