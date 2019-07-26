package com.formacion.battleship.service;

import com.formacion.battleship.repository.GameRepostory;
import com.formacion.battleship.utils.BattleConstant;
import com.formacion.battleship.utils.exceptions.StartBattleExceptionImpl;
import com.formacion.models.request.LaunchRequestJto;
import com.formacion.models.response.StartBattleResponseJto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by David Gomez on 23/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Service(BattleConstant.LAUNCH_SERVICE)
public class Launch implements Action<StartBattleResponseJto, LaunchRequestJto> {

    private final GameRepostory game;
    private final Start startService;
    private int boardSize;

    @Autowired
    public Launch(GameRepostory game, Start startService) {
        this.game = game;
        this.startService = startService;
    }

    @Override
    public StartBattleResponseJto execute(LaunchRequestJto values) {
        boardSize = this.game.getBoardSize();

        if (boardSize == -1) {
            this.startService.startHurryUpBattle();
            boardSize = this.game.getBoardSize();
        }

        this.checkInputValues(values);
        if (values.getX() != -1) {
            this.game.launchMisil(BattleConstant.USER, values.getX(), values.getY());
        }
        this.game.launchMisil(BattleConstant.CPU, getCoordinate(this.boardSize), getCoordinate(this.boardSize));
        return this.game.getStatusGame();

    }

    @Override
    public void checkInputValues(LaunchRequestJto values) {
        if (values.getX() > this.boardSize || values.getX() < -1) {
            throw new StartBattleExceptionImpl("X", String.valueOf(values.getX()));
        }
        if (values.getY() > this.boardSize || values.getX() < -1) {
            throw new StartBattleExceptionImpl("Y", String.valueOf(values.getY()));

        }
    }

    private int getCoordinate(int maxCoordenate) {
        return (int) (Math.random() * maxCoordenate);
    }
}
