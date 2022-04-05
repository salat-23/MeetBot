package com.salat23.bot.botapi.utility;

import org.springframework.stereotype.Component;

@Component
public class AgeConfirmer {

    private final Integer MAX_AGE = 35;
    private final Integer MIN_AGE = 12;
    private Integer lastCheckedAge;

    public AgeStatus confirmAge(String ageString) {
        //if (!containsOnlyDigits(ageString)) return AgeStatus.WRONG_FORMAT;
        int age;
        try {
            age = Integer.parseInt(ageString);
        } catch (NumberFormatException e) {
            return AgeStatus.WRONG_FORMAT;
        }
        if (age > MAX_AGE) return AgeStatus.TOO_HIGH;
        if (age < MIN_AGE) return AgeStatus.TOO_LOW;
        lastCheckedAge = age;
        return AgeStatus.OK;
    }

    public Integer getLastCheckedAge() {
        return lastCheckedAge;
    }

    private boolean containsOnlyDigits(String text) {
        return (text.replaceAll("[0-9]+", "").length() > 0);
    }


}
