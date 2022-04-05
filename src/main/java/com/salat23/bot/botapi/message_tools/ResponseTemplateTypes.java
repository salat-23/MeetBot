package com.salat23.bot.botapi.message_tools;

public enum ResponseTemplateTypes {
    UNKNOWN_COMMAND("unknown"),

    WELCOME_TEXT("welcome"),

    NAME_ENTER_ASK("nameAsk"),
    NAME_ENTER_CONFIRM("nameConfirm"),
    NAME_ENTER_AFTER_CONFIRM("nameAfter"),
    NAME_ENTER_AFTER_DECLINE("nameDecline"),
    NAME_WRONG("nameWrong"),

    GENDER_ENTER_ASK("genderAsk"),
    GENDER_ENTER_CONFIRM("genderConfirm"),
    GENDER_ENTER_AFTER_CONFIRM("genderAfter"),
    GENDER_ENTER_AFTER_DECLINE("genderDecline"),

    TARGET_GENDER_ENTER_ASK("targetAsk"),
    TARGET_GENDER_ENTER_CONFIRM("targetConfirm"),
    TARGET_GENDER_ENTER_AFTER_CONFIRM("targetAfter"),
    TARGET_GENDER_ENTER_AFTER_DECLINE("targetDecline"),

    CITY_ENTER_ASK("cityAsk"),
    CITY_ENTER_CONFIRM("cityConfirm"),
    CITY_ENTER_AFTER_CONFIRM("cityAfter"),
    CITY_ENTER_AFTER_DECLINE("cityDecline"),
    CITY_WRONG("cityWrong"),

    AGE_ENTER_ASK("ageAsk"),
    AGE_ENTER_CONFIRM("ageConfirm"),
    AGE_ENTER_AFTER_CONFIRM("ageAfter"),
    AGE_ENTER_AFTER_DECLINE("ageDecline"),
    AGE_TOO_LOW("ageTooLow"),
    AGE_TOO_HIGH("ageTooHigh"),
    AGE_WRONG("ageWrong"),

    TARGET_AGE_MIN_ASK("targetMinAgeAsk"),
    TARGET_AGE_MAX_ASK("targetMaxAgeAsk"),
    TARGET_AGE_CONFIRM("targetAgeConfirm"),
    TARGET_AGE_AFTER_CONFIRM("targetAgeAfter"),
    TARGET_AGE_AFTER_DECLINE("targetAgeDecline"),
    TARGET_AGE_TOO_LOW("targetAgeTooLow"),
    TARGET_AGE_TOO_HIGH("targetAgeTooHigh"),
    TARGET_AGE_WRONG("targetAgeWrong"),

    BIO_ENTER_ASK("bioAsk"),
    BIO_ENTER_CONFIRM("bioConfirm"),
    BIO_ENTER_AFTER_CONFIRM("bioAfter"),
    BIO_ENTER_AFTER_DECLINE("bioDecline"),
    BIO_WRONG("bioWrong"),

    PHOTO_ENTER_ASK("photoAsk"),
    PHOTO_ENTER_CONFIRM("photoConfirm"),
    PHOTO_ENTER_AFTER_CONFIRM("photoAfter"),
    PHOTO_ENTER_AFTER_DECLINE("photoDecline"),

    MENU_OPTIONS_SUGGEST("menuOptions"),

    FORM_SUGGESTED("formSuggest"),
    FORM_SELF("formSelf"),

    NOTIFICATION_LIKED_RECEIVED("notificationLiked"),
    NOTIFICATION_MUTUAL_RECEIVED("notificationMutual"),

    FORMS_ENDED("noForms"),

    LIKES_ENDED("noLikes");



    private final String shortName;

    public String getShortName() {
        return shortName;
    }

    ResponseTemplateTypes(String shortName) {
        this.shortName = shortName;
    }

}
