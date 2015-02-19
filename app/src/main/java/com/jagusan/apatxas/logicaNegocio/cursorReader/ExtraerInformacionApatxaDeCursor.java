package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

import com.jagusan.apatxas.modelView.ApatxaListado;

public class ExtraerInformacionApatxaDeCursor {

    public static ApatxaListado extraer(Cursor cursor, ApatxaListado apatxaListado) {
        CursorTablaApatxa tablaApatxaCursor = new CursorTablaApatxa(cursor);
        apatxaListado.id = tablaApatxaCursor.getId();
        apatxaListado.nombre = tablaApatxaCursor.getNombre();
        apatxaListado.fechaInicio = tablaApatxaCursor.getFechaInicio();
        apatxaListado.fechaFin = tablaApatxaCursor.getFechaFin();
        apatxaListado.soloUnDia = tablaApatxaCursor.getSoloUnDia();
        apatxaListado.gastoTotal = tablaApatxaCursor.getGastoTotal();
        apatxaListado.repartoRealizado = tablaApatxaCursor.getRepartoHecho();
        apatxaListado.personasPendientesPagarCobrar = tablaApatxaCursor.getNumeroPersonasPendientesPagarCobrar();
        return apatxaListado;
    }

}
