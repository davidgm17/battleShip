package com.formacion.battleship;

import com.formacion.battleship.repository.GameRepostory;
import com.formacion.battleship.service.Launch;
import com.formacion.battleship.service.Start;
import com.formacion.models.database.Square;
import com.formacion.models.request.FlotaRequestJto;
import com.formacion.models.request.LaunchRequestJto;
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
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * Created by David Gomez on 23/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */


@RunWith(SpringRunner.class)
@SpringBootTest
public class LaunchTest {
    @Autowired
    private Start startService;
    @Autowired
    private Launch launchService;
    @Autowired
    private GameRepostory battleRepository;

    @Test
    public void contextLoads() {
    }

//    public void check_no_battle_start() {
//
//        LaunchRequestJto values = new LaunchRequestJto(5, 8);
//        StartBattleResponseJto outputStatus = this.launchService.execute(values);
//        Assert.assertEquals(10, outputStatus.getMatrixSize());
//    }

    @Before
    public void init_battle() {
        int size = 9;

        List<FlotaRequestJto> flota = new ArrayList<>();
        flota.add(new FlotaRequestJto("shipTest", 1, 5));
        flota.add((new FlotaRequestJto("shipTest", 2, 4)));
        StartBattleValuesRequestJto values = new StartBattleValuesRequestJto(size, flota);
        StartBattleResponseJto output = this.startService.execute(values);
        Assert.assertTrue(output.getLogBattle().size() == 1);

    }

    @Test
    public void check_next_turn() {

        LaunchRequestJto valuesRocket = new LaunchRequestJto(-1, -1);
        StartBattleResponseJto outputStatus = this.launchService.execute(valuesRocket);
        Assert.assertTrue(outputStatus.getLogBattle().size() == 3);

        List<Square> cpuImpact = stream(outputStatus.getUserBoard()).flatMap(squares -> stream(squares).filter(Square::isImpact)).collect(Collectors.toList());
        Assert.assertTrue(cpuImpact.size() == 1);
        List<Square> userImpact = stream(outputStatus.getLaunchBoard()).flatMap(squares -> stream(squares).filter(Square::isImpact)).collect(Collectors.toList());
        Assert.assertTrue(userImpact.size() == 0);
    }

    @Test
    public void check_user_attack() {

        LaunchRequestJto values = new LaunchRequestJto(5, 8);
        StartBattleResponseJto outputStatus = this.launchService.execute(values);
        Assert.assertTrue(outputStatus.getLogBattle().size() == 5);

        List<Square> userImpact = stream(outputStatus.getLaunchBoard()).flatMap(squares -> stream(squares).filter(Square::isImpact)).collect(Collectors.toList());
        Assert.assertTrue(userImpact.size() == 1);
        List<Square> cpuImpact = stream(outputStatus.getUserBoard()).flatMap(squares -> stream(squares).filter(Square::isImpact)).collect(Collectors.toList());
        Assert.assertTrue(cpuImpact.size() == 1);
    }
}
