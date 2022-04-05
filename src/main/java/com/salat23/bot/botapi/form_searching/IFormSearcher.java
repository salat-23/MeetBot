package com.salat23.bot.botapi.form_searching;

import com.salat23.bot.models.User;

public interface IFormSearcher {

    User getNextSuggestion(User searcher);

    User getEarliestLiker(User searcher);

}
