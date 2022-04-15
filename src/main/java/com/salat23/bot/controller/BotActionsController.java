package com.salat23.bot.controller;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.backup.RestoreIdsFromTxt;
import com.salat23.bot.botapi.notifications.NotificationManager;
import com.salat23.bot.entity.BaseRequest;
import com.salat23.bot.entity.SimpleNotifyRequest;
import com.salat23.bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BotActionsController {

    @Value("${bot.web.login}")
    private String login;
    @Value("${bot.web.password}")
    private String password;

    private final RestoreIdsFromTxt idsFromTxtRestorer;
    private final UserRepository userRepository;

    public BotActionsController(RestoreIdsFromTxt idsFromTxtRestorer, UserRepository userRepository) {
        this.idsFromTxtRestorer = idsFromTxtRestorer;
        this.userRepository = userRepository;
    }

    @PostMapping("/restore/ids")
    public ResponseEntity<?> restoreIds(@RequestBody BaseRequest request) {
        if (!isCredentialsCorrect(request)) return ResponseEntity.badRequest().build();
        idsFromTxtRestorer.restore();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notify/restored")
    public ResponseEntity<?> restoreIds(@RequestBody SimpleNotifyRequest request) {
        if (!isCredentialsCorrect(request)) return ResponseEntity.badRequest().build();
        NotificationManager.getInstance().notifyAll(List.of(UserState.RESTORED_USER), request.getMessage());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info/total")
    public String getBotInfo(@RequestBody BaseRequest request) {
        if (!isCredentialsCorrect(request)) return "Not authorized";

        Integer totalAmount = userRepository.getTotalUsersCount();
        Integer totalActiveAmount = userRepository.getTotalActiveUsersCount();
        Integer totalMan = userRepository.getMaleUsersCount();
        Integer totalWoman = userRepository.getFemaleUsersCount();

        String response = String.format("Всего пользователей: %s\nВсего активных: %s\nВсего Муж/Жен: %s  -  %s",
                totalAmount, totalActiveAmount, totalMan, totalWoman);
        return response;
    }

    private boolean isCredentialsCorrect(BaseRequest request) {
        return (request.getLogin().equals(login) && request.getPassword().equals(password));
    }

}