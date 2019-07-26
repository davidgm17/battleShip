package com.formacion.battleship.service;

/**
 * Created by David Gomez on 19/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
public interface Action<T, V> {
    T execute(V v);

    void checkInputValues(V v);

}
