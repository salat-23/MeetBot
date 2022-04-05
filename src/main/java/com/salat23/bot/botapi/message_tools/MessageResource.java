package com.salat23.bot.botapi.message_tools;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResource {

    private ResponseTemplateTypes type;

    private String textMale;
    private String textFemale;
    private String textBoth;


    public MessageResource() {}

    public MessageResource(ResponseTemplateTypes type, String textBoth, String textMale, String textFemale) {
        this.type = type;
        this.textBoth = textBoth;
        this.textMale = textMale;
        this.textFemale = textFemale;
    }

}
