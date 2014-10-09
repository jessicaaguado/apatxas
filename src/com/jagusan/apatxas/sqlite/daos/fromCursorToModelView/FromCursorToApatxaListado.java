package com.jagusan.apatxas.sqlite.daos.fromCursorToModelView;

import java.util.Date;

import android.database.Cursor;

import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;

public class FromCursorToApatxaListado {

	public static ApatxaListado convertir(Cursor cursor) {
		ApatxaListado apatxaListado = new ApatxaListado();
		int i = 0;
		apatxaListado.setId(cursor.getLong(i++));
		apatxaListado.setNombre(cursor.getString(i++));
		Long fecha = cursor.getLong(i++);
		if (fecha != null){			
			apatxaListado.setFecha(new Date(fecha));
		}		
		apatxaListado.setBoteInicial(cursor.getDouble(i++));
		return apatxaListado;
	}

}
