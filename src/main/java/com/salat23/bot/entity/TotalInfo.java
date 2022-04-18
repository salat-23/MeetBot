package com.salat23.bot.entity;

import lombok.Data;

@Data
public class TotalInfo {

    private Integer totalAmount;
    private Integer totalActiveAmount;
    private Integer totalMan;
    private Integer totalWoman;

    public TotalInfo(Integer totalAmount, Integer totalActiveAmount,
                     Integer totalMan, Integer totalWoman) {
        this.totalAmount = totalAmount;
        this.totalActiveAmount = totalActiveAmount;
        this.totalMan = totalMan;
        this.totalWoman = totalWoman;
    }


}
