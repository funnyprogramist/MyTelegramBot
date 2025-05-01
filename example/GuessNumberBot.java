package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GuessNumberBot extends TelegramLongPollingBot {

    private final Map<Long, Integer> userSecrets = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "No_Bot_Username";
    }

    @Override
    public String getBotToken() {
        return "No_Token";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();

            if (message.equals("/start")) {
                int number = new Random().nextInt(100) + 1;
                userSecrets.put(chatId, number);
                sendText(chatId, "🎲 Я загадав число від 1 до 100. Спробуй вгадати!");
            } else {
                try {
                    int guess = Integer.parseInt(message);
                    int secret = userSecrets.getOrDefault(chatId, -1);

                    if (secret == -1) {
                        sendText(chatId, "Напиши /start, щоб почати гру.");
                        return;
                    }

                    if (guess < secret) {
                        sendText(chatId, "🔼 Більше!");
                    } else if (guess > secret) {
                        sendText(chatId, "🔽 Менше!");
                    } else {
                        sendText(chatId, "🎉 Вітаю! Ти вгадав число: " + secret + " 🎉\nНапиши /start, щоб зіграти ще раз.");
                        userSecrets.remove(chatId);
                    }
                } catch (NumberFormatException e) {
                    sendText(chatId, "❗ Напиши число або /start.");
                }
            }
        }
    }

    private void sendText(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
