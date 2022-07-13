package com.example.fronted;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Bot implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final HashMap<Long, State>  conditionHashMap;
    private final Set<Long> userIdChat;
    private final CreateWorksheet createWorksheet;


    private final static String BOT_TOKEN = "1869331847:AAH3ivzHkNUzOVBg7R-Pz_PeSy7jBTew3rM";

    public Bot(TelegramBot telegramBot, CreateWorksheet createWorksheet) {

        this.telegramBot = telegramBot;
        this.createWorksheet = createWorksheet;

        conditionHashMap = new HashMap<>();
        userIdChat = new HashSet<>();

        telegramBot.setUpdatesListener(this);

    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> list) {
        createWorksheet.createWorksheet(list);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
