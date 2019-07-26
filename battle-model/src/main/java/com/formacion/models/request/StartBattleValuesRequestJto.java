package com.formacion.models.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Data
public class StartBattleValuesRequestJto {
    @NotNull
    private int matrixSize;
    @NotEmpty
    private List<FlotaRequestJto> stock;

    public StartBattleValuesRequestJto() {
    }

    public StartBattleValuesRequestJto(int matrixSize, List<FlotaRequestJto> stock) {
        this.matrixSize = matrixSize;
        this.stock = stock;
    }
}
