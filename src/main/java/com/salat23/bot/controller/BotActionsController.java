package com.salat23.bot.controller;

import com.salat23.bot.entity.BaseRequest;
import com.salat23.bot.entity.TotalInfo;
import com.salat23.bot.repository.MatchRepository;
import com.salat23.bot.repository.UserRepository;
import com.salat23.bot.repository.ViewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
public class BotActionsController {

    @Value("${bot.web.login}")
    private String login;
    @Value("${bot.web.password}")
    private String password;

    //private final RestoreIdsFromTxt idsFromTxtRestorer;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final ViewRepository viewRepository;

    public BotActionsController(
            //RestoreIdsFromTxt idsFromTxtRestorer,
            UserRepository userRepository, MatchRepository matchRepository, ViewRepository viewRepository) {
        //this.idsFromTxtRestorer = idsFromTxtRestorer;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.viewRepository = viewRepository;
    }

  /*  @PostMapping("/restore/ids")
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
    }*/

    @GetMapping("/info/total")
    public TotalInfo getBotInfo() {

        Integer totalAmount = userRepository.getTotalUsersCount();
        Integer totalActiveAmount = userRepository.getTotalActiveUsersCount();
        Integer totalMan = userRepository.getMaleUsersCount();
        Integer totalWoman = userRepository.getFemaleUsersCount();

        return new TotalInfo(totalAmount, totalActiveAmount, totalMan, totalWoman);
    }

    @GetMapping("/test")
    public String testHandle() {
        return "OK";
    }


}