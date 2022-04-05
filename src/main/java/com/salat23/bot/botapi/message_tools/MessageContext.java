package com.salat23.bot.botapi.message_tools;

import com.salat23.bot.models.GenderEnum;
import com.salat23.bot.models.Location;
import com.salat23.bot.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageContext {

    private User user;
    private User oppositeUser;

    public MessageContext(User user) {
        this.user = user;
        this.oppositeUser = new User();
        //Create empty user fields
        Location userLocation = new Location();
        userLocation.setCityName("");
        oppositeUser.setLocation(userLocation);
        oppositeUser.setName("");
        oppositeUser.setBio("");
        oppositeUser.setGender(GenderEnum.MAN);
        oppositeUser.setAge(0);
    }

    public MessageContext(User user, User oppositeUser) {
        this.user = user;
        this.oppositeUser = oppositeUser;
    }


}
