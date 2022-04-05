package com.salat23.bot.config;

import com.salat23.bot.botapi.handlers.callbacks.CallbackHandler;
import com.salat23.bot.botapi.handlers.EntryHandler;
import com.salat23.bot.botapi.handlers.images.ImageHandler;
import com.salat23.bot.botapi.handlers.locations.LocationHandler;
import com.salat23.bot.botapi.handlers.messages.MessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Component
public class Beans {

    private final EntryHandler entryHandler;
    private final CallbackHandler callbackHandler;
    private final ImageHandler imageHandler;
    private final LocationHandler locationHandler;
    private final MessageHandler messageHandler;

    @Value("${bot.proxy.host}")
    private String proxyHost;
    @Value("${bot.proxy.port}")
    private String proxyPort;
    @Value("${bot.proxy.login}")
    private String proxyLogin;
    @Value("${bot.proxy.password}")
    private String proxyPassword;

    public Beans(EntryHandler entryHandler,
                 CallbackHandler callbackHandler,
                 ImageHandler imageHandler,
                 LocationHandler locationHandler,
                 MessageHandler messageHandler) {
        this.entryHandler = entryHandler;
        this.callbackHandler = callbackHandler;
        this.imageHandler = imageHandler;
        this.locationHandler = locationHandler;
        this.messageHandler = messageHandler;
    }


    @Bean
    public EntryHandler entryHandlerSetup() {
        entryHandler
                .setNext(callbackHandler)
                .setNext(locationHandler)
                .setNext(imageHandler)
                .setNext(messageHandler);

        return entryHandler;
    }

    //Proxy bean
    @Bean
    public DefaultBotOptions botOptions() {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(proxyLogin, proxyPassword.toCharArray());
            }
        });
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost(proxyHost);
        botOptions.setProxyPort(Integer.parseInt(proxyPort));
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        return botOptions;
    }

}
