package com.salat23.bot.botapi.backup;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.models.Boost;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.BoostRepository;
import com.salat23.bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class RestoreIdsFromTxt implements IBackupExecutor {

    private final UserRepository userRepository;
    private final BoostRepository boostRepository;

    @Value("${bot.backup.idtxtpath}")
    private String filePath;

    public RestoreIdsFromTxt(UserRepository userRepository, BoostRepository boostRepository) {
        this.userRepository = userRepository;
        this.boostRepository = boostRepository;
    }

    @Override
    public void restore() {

        try {
            FileReader input = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(input);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                Long userId = Long.parseLong(line);
                if (userRepository.existsById(userId)) continue;

                User newUser = new User();
                newUser.setId(userId);
                Boost boost = new Boost();
                boost.setPopularityBoost(0f);
                boostRepository.save(boost);
                newUser.setBoost(boost);
                newUser.setState(UserState.RESTORED_USER);
                userRepository.save(newUser);
            }

        } catch (IOException e) {}
    }
}
