package com.codeoftheweb.salvo.translate;

import com.codeoftheweb.salvo.Player;
import com.codeoftheweb.salvo.dto.PlayerDto;

public class PlayerTranslator {

    public static PlayerDto toDto(Player model) {

        PlayerDto dto = new PlayerDto();
        dto.setId(model.getId());
        dto.setEmail(model.getUserName());

        return dto;
    }
}
