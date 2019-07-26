package com.formacion.battleship.controller;

import com.formacion.battleship.service.Action;
import com.formacion.battleship.utils.BattleConstant;
import com.formacion.battleship.utils.exceptions.BattleException;
import com.formacion.models.request.LaunchRequestJto;
import com.formacion.models.request.StartBattleValuesRequestJto;
import com.formacion.models.response.StartBattleResponseJto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@RestController
@CrossOrigin
@RequestMapping("battle")
@Api(value = "action", description = "Battle Action Management.")
public class MatchController {

    @Autowired
    @Qualifier(BattleConstant.START_SERVICE)
    private Action startService;

    @Autowired
    @Qualifier(BattleConstant.PRINT_SERVICE)
    private Action printService;

    @Autowired
    @Qualifier(BattleConstant.LAUNCH_SERVICE)
    private Action launchService;


    @ApiOperation(value = "Make a new battleship")
    @PostMapping("/start")
    public ResponseEntity<?> initMain(@RequestBody @Valid StartBattleValuesRequestJto values) {

        ResponseEntity<?> output;
        try {

            StartBattleResponseJto initBattle = (StartBattleResponseJto) startService.execute(values);
            output = new ResponseEntity<>(initBattle, HttpStatus.OK);
        } catch (BattleException error) {
            output = new ResponseEntity<>(error.getInfoException(values.toString()), error.getHttpStatusException());
        }

        return output;
    }

    @ApiOperation(value = "Shoot a rocket")
    @PostMapping("/launch")
    public ResponseEntity<?> launchRocket(@RequestBody @Valid LaunchRequestJto values) {

        ResponseEntity<?> output;
        try {

            StartBattleResponseJto initBattle = (StartBattleResponseJto) launchService.execute(values);
            output = new ResponseEntity<>(initBattle, HttpStatus.OK);
        } catch (BattleException error) {
            output = new ResponseEntity<>(error.getInfoException(values.toString()), error.getHttpStatusException());
        }

        return output;
    }



    @ApiOperation(value = "Print all battleship info")
    @GetMapping("/print")
    public ResponseEntity<?> printInfoBattle() {

        ResponseEntity<?> output;
        try {
            Map<String, StartBattleResponseJto> battleResume = (Map<String, StartBattleResponseJto>) printService.execute(null);
            output = new ResponseEntity<>(battleResume, HttpStatus.OK);
        } catch (BattleException error) {
            output = new ResponseEntity<>(error.getInfoException("print"), error.getHttpStatusException());
        }

        return output;
    }

}
