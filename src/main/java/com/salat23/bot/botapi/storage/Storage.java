package com.salat23.bot.botapi.storage;

import com.salat23.bot.models.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Component
public class Storage implements IFileStorage {

    @Value("${bot.storage.user_path}")
    private String defaultUserFilesLocation;

    private final String USER_IMAGES = "images/";

    private final String USER_IMAGES_LOCATION_TEMPLATE = "%s%s/%s%s.png";

    @Override
    public String store(File file, User user) throws IOException {

        String photoPath = String.format(USER_IMAGES_LOCATION_TEMPLATE,
                defaultUserFilesLocation,
                user.getId(),
                USER_IMAGES,
                Instant.now().toEpochMilli());

        FileUtils.copyFile(file, new File(photoPath));
        return photoPath;
    }

}
