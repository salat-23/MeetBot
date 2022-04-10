package com.salat23.bot.botapi.handlers.messages.sub_handlers.forms;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
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
public class ViewMyFormSubHandler implements IMessageSubHandler {

    private final UserRepository userRepository;
    private final MessageBuilder messageBuilder;

    public ViewMyFormSubHandler(UserRepository userRepository, MessageBuilder messageBuilder) {
        this.userRepository = userRepository;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.FORM_MY);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        user.setState(UserState.FORM_MY_VIEWING);
        userRepository.save(user);

        String myFormMessage = messageBuilder
                .getMessageTextByType(ResponseTemplateTypes.FORM_SUGGESTED, user.getGender());
        myFormMessage = messageBuilder
                .buildAssembleText(new MessageContext(user, user), myFormMessage);
        SendPhoto form = messageBuilder.createPhoto(user, myFormMessage);

        InputFile photo = new InputFile();
        photo.setMedia(new File(user.getPhoto()));
        File photoFile = Paths.get(user.getPhoto()).toFile();
        photo.setMedia(photoFile);
        form.setPhoto(photo);
        form.setReplyMarkup(messageBuilder.getMyFormKeyboard());

        Bot.getInstance().execute(form);
        return false;
    }
}
