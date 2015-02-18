package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

import com.jagusan.apatxas.modelView.ContactoListado;

public class ExtraerInformacionContactoDeCursor {

	public static ContactoListado extraer(Cursor cursor, ContactoListado contactoListado) {
		CursorContacto contactoCursor = new CursorContacto(cursor);
        contactoListado.id = contactoCursor.getId();
        contactoListado.nombre = contactoCursor.getNombre();
        contactoListado.fotoURI = contactoCursor.getFotoUri();
		return contactoListado;
	}

}
