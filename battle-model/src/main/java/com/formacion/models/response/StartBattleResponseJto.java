package com.formacion.models.response;

import com.formacion.models.database.Flota;
import com.formacion.models.database.Square;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Data
@Builder
public class StartBattleResponseJto {

    private Square[][] userBoard;
    private Square[][] launchBoard;
    private int matrixSize;
    private List<Flota> stock;
    private boolean endGame;
    private Map<Integer, String> logBattle;
}
