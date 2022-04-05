package com.salat23.bot.botapi.handlers.messages.sub_handlers.likes;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.form_searching.IFormSearcher;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Component
public class NextLikeSubHandler implements IMessageSubHandler {

    private final IFormSearcher formSearcher;
    private final UserRepository userRepository;
    private final MessageBuilder messageBuilder;

    public NextLikeSubHandler(IFormSearcher formSearcher, UserRepository userRepository, MessageBuilder messageBuilder) {
        this.formSearcher = formSearcher;
        this.userRepository = userRepository;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.LIKES_NEXT);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        User suggestedLikerUser = formSearcher.getEarliestLiker(user);

        if (suggestedLikerUser == null) {
            user.setState(UserState.LIKES_MO_MORE);
            userRepository.save(user);
            return true;
        }

        user.setState(UserState.LIKES_BROWSING);
        user.setLastSuggestion(suggestedLikerUser);
        userRepository.save(user);

        String suggestionMessage = messageBuilder
                .getMessageTextByType(ResponseTemplateTypes.FORM_SUGGESTED, user.getGender());
        suggestionMessage = messageBuilder
                .buildAssembleText(new MessageContext(user, suggestedLikerUser), suggestionMessage);
        SendPhoto form = messageBuilder.createPhoto(user, suggestionMessage);

        InputFile photo = new InputFile();
        photo.setMedia(new File(suggestedLikerUser.getPhoto()));
        File photoFile = Paths.get(suggestedLikerUser.getPhoto()).toFile();
        photo.setMedia(photoFile);
        form.setPhoto(photo);
        form.setReplyMarkup(messageBuilder.getFormKeyboard());

        Bot.getInstance().execute(form);

        return false;
    }
}
