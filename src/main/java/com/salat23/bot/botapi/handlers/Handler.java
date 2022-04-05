package com.salat23.bot.botapi.handlers;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.models.Boost;
import com.salat23.bot.models.User;
import com.salat23.bot.repository.BoostRepository;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class Handler implements IHandler {

    protected final UserRepository userRepository;
    protected IHandler nextHandler;

    private final BoostRepository boostRepository;

    protected Handler(UserRepository userRepository, BoostRepository boostRepository) {
        this.userRepository = userRepository;
        this.boostRepository = boostRepository;
    }

    @Override
    public IHandler setNext(IHandler handler) {
        this.nextHandler = handler;
        return nextHandler;
    }

    protected User userNotFound(Long userId) {
        //Create new user with its chat id
        User user = new User();
        user.setId(userId);
        user.setState(UserState.NEW_USER);
        user.setIsSearchable(false);
        Boost boost = new Boost();
        boost.setPopularityBoost(0f);
        user.setBoost(boost);
        boostRepository.save(boost);
        return userRepository.save(user);
    }

    protected void freezeUser(User user) {
        user.setState(UserState.FROZEN_USER);
        userRepository.save(user);
    }
}
