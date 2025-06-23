package com.nithieshm.amulprice.repository;

import com.nithieshm.amulprice.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    TelegramUser findByChatId(Long chatId);
    List<TelegramUser> findByIsActiveTrue();
}
