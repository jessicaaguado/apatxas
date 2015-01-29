package com.jagusan.apatxas.sqlite.daos.cursorReader;

import android.database.Cursor;

import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;

public class ExtraerInformacionPersonaDeCursor {

	public static PersonaListado extraer(Cursor cursor, PersonaListado persona) {
		CursorTablaPersona tablaPersonaCursor = new CursorTablaPersona(cursor);
		persona.setId(tablaPersonaCursor.getId());
		persona.setNombre(tablaPersonaCursor.getNombre());
		return persona;
	}

	public static PersonaListadoReparto extraer(Cursor cursor, PersonaListadoReparto persona) {
		CursorTablaPersona tablaPersonaCursor = new CursorTablaPersona(cursor);
		persona.setId(tablaPersonaCursor.getId());
		persona.setNombre(tablaPersonaCursor.getNombre());
		persona.setCantidadPago(tablaPersonaCursor.getCuantiaPago());
		persona.setPagado(tablaPersonaCursor.getPagado());
        persona.setRepartoPagado(tablaPersonaCursor.getHecho());
		return persona;
	}

}
