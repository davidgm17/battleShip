package com.formacion.models.database;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Data
public class Game {
    private List<Flota> stock;
    private int matrixSize;
    private Square[][] user;
    private Square[][] cpu;
    private Map<Integer, String> logBattle;

    public Game(List<Flota> stock, int matrixSize, Square[][] user, Square[][] cpu, HashMap<Integer, String> log) {
        this.stock = stock;
        this.matrixSize = matrixSize;
        this.user = user;
        this.cpu = cpu;
        this.logBattle = log;
    }
}
