package com.salat23.bot.botapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.BotOptions;

@Component
public class Bot extends TelegramLongPollingBot {

    private final UpdateReceiver updateReceiver;
    private static Bot instance;

    @Value("${bot.token}")
    private String token;
    @Value("${bot.name}")
    private String botName;

    public Bot(UpdateReceiver updateReceiver, DefaultBotOptions botOptions) {
        super(botOptions);
        this.updateReceiver = updateReceiver;
        if (instance == null) instance = this;
    }

    public static Bot getInstance() {
        return instance;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        new Thread(() -> updateReceiver.handle(update)).start();
    }
}
