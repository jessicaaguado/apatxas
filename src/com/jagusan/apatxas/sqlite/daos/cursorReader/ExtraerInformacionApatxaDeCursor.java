package com.jagusan.apatxas.sqlite.daos.cursorReader;

import android.database.Cursor;

import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;

public class ExtraerInformacionApatxaDeCursor {

	public static ApatxaListado extraer(Cursor cursor, ApatxaListado apatxaListado) {		
		CursorTablaApatxa tablaApatxaCursor = new CursorTablaApatxa(cursor);
		apatxaListado.setId(tablaApatxaCursor.getId());
		apatxaListado.setNombre(tablaApatxaCursor.getNombre());
		apatxaListado.setFecha(tablaApatxaCursor.getFecha());				
		apatxaListado.setBoteInicial(tablaApatxaCursor.getBoteInicial());		
		apatxaListado.setGastoTotal(tablaApatxaCursor.getGastoTotal());		
		apatxaListado.setPagado(tablaApatxaCursor.getGastoPagado());
		apatxaListado.setRepartoRealizado(tablaApatxaCursor.getRepartoHecho());
		return apatxaListado;
	}
	
	

}
