package com.formacion.battleship;

import com.formacion.battleship.service.IBoardMatrixService;
import com.formacion.battleship.service.Start;
import com.formacion.models.database.Flota;
import com.formacion.models.database.Square;
import com.formacion.models.request.FlotaRequestJto;
import com.formacion.models.request.StartBattleValuesRequestJto;
import com.formacion.models.response.StartBattleResponseJto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardMatrixServiceTests {

    @Autowired
    private IBoardMatrixService boardMatrixService;
    @Autowired
    private Start startService;

    @Test
    public void contextLoads() {
    }


    @Test
    public void check_size_new_board_matrix() {
        int size = 10;
        Square[][] emptyMatrix = this.boardMatrixService.initMatrix(size);

        Assert.assertEquals(size, emptyMatrix.length);
        Assert.assertEquals(size, emptyMatrix[0].length);

    }

    @Test
    public void check_checkPosition_empty_matrix() {
        int size = 10;
        Square[][] emptyMatrix = this.boardMatrixService.initMatrix(size);
        int shipSize = 4;
        int x = 6;
        int y = 6;
        int z = 1;
        int direction;

        direction = this.boardMatrixService.checkPosition(emptyMatrix, shipSize, x, y, z);
        Assert.assertEquals(1, direction);

        x = 7;
        direction = this.boardMatrixService.checkPosition(emptyMatrix, shipSize, x, y, z);
        Assert.assertEquals(-1, direction);

        z = -1;

        direction = this.boardMatrixService.checkPosition(emptyMatrix, shipSize, x, y, z);
        Assert.assertEquals(1, direction);

        y = 7;
        direction = this.boardMatrixService.checkPosition(emptyMatrix, shipSize, x, y, z);
        Assert.assertEquals(-1, direction);

    }

    @Test
    public void check_save_one_ship() {
        int size = 10;
        Square[][] emptyMatrix = this.boardMatrixService.initMatrix(size);
        int shipSize = 4;

        List<Flota> flota = Collections.singletonList(new Flota("shipTest", 1, shipSize));
        Square[][] board = this.boardMatrixService.fillMatrix(flota, emptyMatrix);
        List<Square> output = stream(board).flatMap(squares -> stream(squares).filter(Square::isShip)).collect(Collectors.toList());
        Assert.assertFalse(output.isEmpty());
    }

    @Test
    public void check_save_float() {
        int size = 10;

        List<FlotaRequestJto> flota = new ArrayList<>();
        flota.add(new FlotaRequestJto("shipTest", 1, 5));
        flota.add((new FlotaRequestJto("shipTest", 2, 4)));

        Square[][] board = this.boardMatrixService.getNewBoard(size, flota);

        List<Square> output = stream(board).flatMap(squares -> stream(squares).filter(Square::isShip)).collect(Collectors.toList());
        Assert.assertFalse(output.isEmpty());
    }

    @Test
    public void check_hurryUp_battle() {
        int size = 0;

        List<FlotaRequestJto> flota = new ArrayList<>();
        flota.add(new FlotaRequestJto("shipTest", 1, 5));
        flota.add((new FlotaRequestJto("shipTest", 2, 4)));
        StartBattleValuesRequestJto values = new StartBattleValuesRequestJto(size, flota);
        StartBattleResponseJto output = this.startService.execute(values);
        Assert.assertEquals(10, output.getMatrixSize());
    }
}
