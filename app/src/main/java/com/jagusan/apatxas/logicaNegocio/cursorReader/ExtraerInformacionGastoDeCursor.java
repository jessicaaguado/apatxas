package com.jagusan.apatxas.logicaNegocio.cursorReader;

import android.database.Cursor;

import com.jagusan.apatxas.modelView.GastoApatxaListado;

public class ExtraerInformacionGastoDeCursor {

    public static GastoApatxaListado extraer(Cursor cursor, GastoApatxaListado gasto) {
        CursorTablaGasto tablaGastoCursor = new CursorTablaGasto(cursor);
        gasto.id = tablaGastoCursor.getId();
        gasto.concepto = tablaGastoCursor.getConcepto();
        gasto.total = tablaGastoCursor.getTotal();
        gasto.idPagadoPor = tablaGastoCursor.getIdPagador();
        gasto.pagadoPor = tablaGastoCursor.getNombrePagador();
        gasto.idContactoPersonaPagadoPor = tablaGastoCursor.getIdContactoPagador();
        return gasto;
    }

}
