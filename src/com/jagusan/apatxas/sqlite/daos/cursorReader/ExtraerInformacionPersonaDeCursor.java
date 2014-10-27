package com.jagusan.apatxas.sqlite.daos.cursorReader;

import android.database.Cursor;

import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;

public class ExtraerInformacionPersonaDeCursor {

	public static PersonaListado extraer(Cursor cursor, PersonaListado persona) {		
		CursorTablaPersona tablaPersonaCursor = new CursorTablaPersona(cursor);
		persona.setId(tablaPersonaCursor.getId());
		persona.setNombre(tablaPersonaCursor.getNombre());		
		return persona;
	}
	
	

}
