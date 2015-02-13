package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

import com.jagusan.apatxas.logicaNegocio.cursorReader.CursorTablaGasto;
import com.jagusan.apatxas.modelView.GastoApatxaListado;

public class ExtraerInformacionGastoDeCursor {

	public static GastoApatxaListado extraer(Cursor cursor, GastoApatxaListado gasto) {
		CursorTablaGasto tablaGastoCursor = new CursorTablaGasto(cursor);
		gasto.setId(tablaGastoCursor.getId());
		gasto.setConcepto(tablaGastoCursor.getConcepto());
		gasto.setTotal(tablaGastoCursor.getTotal());
		gasto.setIdPagadoPor(tablaGastoCursor.getIdPagador());
		gasto.setPagadoPor(tablaGastoCursor.getNombrePagador());
        gasto.idContactoPersonaPagadoPor = tablaGastoCursor.getIdContactoPagador();
		return gasto;
	}

}
