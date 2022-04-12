package com.salat23.bot.botapi.message_tools;

import com.salat23.bot.botapi.handlers.callbacks.Callbacks;
import com.salat23.bot.models.GenderEnum;
import com.salat23.bot.models.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.salat23.bot.botapi.message_tools.TextAssembleTags.*;

@Component
public class MessageBuilder {


    private final IResponseResourceProvider provider;
    private final String CONFIRM_KEYBOARD_ACCEPT = "Да";
    private final String CONFIRM_KEYBOARD_DECLINE = "Нет";
    private final String LOCATION_KEYBOARD_REQUEST = "Определить мой город";

    private final String LIKE_TEXT = "\uD83D\uDC4D";
    private final String DISLIKE_TEXT = "\uD83D\uDC4E";
    private final String MENU_TEXT = "Назад в меню";

    private final String SEE_LIKES_TEXT = "Посмотреть";

    private final String MALE_BUTTON_TEXT = "Парень";
    private final String FEMALE_BUTTON_TEXT = "Девушка";

    private final String VIEW_MY_FORM_TEXT = "Моя анкета";
    private final String EDIT_MY_FORM_TEXT = "Изменить анкету";
    private final String BROWSE_FORMS = "Смотреть анкеты";
    private final String BROWSE_LIKES = "Кому я нравлюсь?";


    public MessageBuilder(@Qualifier("XMLResponseParser") IResponseResourceProvider provider) {
        this.provider = provider;
    }

    public SendMessage createMessage(User user, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(user.getId().toString());
        return sendMessage;
    }

    public SendPhoto createPhoto(User user, String text) {
        SendPhoto sendMessage = new SendPhoto();
        sendMessage.setCaption(text);
        sendMessage.setChatId(user.getId().toString());
        return sendMessage;
    }

    public String buildAssembleText(MessageContext context, String text) {

        //Create map which sets all the properties
        final HashMap<String, String> tagPropertyMap = new HashMap<>() {{
            put(USER_NAME_TAG.getTag(), context.getUser().getName());
            put(USER_AGE_TAG.getTag(), context.getUser().getAge() + "");
            put(USER_TARGET_AGE_MAX.getTag(), context.getUser().getTargetMaxAge() + "");
            put(USER_TARGET_AGE_MIN.getTag(), context.getUser().getTargetMinAge() + "");
            put(USER_BIO_TAG.getTag(), context.getUser().getBio());
            if (context.getUser().getGender() != null)
                put(USER_GENDER_TAG.getTag(),
                        context.getUser().getGender().getShortName());
            if (context.getUser().getLocation() != null)
                put(USER_CITY_TAG.getTag(), context.getUser().getLocation().getCityName());
            put(OPPOSITE_USER_NAME_TAG.getTag(), context.getOppositeUser().getName());
            put(OPPOSITE_USER_AGE_TAG.getTag(), context.getOppositeUser().getAge() + "");
            put(OPPOSITE_USER_BIO_TAG.getTag(), context.getOppositeUser().getBio());
            if (context.getOppositeUser().getGender() != null)
                put(OPPOSITE_USER_GENDER_TAG.getTag(),
                        context.getOppositeUser().getGender().getShortName());
            put(OPPOSITE_USER_CITY_TAG.getTag(), context.getOppositeUser().getLocation().getCityName());
            put(OPPOSITE_USER_LINK.getTag(), createUserLink(context.getOppositeUser()));
        }};

        String result = text;
        //Iterate through the map and replace every tag occurrence in text to our property
        for (String key : tagPropertyMap.keySet()) {
            result = result.replaceAll(key, tagPropertyMap.get(key));
        }

        return result;
    }

    private String createUserLink(User user) {
        return "[" + user.getName() + "](tg://user?id=" + user.getId() + ")";
    }

    public String getMessageTextByType(ResponseTemplateTypes type) {
        List<MessageResource> appropriateTypes = provider.getMessageResources()
                .stream()
                .filter(r -> r.getType().equals(type))
                .collect(Collectors.toList());
        int randomIndex = new Random().nextInt(appropriateTypes.size());

        return appropriateTypes.get(randomIndex).getTextBoth();
    }

