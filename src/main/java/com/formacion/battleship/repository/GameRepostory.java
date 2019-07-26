package com.formacion.battleship.repository;

import com.formacion.battleship.utils.BattleConstant;
import com.formacion.models.SquareStatus;
import com.formacion.models.database.Flota;
import com.formacion.models.database.Game;
import com.formacion.models.database.Square;
import com.formacion.models.response.StartBattleResponseJto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * Created by David Gomez on 22/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Component
public class GameRepostory {

    private Game game;
    private int initShipX = -1;
    private int initShipY = -1;

    public int getBoardSize() {
        if (game != null){
            return this.game.getMatrixSize();
        }
        return -1;
    }

    public void loadNewGame(List<Flota> stock, int matrixSize, Square[][] user, Square[][] cpu) {
        HashMap<Integer, String> log = new HashMap<>();
        log.put(0, "Init Game, board size: " + String.valueOf(matrixSize) + "*" + String.valueOf(matrixSize)+"\n= = = = =");
        this.game = new Game(stock, matrixSize, user, cpu, log);
    }

    public StartBattleResponseJto getStatusGame() {
        Square[][] launchBoard = getEnemyStatusBoard(BattleConstant.CPU);

        return StartBattleResponseJto
                .builder()
                .matrixSize(game.getMatrixSize())
                .stock(game.getStock())
                .userBoard(game.getUser())
                .launchBoard(launchBoard)
                .endGame(isEndBattle())
                .logBattle(game.getLogBattle())
                .build();
    }

    private Square[][] getEnemyStatusBoard(String enemy) {
        int sizeBoard = game.getUser().length;
        Square[][] launchBoard = new Square[sizeBoard][sizeBoard];
        if (BattleConstant.CPU.equals(enemy)) {
            for (int x = 0; x < game.getCpu().length; x++) {
                for (int y = 0; y < game.getCpu().length; y++) {
                    Square square = new Square();
                    square.setStatus(game.getCpu()[x][y].getStatus());
                    square.setImpact(game.getCpu()[x][y].isImpact());
                    //linea para poder activar trampas
                    square.setShipSize(game.getCpu()[x][y].getShipSize());
                    launchBoard[x][y] = square;

                }
            }
        } else {
            for (int x = 0; x < game.getUser().length; x++) {
                for (int y = 0; y < game.getUser().length; y++) {
                    Square square = new Square();
                    square.setStatus(game.getUser()[x][y].getStatus());
                    square.setImpact(game.getUser()[x][y].isImpact());
                    //linea para poder activar trampas
                    square.setShipSize(game.getUser()[x][y].getShipSize());
                    launchBoard[x][y] = square;

                }
            }
        }
        return launchBoard;
    }

    public Map<String, StartBattleResponseJto> getAllBattleStatus() {
        Map<String, StartBattleResponseJto> output = new HashMap<>();
        if (!Objects.isNull(game)) {
            game.getLogBattle().put((game.getLogBattle().size()), "Battle Resume.");
            output.put(BattleConstant.USER, getStatusGame());

            Square[][] launchBoard = getEnemyStatusBoard(BattleConstant.USER);

            output.put(BattleConstant.CPU,
                    StartBattleResponseJto
                            .builder()
                            .matrixSize(game.getMatrixSize())
                            .stock(game.getStock())
                            .userBoard(game.getCpu())
                            .launchBoard(launchBoard)
                            .endGame(isEndBattle())
                            .logBattle(game.getLogBattle())
                            .build());
        }
        return output;
    }

    public void launchMisil(String rocketOwner, int x, int y) {
        this.game.getLogBattle()
                .put(this.game.getLogBattle().size(),rocketOwner
                        +" Coordenates:\n\tX --> "+ String.valueOf(x)
                        +" \n\tY --> "+ String.valueOf(y) );
        if (BattleConstant.USER.equals(rocketOwner)) {
            Square square = this.game.getCpu()[x][y];
            square.setImpact(true);
            if (square.isShip()) {
                square.setStatus(this.isSunken(this.game.getCpu(), x, y) ? SquareStatus.SUNKEN : SquareStatus.TOUCH);



                if (SquareStatus.SUNKEN.equals(square.getStatus())) {
                    this.updateSunken(rocketOwner, x, y);

                    this.game.getLogBattle()
                            .put(this.game.getLogBattle().size(),"Status: "+ SquareStatus.SUNKEN
                                    +", Ship size: "+ this.game.getCpu()[x][y].getShipSize()+"\n= = = = =");

                }else{
                    this.game.getLogBattle().put(this.game.getLogBattle().size(),"Status: "+ SquareStatus.TOUCH+"\n= = = = =");
                }
            } else {
                square.setStatus(SquareStatus.WATTER);
                this.game.getLogBattle().put(this.game.getLogBattle().size(),"Status: "+ SquareStatus.WATTER+"\n= = = = =");
            }
        } else {
            Square square = this.game.getUser()[x][y];
            if (square.isImpact()){
                int maxSize = this.game.getUser().length;
                this.launchMisil(BattleConstant.CPU,getCoordinate(maxSize),getCoordinate(maxSize));
            }
            square.setImpact(true);
            if (square.isShip()) {
                square.setStatus(this.isSunken(this.game.getUser(), x, y) ? SquareStatus.SUNKEN : SquareStatus.TOUCH);



                if (SquareStatus.SUNKEN.equals(square.getStatus())) {
                    this.updateSunken(rocketOwner, x, y);

                    this.game.getLogBattle()
                            .put(this.game.getLogBattle().size(),"Status: "+ SquareStatus.SUNKEN
                                    +"Ship size: "+ this.game.getUser()[x][y].getShipSize()+"\n= = = = =");
                }else{
                    this.game.getLogBattle().put(this.game.getLogBattle().size(),"Status: "+ SquareStatus.TOUCH+"\n= = = = =");
                }
            } else {
                square.setStatus(SquareStatus.WATTER);

                this.game.getLogBattle().put(this.game.getLogBattle().size(),"Status: "+ SquareStatus.WATTER+"\n= = = = =");
            }

        }
    }

