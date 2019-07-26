package com.formacion.battleship.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BattleException extends RuntimeException {

    private String params;
    private HttpStatus httpStatusException= HttpStatus.BAD_REQUEST;
    private String paramError;
    private String valueError;

    BattleException(String paramError, String valueError) {

        this.paramError =paramError;
        this.valueError = valueError;
    }


    public Map<String, String> getInfoException(@NotNull String inputParams) {
        Map<String, String> infoOutput = new HashMap<>();
        infoOutput.put("params", inputParams);
        infoOutput.put("error_param", this.paramError);
        infoOutput.put("error_value", this.valueError);
        return infoOutput;
    }

    public HttpStatus getHttpStatusException(){
        return this.httpStatusException;
    }
}
