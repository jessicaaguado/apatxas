package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

import java.util.Date;

public class CursorTablaApatxa {

	private static final Integer POSICION_ID = 0;
	private static final Integer POSICION_NOMBRE = 1;
	private static final Integer POSICION_FECHA_INICIO = 2;
    private static final Integer POSICION_FECHA_FIN = 3;
    private static final Integer POSICION_SOLO_UN_DIA = 4;
	private static final Integer POSICION_BOTE_INICIAL = 5;
	private static final Integer POSICION_REPARTO_HECHO = 6;
	private static final Integer POSICION_NUM_PERSONAS_PENDIENTES_PAGAR_O_COBRAR = 7;
	private static final Integer POSICION_DESCONTAR_BOTE_INNICIAL = 8;
    private static final Integer POSICION_GASTO_TOTAL = 9;

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

	public Date getFechaInicio() {
		Long fecha = cursor.getLong(POSICION_FECHA_INICIO);
		if (fecha != null) {
			return new Date(fecha);
		}
		return null;
	}

    public Date getFechaFin() {
        Long fecha = cursor.getLong(POSICION_FECHA_FIN);
        if (fecha != null) {
            return new Date(fecha);
        }
        return null;
    }

    public boolean getSoloUnDia() {
        return cursor.getInt(POSICION_SOLO_UN_DIA) == 1;
    }

	public Double getBoteInicial() {
		return cursor.getDouble(POSICION_BOTE_INICIAL);
	}

	public Double getGastoTotal() {
		return cursor.getDouble(POSICION_GASTO_TOTAL);
	}

	public boolean getRepartoHecho() {
		return cursor.getInt(POSICION_REPARTO_HECHO) == 1;
	}

	public Integer getNumeroPersonasPendientesPagarCobrar() {
		return cursor.getInt(POSICION_NUM_PERSONAS_PENDIENTES_PAGAR_O_COBRAR);
	}

	public boolean getDescontarBoteInicial() {
		return cursor.getInt(POSICION_DESCONTAR_BOTE_INNICIAL) == 1;
	}

}
