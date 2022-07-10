package com.example.fronted;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CreateWorksheet {

    private final TelegramBot telegramBot;
    private final HashMap<Long, Condition> conditionHashMap;
    private final Set<Long> userIdChat;

    @Autowired
    RestTemplate restTemplate;


    public CreateWorksheet(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;

        conditionHashMap = new HashMap<>();
        userIdChat = new HashSet<>();


    }

    public void createWorksheet(List<Update> list) {
        list.forEach(update -> {

            Long userId = update.message().from().id();

            if (update.message() != null) {

                if (update.message().text() != null) {

                    if (update.message().text().equals("/start")) {

                        conditionHashMap.put(userId, Condition.Start);

                    } else if (conditionHashMap.get(userId) != null && conditionHashMap.get(userId).equals(Condition.Start)) {

                        userIdChat.add(userId);
                        conditionHashMap.put(userId, Condition.WaitSex);

                        SendMessage sendMessageAge = new SendMessage(update.message().chat().id(), "Твой возраст: " + update.message().text());
                        telegramBot.execute(sendMessageAge);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(Condition.WaitSex)) {

                        conditionHashMap.put(userId, Condition.WaitInterest);

                        SendMessage sendMessageSex = new SendMessage(update.message().chat().id(), "Твой пол: " + update.message().text());
                        telegramBot.execute(sendMessageSex);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(Condition.WaitInterest)) {

                        conditionHashMap.put(userId, Condition.WaitCity);

                        SendMessage sendMessageInterest = new SendMessage(update.message().chat().id(), "Тебе интересны: " + update.message().text());
                        telegramBot.execute(sendMessageInterest);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(Condition.WaitCity)) {

                        conditionHashMap.put(userId, Condition.WaitName);

                        SendMessage sendMessageCity = new SendMessage(update.message().chat().id(), "Твой город: " + update.message().text());
                        telegramBot.execute(sendMessageCity);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(Condition.WaitName)) {

                        conditionHashMap.put(userId, Condition.WaitDescription);

                        SendMessage sendMessageName = new SendMessage(update.message().chat().id(), "Твоё имя: " + update.message().text());
                        telegramBot.execute(sendMessageName);

                    } else if (conditionHashMap.get(userId) != null
                            && conditionHashMap.get(userId).equals(Condition.WaitDescription)) {

                        conditionHashMap.put(userId, Condition.End);

                        SendMessage sendMessageDescription = new SendMessage(update.message().chat().id(), "Твоё описание: " + update.message().text()
                                + "\n" + "Анкета заполнена :)");
                        telegramBot.execute(sendMessageDescription);

                        RT rt = new RT(1, 2, "asd", 231412412L, "dsadasdas", "dasd", 23, "dsadasd");
                        RT createdRt = restTemplate.postForObject("http://localhost:8080/user-save", rt, RT.class);


                    }
                }
            }
        });
    }
}
