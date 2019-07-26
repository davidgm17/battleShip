package com.formacion.models.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Data
public class FlotaRequestJto {
    @NotNull
    private String name;
    private int quantity;
    @NotNull
    private int size;

    public FlotaRequestJto() {
    }

    public FlotaRequestJto(@NotNull String name, int quantity, @NotNull int size) {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
    }
}
