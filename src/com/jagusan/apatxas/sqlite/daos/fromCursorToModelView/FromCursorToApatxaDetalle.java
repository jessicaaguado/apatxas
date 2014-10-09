package com.jagusan.apatxas.sqlite.daos.fromCursorToModelView;

import java.util.Date;

import android.database.Cursor;

import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;

public class FromCursorToApatxaDetalle {

	public static ApatxaDetalle convertir(Cursor cursor) {
		ApatxaDetalle apatxaDetalle = new ApatxaDetalle();
		int i = 0;
		apatxaDetalle.setId(cursor.getLong(i++));
		apatxaDetalle.setNombre(cursor.getString(i++));
		Long fecha = cursor.getLong(i++);
		if (fecha != null) {
			apatxaDetalle.setFecha(new Date(fecha));
		}
		apatxaDetalle.setBoteInicial(cursor.getDouble(i++));
		return apatxaDetalle;
	}

}
