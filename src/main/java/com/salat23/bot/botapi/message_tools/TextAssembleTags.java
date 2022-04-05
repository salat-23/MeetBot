package com.salat23.bot.botapi.message_tools;

public enum TextAssembleTags {
    USER_NAME_TAG("%name%"),
    USER_AGE_TAG("%age%"),
    USER_BIO_TAG("%bio%"),
    USER_CITY_TAG("%city%"),
    USER_GENDER_TAG("%gender%"),
    USER_TARGET_AGE_MAX("%target_max%"),
    USER_TARGET_AGE_MIN("%target_min%"),
    OPPOSITE_USER_NAME_TAG("%o_name%"),
    OPPOSITE_USER_AGE_TAG("%o_age%"),
    OPPOSITE_USER_BIO_TAG("%o_bio%"),
    OPPOSITE_USER_CITY_TAG("%o_city%"),
    OPPOSITE_USER_GENDER_TAG("%o_gender%"),
    OPPOSITE_USER_LINK("%o_link%");

    private final String tag;

    TextAssembleTags(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
