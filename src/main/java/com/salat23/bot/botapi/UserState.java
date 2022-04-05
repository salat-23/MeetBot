package com.salat23.bot.botapi;

public enum UserState {
    NEW_USER,
    RESTORED_USER,
    FROZEN_USER,
    ASK_NAME,
    ENTERING_NAME,
    ASK_CONFIRMING_NAME,
    CONFIRMING_NAME,
    ASK_GENDER,
    ENTERING_GENDER,
    ASK_CONFIRMING_GENDER,
    CONFIRMING_GENDER,
    ASK_CITY,
    ENTERING_CITY,
    ASK_CONFIRMING_CITY,
    CONFIRMING_CITY,
    ASK_AGE,
    ENTERING_AGE,
    ASK_CONFIRMING_AGE,
    CONFIRMING_AGE,
    ASK_TARGET_AGE_MIN,
    ASK_TARGET_AGE_MAX,
    ENTERING_TARGET_AGE_MIN,
    ENTERING_TARGET_AGE_MAX,
    ASK_CONFIRMING_TARGET_AGE,
    CONFIRMING_TARGET_AGE,
    ASK_BIO,
    ENTERING_BIO,
    ASK_CONFIRMING_BIO,
    CONFIRMING_BIO,
    ASK_PHOTO,
    ENTERING_PHOTO,
    ASK_CONFIRMING_PHOTO,
    CONFIRMING_PHOTO,
    COMPLETE_REGISTRATION,
    START_REGISTRATION,
    IN_MENU,
    FORM_MY,
    FORM_NEXT,
    FORM_CONTINUE,
    FORM_BROWSING,
    FORM_NO_MORE,
    LIKES_NEXT,
    LIKES_BROWSING,
    LIKES_MO_MORE
}