package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

public class CursorTablaPersona {

	private static final Integer POSICION_ID = 0;
	private static final Integer POSICION_NOMBRE = 1;
	private static final Integer POSICION_ID_APATXA = 2;
	private static final Integer POSICION_CUANTIA_PAGO = 3;
	private static final Integer POSICION_PAGADO = 4;
    private static final Integer POSITION_HECHO = 5;

	private final Cursor cursor;

	public CursorTablaPersona(Cursor cursor) {
		this.cursor = cursor;
	}

	public Long getId() {
		return cursor.getLong(POSICION_ID);
	}

	public String getNombre() {
		return cursor.getString(POSICION_NOMBRE);
	}

	public Long getIdApatxa() {
		return cursor.getLong(POSICION_ID_APATXA);
	}

	public Double getCuantiaPago() {
		return cursor.getDouble(POSICION_CUANTIA_PAGO);
	}

	public Double getPagado() {
		return cursor.getDouble(POSICION_PAGADO);
	}

    public Boolean getHecho() {
        return cursor.getInt(POSITION_HECHO) == 1;
    }
}
