package com.formacion.battleship.service;

import com.formacion.models.database.Flota;
import com.formacion.models.database.Square;
import com.formacion.models.request.FlotaRequestJto;

import java.util.List;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
public interface IBoardMatrixService {

    Square[][] getNewBoard(int matrixSize, List<FlotaRequestJto> stock);

    Square[][] initMatrix(int size);

    Square[][] fillMatrix(List<Flota> initialStock, Square[][] emptyMatrix);

    int checkPosition(Square[][] matrix, int size, int x, int y, int z);

    void saveShip(Square[][] matrix, int size, int x, int y, int z, int direction);
}
