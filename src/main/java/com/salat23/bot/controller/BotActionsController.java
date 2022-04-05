package com.salat23.bot.controller;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.backup.RestoreIdsFromTxt;
import com.salat23.bot.botapi.notifications.NotificationManager;
import com.salat23.bot.entity.BaseRequest;
import com.salat23.bot.entity.SimpleNotifyRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    public BotActionsController(RestoreIdsFromTxt idsFromTxtRestorer) {
        this.idsFromTxtRestorer = idsFromTxtRestorer;
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

    private boolean isCredentialsCorrect(BaseRequest request) {
        return (request.getLogin().equals(login) && request.getPassword().equals(password));
    }
}