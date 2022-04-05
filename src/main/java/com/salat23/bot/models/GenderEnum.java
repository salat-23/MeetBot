package com.salat23.bot.models;

public enum GenderEnum {
    MAN("М"),
    WOMAN("Ж");

    GenderEnum(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    private final String shortName;
}
