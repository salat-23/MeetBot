package com.salat23.bot.botapi.handlers.images.sub_handlers;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.storage.IFileStorage;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Component
public class AvatarUploadSubHandler implements IImageSubHandler {

    private final IFileStorage fileStorage;
    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;

    public AvatarUploadSubHandler(IFileStorage fileStorage, MessageBuilder messageBuilder, UserRepository userRepository) {
        this.fileStorage = fileStorage;
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ENTERING_PHOTO);
    }

    @Override
    public boolean handle(User user, List<PhotoSize> photos) throws TelegramApiException {

        PhotoSize photo = photos.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);

        assert photo != null;
        GetFile getFileRequest = GetFile.builder()
                .fileId(photo.getFileId())
                .build();
        File file = Bot.getInstance().execute(getFileRequest);

        try {
            String path = fileStorage.store(Bot.getInstance().downloadFile(file), user);
            user.setPhoto(path);
            user.setState(UserState.ASK_CONFIRMING_PHOTO);
            userRepository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }
}
