package com.salat23.bot.botapi.handlers.messages.sub_handlers.data_editing.location;

import com.salat23.bot.botapi.Bot;
import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.handlers.messages.sub_handlers.IMessageSubHandler;
import com.salat23.bot.botapi.message_tools.MessageBuilder;
import com.salat23.bot.botapi.message_tools.MessageContext;
import com.salat23.bot.botapi.message_tools.ResponseTemplateTypes;
import com.salat23.bot.models.Location;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.LocationRepository;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class CityReceiveSubHandler implements IMessageSubHandler {

    private final MessageBuilder messageBuilder;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public CityReceiveSubHandler(MessageBuilder messageBuilder, UserRepository userRepository, LocationRepository locationRepository) {
        this.messageBuilder = messageBuilder;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<UserState> getSupportedStates() {
        return List.of(UserState.ENTERING_CITY);
    }

    @Override
    public boolean handle(User user, String message) throws TelegramApiException {

        if (message.equals("")) {

            user.setState(UserState.ASK_CITY);
            userRepository.save(user);
            String responseWrong = messageBuilder
                    .getMessageTextByType(ResponseTemplateTypes.CITY_WRONG, user.getGender());
            responseWrong = messageBuilder.buildAssembleText(new MessageContext(user), responseWrong);
            Bot.getInstance().execute(messageBuilder.createMessage(user, responseWrong));

        } else {
            //Creating new user location without latitude and longitude
            Location userLocation = new Location();
            userLocation.setCityName(message);
            locationRepository.save(userLocation);
            //Saving user
            user.setLocation(userLocation);
            user.setState(UserState.ASK_CONFIRMING_CITY);
            userRepository.save(user);
        }

        return true;
    }
}
