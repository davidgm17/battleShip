package com.formacion.battleship.service;

import com.formacion.battleship.repository.GameRepostory;
import com.formacion.battleship.utils.BattleConstant;
import com.formacion.models.response.StartBattleResponseJto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by David Gomez on 22/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Service(BattleConstant.PRINT_SERVICE)
public class Print implements Action<Map<String, StartBattleResponseJto>, Void> {

    private final GameRepostory game;

    @Autowired
    public Print(GameRepostory game) {
        this.game = game;
    }


    @Override
    public Map<String, StartBattleResponseJto> execute(Void empty) {
        return this.game.getAllBattleStatus();
    }

    @Override
    public void checkInputValues(Void empty) {

    }
}
