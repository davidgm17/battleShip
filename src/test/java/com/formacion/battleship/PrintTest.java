package com.formacion.battleship;

import com.formacion.battleship.service.Print;
import com.formacion.battleship.service.Start;
import com.formacion.battleship.utils.BattleConstant;
import com.formacion.models.database.Square;
import com.formacion.models.request.FlotaRequestJto;
import com.formacion.models.request.StartBattleValuesRequestJto;
import com.formacion.models.response.StartBattleResponseJto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * Created by David Gomez on 23/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PrintTest {

    @Autowired
    private Start startService;

    @Autowired
    private Print printService;

    @Test
    public void contextLoads() {
    }
    @Before
    public void loadNewGame() {
        int size = 10;

        List<FlotaRequestJto> flota = new ArrayList<>();
        flota.add(new FlotaRequestJto("shipTest", 1, 5));
        flota.add((new FlotaRequestJto("shipTest", 2, 4)));

        StartBattleValuesRequestJto values = new StartBattleValuesRequestJto(size,flota);
//        values.setMatrixSize(size);
//        values.setStock(flota);
        StartBattleResponseJto output = this.startService.execute(values);
    }

    @Test
    public void check_Print_Service() {
        Map<String, StartBattleResponseJto> printOutput = this.printService.execute(null);

        Assert.assertEquals(2,printOutput.size());
        StartBattleResponseJto user = printOutput.get(BattleConstant.USER);
        StartBattleResponseJto cpu = printOutput.get(BattleConstant.CPU);
        Assert.assertEquals(user.getMatrixSize(),cpu.getMatrixSize());
        Assert.assertEquals(user.getStock().size(),cpu.getStock().size());

        List<Square> userShipInBoard = stream(user.getUserBoard()).flatMap(squares -> stream(squares).filter(Square::isShip)).collect(Collectors.toList());
        List<Square> cpuShipInBoard = stream(cpu.getUserBoard()).flatMap(squares -> stream(squares).filter(Square::isShip)).collect(Collectors.toList());

        Assert.assertEquals(userShipInBoard.size(),cpuShipInBoard.size());


    }
}
