package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

public class CursorTablaGasto {

	private static final Integer POSICION_ID = 0;
	private static final Integer POSICION_CONCEPTO = 1;
	private static final Integer POSICION_TOTAL = 2;
	private static final Integer POSICION_ID_PAGADOR = 3;
	private static final Integer POSICION_NOMBRE_PAGADOR = 4;

	private final Cursor cursor;

	public CursorTablaGasto(Cursor cursor) {
		this.cursor = cursor;
	}

	public Long getId() {
		return cursor.getLong(POSICION_ID);
	}

	public String getConcepto() {
		return cursor.getString(POSICION_CONCEPTO);
	}

	public Double getTotal() {
		return cursor.getDouble(POSICION_TOTAL);
	}

	public Long getIdPagador() {
		return cursor.getLong(POSICION_ID_PAGADOR);
	}

	public String getNombrePagador() {
		return cursor.getString(POSICION_NOMBRE_PAGADOR);
	}

}
