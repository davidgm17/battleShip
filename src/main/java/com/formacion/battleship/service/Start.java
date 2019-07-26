package com.formacion.battleship.service;

import com.formacion.battleship.repository.GameRepostory;
import com.formacion.battleship.service.converters.FlotaConverter;
import com.formacion.battleship.utils.BattleConstant;
import com.formacion.battleship.utils.exceptions.StartBattleExceptionImpl;
import com.formacion.models.database.Flota;
import com.formacion.models.database.Square;
import com.formacion.models.request.FlotaRequestJto;
import com.formacion.models.request.StartBattleValuesRequestJto;
import com.formacion.models.response.StartBattleResponseJto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Service(BattleConstant.START_SERVICE)
public class Start implements Action<StartBattleResponseJto, StartBattleValuesRequestJto> {


    private final BoardMatrixServiceImpl boardservice;

    private final GameRepostory game;

    private final FlotaConverter flotaConverter;

    @Autowired
    public Start(BoardMatrixServiceImpl boardservice, GameRepostory game, FlotaConverter flotaConverter) {
        this.boardservice = boardservice;
        this.game = game;
        this.flotaConverter = flotaConverter;
    }

    @Override
    public StartBattleResponseJto execute(StartBattleValuesRequestJto inputValues) {

        this.checkInputValues(inputValues);
        if (inputValues.getMatrixSize() == 0) {
            this.startHurryUpBattle();
        } else {

            Square[][] boardUser = this.boardservice.getNewBoard(inputValues.getMatrixSize(), inputValues.getStock());
            Square[][] boardCpu = this.boardservice.getNewBoard(inputValues.getMatrixSize(), inputValues.getStock());
            List<Flota> flotas = flotaConverter.getFlota(inputValues.getStock());
            this.game.loadNewGame(flotas, inputValues.getMatrixSize(), boardUser, boardCpu);
        }

        return this.game.getStatusGame();
    }

    @Override
    public void checkInputValues(StartBattleValuesRequestJto inputValues) {
        if (inputValues.getStock().isEmpty()) {
            throw new StartBattleExceptionImpl("stock", inputValues.getStock().toString());
        }

        if (inputValues.getMatrixSize() != 0) {

            int totalFlotaSquare = inputValues.getStock().stream().mapToInt((FlotaRequestJto flota) -> flota.getQuantity() * flota.getSize()).sum();
            if (totalFlotaSquare > ((inputValues.getMatrixSize() * inputValues.getMatrixSize()) / 2)) {
                throw new StartBattleExceptionImpl("Min_Size", String.valueOf(totalFlotaSquare + 1));
            }
        }
    }

    public void startHurryUpBattle() {
        int size = 10;

        List<FlotaRequestJto> flota = new ArrayList<>();
        flota.add(new FlotaRequestJto("Portaaviones", 1, 5));
        flota.add((new FlotaRequestJto("Cruceros", 2, 4)));
        flota.add((new FlotaRequestJto("Destructores", 3, 3)));
        flota.add((new FlotaRequestJto("Fragatas", 2, 2)));
        flota.add((new FlotaRequestJto("Submarinos", 4, 1)));
        StartBattleValuesRequestJto values = new StartBattleValuesRequestJto(size, flota);
        this.execute(values);

    }

}
