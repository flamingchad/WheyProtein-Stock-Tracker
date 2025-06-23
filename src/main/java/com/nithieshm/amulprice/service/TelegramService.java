package com.nithieshm.amulprice.service;

import com.nithieshm.amulprice.entity.Product;
import com.nithieshm.amulprice.entity.TelegramUser;
import com.nithieshm.amulprice.repository.ProductRepository;
import com.nithieshm.amulprice.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelegramService extends TelegramLongPollingBot {
    private final ProductRepository productRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final String botUsername;

    public TelegramService(@Value("${SPRING_TELEGRAM_USERNAME}") String botUsername,
                           @Value("${SPRING_TELEGRAM_TOKEN}") String botToken, DefaultBotOptions options,
                           ProductRepository productRepository, TelegramUserRepository telegramUserRepository) {
        super(options, botToken);
        this.botUsername = botUsername;
        this.productRepository = productRepository;
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived (Update update) {
        SendMessage sendMessage = new SendMessage();
        Long chatId = update.getMessage().getChatId();

        TelegramUser telegramUser = telegramUserRepository.findByChatId(chatId);

        if (telegramUser == null) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String message = update.getMessage().getText();
                if (message.equals("/start")) {
                    try {
                        execute(registerTelegramUser(update, sendMessage));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else if (update.getMessage().getText().equals("/start")) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("Already registered!!");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            sendMessage.setChatId(chatId);
            sendMessage.setText("Invalid command!, Current available commands: /start");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public SendMessage registerTelegramUser(Update update, SendMessage message) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(update.getMessage().getChatId());
        telegramUser.setUsername(update.getMessage().getFrom().getUserName());
        telegramUser.setActive(true);
        telegramUser.setRegisteredAt(LocalDateTime.now());
        telegramUserRepository.save(telegramUser);
        return sendWelcomeMessage(update, telegramUser, message);
    }
    public SendMessage sendWelcomeMessage(Update update, TelegramUser user, SendMessage message) {
        message.setChatId(user.getChatId());
        message.setText("Thank you for subscribing to the bot! " +
                "\nYou will now recieve notifications as soon as a product goes in stock!");
        return message;
    }

    public void notifyAllUsers() {
        List<Product> products = productRepository.findAll();
        List<TelegramUser> users = telegramUserRepository.findByIsActiveTrue();
        for (Product product : products) {
            if (!product.isPrevStockStatus() && product.isInStock()) {
               sendNotification(users, product);
            }
        }
    }

    public void sendNotification(List<TelegramUser> users, Product product) {
        SendMessage sendMessage = new SendMessage();
        for (TelegramUser user : users) {
            sendMessage.setChatId(user.getChatId());
            sendMessage.setText(product.getName() + " Is currently in stock!!" + "\n\n" + "URL: " + product.getUrl());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

