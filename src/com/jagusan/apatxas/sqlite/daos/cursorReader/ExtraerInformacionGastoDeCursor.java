package com.jagusan.apatxas.sqlite.daos.cursorReader;

import android.database.Cursor;
import android.util.Log;

import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;

public class ExtraerInformacionGastoDeCursor {

	public static GastoApatxaListado extraer(Cursor cursor, GastoApatxaListado gasto) {		
		CursorTablaGasto tablaGastoCursor = new CursorTablaGasto(cursor);
		gasto.setId(tablaGastoCursor.getId());
		gasto.setConcepto(tablaGastoCursor.getConcepto());		
		gasto.setTotal(tablaGastoCursor.getTotal());
		gasto.setIdPagadoPor(tablaGastoCursor.getIdPagador());
		gasto.setPagadoPor(tablaGastoCursor.getNombrePagador());
		return gasto;
	}
	

}
