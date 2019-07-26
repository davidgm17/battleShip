package com.formacion.battleship.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class StartBattleExceptionImpl extends BattleException {

    private String params;
    private final HttpStatus httpStatusException;
    private String paramError;
    private String valueError;



    public StartBattleExceptionImpl(String paramError,String valueError){
        super(paramError,valueError);
        this.httpStatusException = HttpStatus.BAD_REQUEST;
        this.paramError =paramError;
        this.valueError = valueError;
    }

}
