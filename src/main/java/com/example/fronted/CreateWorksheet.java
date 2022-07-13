package com.example.fronted;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class CreateWorksheet {

    private final TelegramBot telegramBot;
    private final HashMap<Long, State> conditionHashMap;
    private final HashMap<Long, RT> userInfo;

    @Autowired
    RestTemplate restTemplate;


    public CreateWorksheet(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;

        conditionHashMap = new HashMap<>();
        userInfo = new HashMap<>();


    }

    public void createWorksheet(List<Update> list) {
        list.forEach(update -> {

            Long userId = update.message().from().id();


            if (update.message() != null) {

                if (update.message().text() != null) {

                    if (update.message().text().equals("/start")) {

                        userInfo.put(userId, new RT());
                        userInfo.get(userId).setIdTelegramUser(userId);

                        conditionHashMap.put(userId, State.Start);

                        SendMessage sendMessageStart = new SendMessage(update.message().chat().id(), "Здарова, Бандит^_^\nВведи свой возраст, Бандит");

                        telegramBot.execute(sendMessageStart);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(State.Start)) {

                        try {
                            conditionHashMap.put(userId, State.WaitSex);
                            userInfo.get(userId).setAge(Integer.valueOf(update.message().text()));
                            SendMessage sendMessageAge = new SendMessage(update.message().chat().id(), "Твой возраст: " + update.message().text() + "\nКакой твой любимый секс?");
                            telegramBot.execute(sendMessageAge);
                        } catch (Exception NumberFormatException) {
                            SendMessage sendMessageException = new SendMessage(update.message().chat().id(), "Ti...");
                            telegramBot.execute(sendMessageException);
                        }

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(State.WaitSex)) {

                        if (update.message().text().equals("м") || update.message().text().equals("ж")) {

                            conditionHashMap.put(userId, State.WaitInterest);

                            userInfo.get(userId).setSex(update.message().text());

                            SendMessage sendMessageSex = new SendMessage(update.message().chat().id(), "Твой пол: " + update.message().text() + "\nКто тебе интересны?");
                            telegramBot.execute(sendMessageSex);
                        } else {

                            SendMessage sendMessageSexError = new SendMessage(update.message().chat().id(), "Такого пола нет( Есть только м - мужской и ж - женский");
                            telegramBot.execute(sendMessageSexError);
                        }

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(State.WaitInterest)) {

                        conditionHashMap.put(userId, State.WaitCity);

                        userInfo.get(userId).setInterest(update.message().text());

                        SendMessage sendMessageInterest = new SendMessage(update.message().chat().id(), "Тебе интересны: " + update.message().text() + "\nКакой твой город?");
                        telegramBot.execute(sendMessageInterest);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(State.WaitCity)) {

                        conditionHashMap.put(userId, State.WaitName);

                        userInfo.get(userId).setCity(update.message().text());

                        SendMessage sendMessageCity = new SendMessage(update.message().chat().id(), "Твой город: " + update.message().text() + "\nТвоё имя? Бандит?");
                        telegramBot.execute(sendMessageCity);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(State.WaitName)) {

                        conditionHashMap.put(userId, State.WaitPhoto);

                        userInfo.get(userId).setName(update.message().text());

                        SendMessage sendMessageName = new SendMessage(update.message().chat().id(), "Твоё имя: " + update.message().text() + "\nТеперь фоточку прикрепляй :)");
                        telegramBot.execute(sendMessageName);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(State.WaitDescription)) {

                        conditionHashMap.put(userId, State.GetWorksheet);

                        userInfo.get(userId).setDescription(update.message().text());

                        SendMessage sendMessageDescription = new SendMessage(update.message().chat().id(), "Твоё описание: " + update.message().text()
                                + "\n" + "Анкета заполнена :)");
                        telegramBot.execute(sendMessageDescription);

//                        RT rt = new RT(1, 2, "asd", 231412412L, "dsadasdas", "dasd", 23, "dsadasd");
                        RT createdRt = restTemplate.postForObject("http://localhost:8080/user-save", userInfo.get(userId), RT.class);

                    } else if (update.message().text().equals("/getws") && conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(State.GetWorksheet)) {

                        String str = String.format("http://localhost:8080/user-get-worksheet?age=%d&sex=%s&city=%s", userInfo.get(userId).getAge(), userInfo.get(userId).getSex(), userInfo.get(userId).getCity());
                        ResponseEntity<RT[]> response = restTemplate.getForEntity(str, RT[].class);

                        String str1 = String.format("http://localhost:8080/user-get-worksheet-need?userId=%d", userId);
                        ResponseEntity<Long> responseUserId = restTemplate.getForEntity(str1, Long.class);
                        Long rts = responseUserId.getBody();

                        String str2 = String.format("http://localhost:8080//user-get-worksheet-by-id?userIdReturn=%d", rts);
                        ResponseEntity<RT> response1 = restTemplate.getForEntity(str2, RT.class);
                        RT rtEnd = response1.getBody();

//                        RT[] rts = response.getBody();


                        SendMessage sendMessageWorksheet = new SendMessage(update.message().chat().id(), "Worksheet: " + rtEnd);
                        telegramBot.execute(sendMessageWorksheet);
                    }
                } else if (update.message().document() != null && conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(State.WaitPhoto)) {

                    conditionHashMap.put(userId, State.WaitDescription);

                    userInfo.get(userId).setAvatarPhotoId(update.message().document().fileId());

                    SendMessage sendMessageDescr = new SendMessage(update.message().chat().id(), "Твоё фото: ");
                    SendDocument sendDocument = new SendDocument(update.message().chat().id(), update.message().document().fileId());
                    SendMessage sendMessageDocument = new SendMessage(update.message().chat().id(), "Ну и описание тоже");

                    telegramBot.execute(sendMessageDescr);
                    telegramBot.execute(sendDocument);
                    telegramBot.execute(sendMessageDocument);

                }
            }
        });
    }
}
