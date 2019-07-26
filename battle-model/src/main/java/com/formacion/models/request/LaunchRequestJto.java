package com.formacion.models.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by David Gomez on 23/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Data
public class LaunchRequestJto {
    @NotNull
    private int x;
    @NotNull
    private int y;

    public LaunchRequestJto() {
    }

    public LaunchRequestJto(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
