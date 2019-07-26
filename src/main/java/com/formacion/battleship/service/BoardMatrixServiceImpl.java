package com.formacion.battleship.service;

import com.formacion.battleship.service.converters.FlotaConverter;
import com.formacion.battleship.utils.exceptions.StartBattleExceptionImpl;
import com.formacion.models.database.Flota;
import com.formacion.models.database.Square;
import com.formacion.models.request.FlotaRequestJto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Service
public class BoardMatrixServiceImpl implements IBoardMatrixService {
    @Autowired
    private FlotaConverter flotaConverter;

    @Override
    public Square[][] getNewBoard(int matrixSize, List<FlotaRequestJto> stock) {
        Square[][] emptyMatrix = initMatrix(matrixSize);

        List<Flota> flotas = flotaConverter.getFlota(stock);
        return fillMatrix(flotas, emptyMatrix);
    }

    @Override
    public Square[][] initMatrix(int size) {

        Square[][] board = new Square[size][size];
        for (int x = 0; x < board.length; x++) {

            for (int y = 0; y < board[x].length; y++) {
                board[x][y] = new Square();
            }
        }
        return board;
    }

    @Override
    public Square[][] fillMatrix(List<Flota> initialStock, Square[][] emptyMatrix) {
        for (Flota flota : initialStock) {
            for (int ship = 0; ship < flota.getQuantity(); ship++) {
                boolean position = false;
                for (int testNumber = 0; testNumber < 1000 && !position; testNumber++) {
                    int x = getCoordinate(emptyMatrix.length);
                    int y = getCoordinate(emptyMatrix.length);
                    int z = getExe();
                    int direction = checkPosition(emptyMatrix, flota.getSize(), x, y, z);

                    if (direction != 0) {
                        saveShip(emptyMatrix, flota.getSize(), x, y, z, direction);
                        position = true;
                    }
                }

                if (!position) {
                    throw new StartBattleExceptionImpl("ship", "NotPosition");
                }
            }
        }


        return emptyMatrix;
    }

    @Override
    public int checkPosition(Square[][] matrix, int size, int x, int y, int z) {
        int direction = 0;
        if ((z == 1 && (matrix.length >= (size + x))) || (z == -1 && (matrix.length >= (size + y)))) {
            direction = 1;

        } else if ((z == 1 && 0 <= x - size) || (z == -1 && 0 <= y - size)) {
            direction = -1;
        }

        for (int i = 0; i < size && direction != 0; i++) {
            if (z == 1) {
                int xCoordenate = x + (i * direction);
                direction = matrix[xCoordenate][y].isShip() ? 0 : direction;

            } else if (z == -1) {
                int yCoordenate = y + (i * direction);
                direction = matrix[x][yCoordenate].isShip() ? 0 : direction;
            }
        }
        return direction;

    }

    @Override
    public void saveShip(Square[][] matrix, int size, int x, int y, int z, int direction) {

        for (int i = 0; i < size; i++) {
            if (z == 1) {
                int xCoordenate = x + (i * direction);
                matrix[xCoordenate][y].setShipSize(size);
                matrix[xCoordenate][y].setZ(z);
                matrix[xCoordenate][y].setShip(true);

            } else if (z == -1) {
                int yCoordenate = y + (i * direction);
                matrix[x][yCoordenate].setShipSize(size);
                matrix[x][yCoordenate].setZ(z);
                matrix[x][yCoordenate].setShip(true);
            }
        }
    }

    /**
     * @return 1 si eje horizontal, -1 si eje vertical
     */
    private int getExe() {
        return ((int) (Math.random() * 10)) >= 5 ? 1 : -1;
    }

    private int getCoordinate(int maxCoordenate) {
        return (int) (Math.random() * maxCoordenate);
    }
}
