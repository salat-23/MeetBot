package com.salat23.bot.botapi.form_searching;

import com.salat23.bot.models.User;
import com.salat23.bot.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class SimpleFormSearcher implements IFormSearcher {

    private final UserRepository userRepository;

    public SimpleFormSearcher(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getNextSuggestion(User searcher) {
        return userRepository.findForm(searcher.getId());
    }

    @Override
    public User getEarliestLiker(User searcher) {
        return userRepository.findEarliestLiker(searcher.getId());
    }

}
