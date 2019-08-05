package com.codeoftheweb.salvo.translate;

import com.codeoftheweb.salvo.GamePlayer;
import com.codeoftheweb.salvo.dto.GamePlayerDto;
import com.codeoftheweb.salvo.dto.SalvoDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GamePlayerTranslator {

    public static List<GamePlayerDto> toDtos(List<GamePlayer> models) {
        return models.stream()
                .map(GamePlayerTranslator::toDto)
                .collect(toList());
    }

    public static GamePlayerDto toDto(GamePlayer model) {

        List<SalvoDto> salvoDtos = model.getSalvoes()
                .stream()
                .map(SalvoTranslator::toDto)
                .collect(toList());

        GamePlayerDto dto = new GamePlayerDto();
        dto.setId(model.getId());
        dto.setPlayer(PlayerTranslator.toDto(model.getPlayer()));
        dto.setSalvoes(salvoDtos);

        return dto;
    }
}
