package com.formacion.models.database;

import com.formacion.models.SquareStatus;
import lombok.Data;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Data
public class Square {
    // Si z =  1 --> eje horizontal
    // Si z = -1 --> eje vertical
    private int z;
    private SquareStatus status;
    private boolean impact;
    private boolean ship;
    private int shipSize;

    public Square() {
        this.z = 0;
        this.status = SquareStatus.INIT;
        this.impact = false;
        this.ship = false;
        this.shipSize = 0;
    }

}
