package com.jagusan.apatxas.sqlite.daos.fromCursorToModelView;

import android.database.Cursor;

import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;

public class FromCursorToApatxaListado {

	public static ApatxaListado convertir(Cursor cursor) {
		ApatxaListado apatxaListado = new ApatxaListado();
		TablaApatxaCursor tablaApatxaCursor = new TablaApatxaCursor(cursor);
		apatxaListado.setId(tablaApatxaCursor.getId());
		apatxaListado.setNombre(tablaApatxaCursor.getNombre());
		apatxaListado.setFecha(tablaApatxaCursor.getFecha());				
		apatxaListado.setBoteInicial(tablaApatxaCursor.getBoteInicial());		
		apatxaListado.setGastoTotal(tablaApatxaCursor.getGastoTotal());		
		apatxaListado.setGastoTotal( tablaApatxaCursor.getGastoPagado());
		return apatxaListado;
	}
	
	

}
