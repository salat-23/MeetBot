package com.salat23.bot.botapi.handlers.callbacks;

public enum Callbacks {

    UNKNOWN(""),
    BACK_TO_MENU("/back_to_menu"),
    ACCEPT("/accept"),
    DECLINE("/decline"),
    MALE("/male"),
    FEMALE("/female"),
    VIEW_MY_FORM("/view_my_form"),
    EDIT_MY_FORM("/edit_my_form"),
    BROWSE_FORMS("/browse_forms"),
    BROWSE_LIKES("/browse_likes")
    ;

    private final String callback;

    Callbacks(String callback) {
        this.callback = callback;
    }

    public String getCallback() {
        return callback;
    }
}
