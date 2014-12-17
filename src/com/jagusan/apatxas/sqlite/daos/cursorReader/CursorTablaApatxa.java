package com.jagusan.apatxas.sqlite.daos.cursorReader;

import java.util.Date;

import android.database.Cursor;

public class CursorTablaApatxa {

	private static final Integer POSICION_ID = 0;
	private static final Integer POSICION_NOMBRE = 1;
	private static final Integer POSICION_FECHA = 2;
	private static final Integer POSICION_BOTE_INICIAL = 3;
	private static final Integer POSICION_GASTO_TOTAL = 4;
	private static final Integer POSICION_GASTO_PAGADO = 5;
	private static final Integer POSICION_REPARTO_HECHO = 6;
	private static final Integer POSICION_NUM_PERSONAS_PENDIENTES_PAGAR_O_COBRAR = 7;

	private final Cursor cursor;

	public CursorTablaApatxa(Cursor cursor) {
		this.cursor = cursor;
	}

	public Long getId() {
		return cursor.getLong(POSICION_ID);
	}

	public String getNombre() {
		return cursor.getString(POSICION_NOMBRE);
	}

	public Date getFecha() {
		Long fecha = cursor.getLong(POSICION_FECHA);
		if (fecha != null) {
			return new Date(fecha);
		}
		return null;
	}

	public Double getBoteInicial() {
		return cursor.getDouble(POSICION_BOTE_INICIAL);
	}

	public Double getGastoTotal() {
		return cursor.getDouble(POSICION_GASTO_TOTAL);
	}

	public Double getGastoPagado() {
		return cursor.getDouble(POSICION_GASTO_PAGADO);
	}

	public boolean getRepartoHecho() {
		return cursor.getInt(POSICION_REPARTO_HECHO) == 1;

	}

	public Integer getNumeroPersonasPendientesPagarCobrar() {
		return cursor.getInt(POSICION_NUM_PERSONAS_PENDIENTES_PAGAR_O_COBRAR);
	}

}
