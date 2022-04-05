package com.salat23.bot.botapi.storage;

import com.salat23.bot.models.User;

import java.io.File;
import java.io.IOException;

public interface IFileStorage {

    String store(File file, User user) throws IOException;

}
