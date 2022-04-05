package com.salat23.bot.botapi.handlers.locations;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.geolocation.IGeolocationProvider;
import com.salat23.bot.botapi.handlers.Handler;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.BoostRepository;
import com.salat23.bot.repository.LocationRepository;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@Component
public class LocationHandler extends Handler {

    private final IGeolocationProvider geolocationProvider;
    private final LocationRepository locationRepository;

    protected LocationHandler(UserRepository userRepository, BoostRepository boostRepository, IGeolocationProvider geolocationProvider, LocationRepository locationRepository) {
        super(userRepository, boostRepository);
        this.geolocationProvider = geolocationProvider;
        this.locationRepository = locationRepository;
    }

    @Override
    public void handle(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            //If user does not exist - create new entry in database and return it
            Long userId = message.getFrom().getId();
            User user = userRepository.findById(userId)
                    .orElseGet(() -> userNotFound(userId));

            if (message.hasLocation()) {
                if (user.getState() != UserState.ENTERING_CITY) return;

                Location location = message.getLocation();
                try {
                    com.salat23.bot.models.Location userLocation = geolocationProvider.locate(
                            String.valueOf(location.getLongitude()),
                            String.valueOf(location.getLatitude()));
                    if (userLocation != null) {
                        locationRepository.save(userLocation);
                        user.setLocation(userLocation);
                        user.setState(UserState.ASK_CONFIRMING_CITY);
                        userRepository.save(user);
                        message.setText("");
                        update.setMessage(message);
                    }
                } catch (IOException e) {
                    message.setText("");
                    update.setMessage(message);
                    e.printStackTrace();
                }
            }
        }
        nextHandler.handle(update);
    }
}