package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;

public class ExtraerInformacionPersonaDeCursor {

    public static PersonaListado extraer(Cursor cursor, PersonaListado persona) {
        CursorTablaPersona tablaPersonaCursor = new CursorTablaPersona(cursor);
        persona.id = tablaPersonaCursor.getId();
        persona.nombre = tablaPersonaCursor.getNombre();
        persona.idContacto = tablaPersonaCursor.getIdContacto();
        persona.uriFoto = tablaPersonaCursor.getFotoContacto();
        return persona;
    }

    public static PersonaListadoReparto extraer(Cursor cursor, PersonaListadoReparto persona) {
        CursorTablaPersona tablaPersonaCursor = new CursorTablaPersona(cursor);
        persona.id = tablaPersonaCursor.getId();
        persona.nombre = tablaPersonaCursor.getNombre();
        persona.idContacto = tablaPersonaCursor.getIdContacto();
        persona.uriFoto = tablaPersonaCursor.getFotoContacto();
        persona.cantidadPago = tablaPersonaCursor.getCuantiaPago();
        persona.repartoPagado = tablaPersonaCursor.getHecho();
        return persona;
    }

}
