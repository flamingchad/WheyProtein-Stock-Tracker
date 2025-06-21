package com.nithieshm.amulprice.service;

import com.nithieshm.amulprice.entity.Product;
import com.nithieshm.amulprice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class TelegramService extends TelegramLongPollingBot {
    private final ProductRepository productRepository;
    private final String botUsername;

    public TelegramService(@Value("${bot_username}") String botUsername,
                           @Value("${bot_token}") String botToken, DefaultBotOptions options, ProductRepository productRepository) {
        super(options, botToken);
        this.botUsername = botUsername;
        this.productRepository = productRepository;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived (Update update) {
        List<Product> products = productRepository.findAll();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            if (message.contains("Hi")) {
                for (Product product : products) {
                    if (product.isInStock()) {
                        long chatId = update.getMessage().getChatId();
                        String name = product.getName();
                        String pinCode = product.getPincode();
                        String url = product.getUrl();
                        String inStock = "Is in stock.";
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(chatId);
                        sendMessage.setText(
                                name + " " + inStock + " " + pinCode + " " + url
                        );
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

}

