package com.salat23.bot.entity;

import com.salat23.bot.models.GenderEnum;
import lombok.Data;

@Data
public class NotificationRequest {

    private String text;
    private String target;

}
