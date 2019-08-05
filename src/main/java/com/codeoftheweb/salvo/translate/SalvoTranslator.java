package com.codeoftheweb.salvo.translate;

import com.codeoftheweb.salvo.Salvo;
import com.codeoftheweb.salvo.dto.SalvoDto;

public class SalvoTranslator {

    public static SalvoDto toDto(Salvo salvo) {

        SalvoDto dto = new SalvoDto();
        dto.setTurn(salvo.getTurn());
        dto.setLocations(salvo.getLocations());

        return dto;
    }
}