    public String getMessageTextByType(ResponseTemplateTypes type, GenderEnum gender) {
        List<MessageResource> appropriateTypes = provider.getMessageResources()
                .stream()
                .filter(r -> r.getType().equals(type))
                .collect(Collectors.toList());
        int randomIndex = new Random().nextInt(appropriateTypes.size());
        MessageResource resource = appropriateTypes.get(randomIndex);

        if (gender.equals(GenderEnum.MAN)) {
            if (resource.getTextMale() != null)
                return resource.getTextMale();
            else
                return resource.getTextBoth();
        } else if (gender.equals(GenderEnum.WOMAN)) {
            if (resource.getTextFemale() != null)
                return resource.getTextFemale();
            else
                return resource.getTextBoth();
        } else {
            return resource.getTextBoth();
        }
    }

    public ReplyKeyboardMarkup getConfirmKeyboard() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(
                KeyboardButton.builder()
                        .text(CONFIRM_KEYBOARD_ACCEPT)
                        .build());
        keyboardRow.add(
                KeyboardButton.builder()
                        .text(CONFIRM_KEYBOARD_DECLINE)
                        .build());

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup getFormKeyboard() {
        KeyboardRow keyboardLikeDislike = new KeyboardRow();
        keyboardLikeDislike.add(
                KeyboardButton.builder()
                        .text(LIKE_TEXT)
                        .build());
        keyboardLikeDislike.add(
                KeyboardButton.builder()
                        .text(DISLIKE_TEXT)
                        .build());

        KeyboardRow keyboardBackToMenu = new KeyboardRow();
        keyboardBackToMenu.add(
                KeyboardButton.builder()
                        .text(MENU_TEXT)
                        .build()
        );

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardLikeDislike, keyboardBackToMenu))
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup getMyFormKeyboard() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(
                KeyboardButton.builder()
                        .text(MENU_TEXT)
                        .build()
        );

        return ReplyKeyboardMarkup.builder().keyboardRow(keyboardRow).resizeKeyboard(true).build();
    }



    public InlineKeyboardMarkup getLikeReceivedKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        //View likes button
        InlineKeyboardButton viewLikesButton = new InlineKeyboardButton(SEE_LIKES_TEXT);
        viewLikesButton.setCallbackData(Callbacks.BROWSE_LIKES.getCallback());

        markup.setKeyboard(List.of(
                List.of(
                        viewLikesButton
                )
        ));

        return markup;
    }

    public ReplyKeyboardMarkup getLocationKeyboard() {
        //This should work for geolocation. Need to implement a separate function

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(
                KeyboardButton.builder()
                        .requestLocation(true)
                        .text(LOCATION_KEYBOARD_REQUEST)
                        .build()
        );

        return ReplyKeyboardMarkup.builder().keyboardRow(keyboardRow).resizeKeyboard(true).build();
    }

    public ReplyKeyboardMarkup getPreviousOptionKeyboard(String previousOption) {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(
                KeyboardButton.builder()
                        .text(previousOption)
                        .build()
        );

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup getGenderKeyboard() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(
                KeyboardButton.builder()
                        .text(GenderEnum.MAN.getShortName())
                        .build()
        );
        keyboardRow.add(
                KeyboardButton.builder()
                        .text(GenderEnum.WOMAN.getShortName())
                        .build()
        );

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .resizeKeyboard(true)
                .build();
    }


    public ReplyKeyboardMarkup getMenuKeyboard() {
        //TODO: Get rid of hardcoded strings
        KeyboardRow keyboardRowForms = new KeyboardRow();
        keyboardRowForms.add(
                KeyboardButton.builder()
                        .text("Смотреть анкеты")
                        .build());

        KeyboardRow keyboardRowSelf = new KeyboardRow();
        keyboardRowSelf.add(
                KeyboardButton.builder()
                        .text("Моя анкета")
                        .build());

        KeyboardRow keyboardRowSelfEdit = new KeyboardRow();
        keyboardRowSelfEdit.add(
                KeyboardButton.builder()
                        .text("Изменить анкету")
                        .build());

        KeyboardRow keyboardRowLikes = new KeyboardRow();
        keyboardRowLikes.add(
                KeyboardButton.builder()
                        .text("Мои симпатии")
                        .build());

        return ReplyKeyboardMarkup.builder().keyboard(
                        List.of(
                                keyboardRowForms,
                                keyboardRowSelf,
                                keyboardRowSelfEdit,
                                keyboardRowLikes))
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup getEmptyKeyboard() {
        //This should work for geolocation. Need to implement a separate function
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        KeyboardRow keyboardRow = new KeyboardRow();

        return ReplyKeyboardMarkup.builder().keyboardRow(keyboardRow).build();
    }


}
