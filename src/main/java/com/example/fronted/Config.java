package com.example.fronted;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    private final static String BOT_TOKEN = "1869331847:AAH3ivzHkNUzOVBg7R-Pz_PeSy7jBTew3rM";
    @Bean
    public TelegramBot getTelegramBot() {
        return new TelegramBot(BOT_TOKEN);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RT getRT() {
        return  new RT();
    }
}
