package com.salat23.bot.entity;

import lombok.Data;

@Data
public class TotalInfo {

    private Integer totalAmount;
    private Integer totalActiveAmount;
    private Integer totalMan;
    private Integer totalWoman;
    private Integer latestDayRegistrationMale;
    private Integer latestDayRegistrationFemale;

    public TotalInfo(Integer totalAmount, Integer totalActiveAmount,
                     Integer totalMan, Integer totalWoman, Integer regMale, Integer regFemale) {
        this.totalAmount = totalAmount;
        this.totalActiveAmount = totalActiveAmount;
        this.totalMan = totalMan;
        this.totalWoman = totalWoman;
        this.latestDayRegistrationMale = regMale;
        this.latestDayRegistrationFemale = regFemale;
    }


}
