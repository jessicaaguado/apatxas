package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

public class CursorContacto {

    private static final Integer POSICION_ID = 0;
    private static final Integer POSICION_NOMBRE = 1;
    private static final Integer POSICION_FOTO_URI = 2;

    private final Cursor cursor;

    public CursorContacto(Cursor cursor) {
        this.cursor = cursor;
    }

    public Long getId() {
        return cursor.getLong(POSICION_ID);
    }

    public String getNombre() {
        return cursor.getString(POSICION_NOMBRE);
    }

    public String getFotoUri() {
        return cursor.getString(POSICION_FOTO_URI);
    }

}