    private boolean isSunken(Square[][] board, int x, int y) {
        int sizeBoard = board.length;
        int sizeShip = board[x][y].getShipSize();
        int axis = board[x][y].getZ();
        int shipSquareFind = 0;
        int shipSquareImpact = 0;
        if (axis == 1) {
            for (int i = x; i < sizeBoard && board[i][y].isShip() && board[i][y].getZ() == axis ; i++) {
                shipSquareImpact = board[i][y].isImpact() ? shipSquareImpact + 1 : shipSquareImpact;
                shipSquareFind++;
            }
            this.initShipX = x - sizeShip + shipSquareFind;
            if (shipSquareFind != sizeShip) {
                for (int i = (x - 1); i >= 0 && board[i][y].isShip() && board[i][y].getZ() == axis; i--) {
                    shipSquareImpact = board[i][y].isImpact() ? shipSquareImpact + 1 : shipSquareImpact;
                    shipSquareFind++;
                }
            }
            return shipSquareImpact == sizeShip;

        } else {
            for (int i = y; i < sizeBoard && board[x][i].isShip() && board[x][i].getZ() == axis; i++) {
                shipSquareImpact = board[x][i].isImpact() ? shipSquareImpact + 1 : shipSquareImpact;
                shipSquareFind++;
            }
            this.initShipY = y - sizeShip + shipSquareFind;
            if (shipSquareFind != sizeShip) {
                for (int i = (y - 1); i >= 0 && board[x][i].isShip() && board[x][i].getZ() == axis; i--) {
                    shipSquareImpact = board[x][i].isImpact() ? shipSquareImpact + 1 : shipSquareImpact;
                    shipSquareFind++;
                }
            }
            return shipSquareImpact == sizeShip;

        }
    }

    private void updateSunken(String rocketOwner, int x, int y) {
        int boardSize=this.game.getCpu().length;
        if (BattleConstant.USER.equals(rocketOwner)) {
            int axis = this.game.getCpu()[x][y].getZ();
            // int shipSize = this.game.getCpu()[x][y].getShipSize();
            if ((axis == 1)&&(this.initShipX != -1)) {
                int lastSquare = this.initShipX+ this.game.getCpu()[x][y].getShipSize();
                for (int i = this.initShipX;i<boardSize && this.game.getCpu()[i][y].isShip() && i<= lastSquare; i++) {
                    if (this.game.getCpu()[i][y].isImpact()) {
                        this.game.getCpu()[i][y].setStatus(SquareStatus.SUNKEN);
                    }
                }
                this.initShipX = -1;
            } else if (axis == -1 && this.initShipY != -1){
                int lastSquare = this.initShipY+ this.game.getCpu()[x][y].getShipSize();
                for (int i = this.initShipY;i<boardSize && this.game.getCpu()[x][i].isShip() && i<= lastSquare; i++) {
                    if (this.game.getCpu()[x][i].isImpact()) {
                        this.game.getCpu()[x][i].setStatus(SquareStatus.SUNKEN);
                    }
                }
                this.initShipY = -1;
            }
        } else {
            int axis = this.game.getUser()[x][y].getZ();
            // int shipSize = this.game.getCpu()[x][y].getShipSize();
            if (axis == 1 && this.initShipX != -1) {
                int lastSquare = this.initShipX+ this.game.getUser()[x][y].getShipSize();
                for (int i = this.initShipX;i<boardSize && this.game.getUser()[i][y].isShip()&& i<= lastSquare; i++) {
                    if (this.game.getUser()[i][y].isImpact()) {
                        this.game.getUser()[i][y].setStatus(SquareStatus.SUNKEN);
                    }
                }
                this.initShipX = -1;
            } else if (axis == -1 && this.initShipY != -1){
                int lastSquare = this.initShipY+ this.game.getUser()[x][y].getShipSize();
                for (int i = this.initShipY;i<boardSize && this.game.getUser()[x][i].isShip()&& i<= lastSquare; i++) {
                    if (this.game.getUser()[x][i].isImpact()) {
                        this.game.getUser()[x][i].setStatus(SquareStatus.SUNKEN);
                    }
                }
                this.initShipY = -1;
            }
        }
    }
    private boolean isEndBattle(){
        List<Square> userShipImpact = stream(this.game.getUser())
                .flatMap(squares -> stream(squares)
                        .filter(Square::isShip)
                        .filter(Square::isImpact))
                .collect(Collectors.toList());
        List<Square> cpuShipImpact = stream(this.game.getCpu())
                .flatMap(squares -> stream(squares)
                        .filter(Square::isShip)
                        .filter(Square::isImpact))
                .collect(Collectors.toList());
        int totalSquareShip = this.game.getStock().stream().mapToInt(flota -> flota.getSize()*flota.getQuantity()).sum();
        return (totalSquareShip == userShipImpact.size() || totalSquareShip == cpuShipImpact.size());
    }
    private int getCoordinate(int maxCoordenate) {
        return (int) (Math.random() * maxCoordenate);
    }
}
