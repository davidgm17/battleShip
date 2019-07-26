package com.formacion.models.database;

import com.formacion.models.request.FlotaRequestJto;
import lombok.Data;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Data
public class Flota {
    private String name;
    private int quantity;
    private int size;

    public Flota(String name, int quantity, int size) {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
    }

    public Flota (FlotaRequestJto flotaJto){
        this.name = flotaJto.getName();
        this.quantity = flotaJto.getQuantity();
        this.size = flotaJto.getSize();
    }
}
