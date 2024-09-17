package com.giobank.cards.mapper;

import com.giobank.cards.dto.CardsDto;
import com.giobank.cards.entity.Cards;

public class CardsMapper {

    public static CardsDto mapToCardsDto(Cards cards, CardsDto dto) {
        dto.setCardNumber(cards.getCardNumber());
        dto.setCardType(cards.getCardType());
        dto.setMobileNumber(cards.getMobileNumber());
        dto.setTotalLimit(cards.getTotalLimit());
        dto.setAvailableAmount(cards.getAvailableAmount());
        dto.setAmountUsed(cards.getAmountUsed());
        return dto;
    }

    public static Cards mapToCards(CardsDto dto, Cards cards) {
        cards.setCardNumber(dto.getCardNumber());
        cards.setCardType(dto.getCardType());
        cards.setMobileNumber(dto.getMobileNumber());
        cards.setTotalLimit(dto.getTotalLimit());
        cards.setAvailableAmount(dto.getAvailableAmount());
        cards.setAmountUsed(dto.getAmountUsed());
        return cards;
    }
}

