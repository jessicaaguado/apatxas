package com.jagusan.apatxas.logicaNegocio.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jagusan.apatxas.logicaNegocio.cursorReader.ExtraerInformacionApatxaDeCursor;
import com.jagusan.apatxas.modelView.ApatxaDetalle;
import com.jagusan.apatxas.modelView.ApatxaListado;
import com.jagusan.apatxas.sqlite.tables.TablaApatxa;
import com.jagusan.apatxas.sqlite.tables.TablaGasto;
import com.jagusan.apatxas.sqlite.tables.TablaPersona;

import java.util.ArrayList;
import java.util.List;

public class ApatxaDAO {

    private SQLiteDatabase database;

    private static final String SQL_NUM_PERSONAS_PTES_APATXA = "(select count(*) from " + TablaPersona.NOMBRE_TABLA + " where " + TablaPersona.COLUMNA_ID_APATXA + " = " + TablaApatxa.COLUMNA_FULL_ID
            + " and (" + TablaPersona.COLUMNA_HECHO + " = 0 or " + TablaPersona.COLUMNA_HECHO + " is null))";

    private static final String SQL_GASTO_TOTAL_APATXA = "(select sum(" + TablaGasto.COLUMNA_TOTAL + ") from " + TablaGasto.NOMBRE_TABLA + " where " + TablaGasto.COLUMNA_ID_APATXA + " = " + TablaApatxa.COLUMNA_FULL_ID + ")";

    private static final String[] COLUMNAS_APATXA = {TablaApatxa.COLUMNA_ID, TablaApatxa.COLUMNA_NOMBRE,
            TablaApatxa.COLUMNA_FECHA_INICIO, TablaApatxa.COLUMNA_FECHA_FIN, TablaApatxa.COLUMNA_SOLO_UN_DIA,
            TablaApatxa.COLUMNA_REPARTO_REALIZADO, SQL_NUM_PERSONAS_PTES_APATXA,
            SQL_GASTO_TOTAL_APATXA};

    private static final String ORDEN_APATXAS_DEFECTO = TablaApatxa.COLUMNA_FECHA_INICIO + " DESC, "+TablaApatxa.COLUMNA_NOMBRE+" ASC";

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public Long nuevoApatxa(String nombre, Long fechaInicio, Long fechaFin, Boolean soloUnDia) {
        ContentValues values = new ContentValues();
        values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
        values.put(TablaApatxa.COLUMNA_FECHA_INICIO, fechaInicio);
        values.put(TablaApatxa.COLUMNA_FECHA_FIN, fechaFin);
        values.put(TablaApatxa.COLUMNA_SOLO_UN_DIA, soloUnDia);
        return database.insert(TablaApatxa.NOMBRE_TABLA, null, values);
    }

    public void actualizarApatxa(Long id, String nombre, Long fechaInicio, Long fechaFin, Boolean soloUnDia) {
        ContentValues values = new ContentValues();
        values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
        values.put(TablaApatxa.COLUMNA_FECHA_INICIO, fechaInicio);
        values.put(TablaApatxa.COLUMNA_FECHA_FIN, fechaFin);
        values.put(TablaApatxa.COLUMNA_SOLO_UN_DIA, soloUnDia);
        String where = TablaApatxa.COLUMNA_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        database.update(TablaApatxa.NOMBRE_TABLA, values, where, whereArgs);
    }

    public ApatxaDetalle getApatxa(Long id) {
       ApatxaDetalle apatxa = null;
        Cursor cursor = database.query(TablaApatxa.NOMBRE_TABLA, COLUMNAS_APATXA, TablaApatxa.COLUMNA_ID + "= " + id, null, null, null, ORDEN_APATXAS_DEFECTO);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            apatxa = new ApatxaDetalle();
            apatxa = (ApatxaDetalle) ExtraerInformacionApatxaDeCursor.extraer(cursor, apatxa);
        }
        cursor.close();
        return apatxa;
    }

    public List<ApatxaListado> getTodosApatxas() {
        List<ApatxaListado> apatxas = new ArrayList<>();
        Cursor cursor = database.query(TablaApatxa.NOMBRE_TABLA, COLUMNAS_APATXA, null, null, null, null, ORDEN_APATXAS_DEFECTO);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ApatxaListado apatxa = new ApatxaListado();
            apatxa = ExtraerInformacionApatxaDeCursor.extraer(cursor, apatxa);
            apatxas.add(apatxa);
            cursor.moveToNext();
        }
        cursor.close();
        return apatxas;
    }

    public void cambiarEstadoRepartoApatxa(Long idApatxa, boolean repartoHecho) {
        Integer valorRepartoHecho = repartoHecho ? 1 : 0;
        String sqlActualizar = "update " + TablaApatxa.NOMBRE_TABLA + " set " + TablaApatxa.COLUMNA_REPARTO_REALIZADO + " = " + valorRepartoHecho + " where " + TablaApatxa.COLUMNA_ID + " = "
                + idApatxa;
        database.execSQL(sqlActualizar);

    }

    public void borrarApatxas(List<Long> idsApatxasBorrar) {
        String sqlDeleteApatxas = "delete from " + TablaApatxa.NOMBRE_TABLA + " where "
                + TablaApatxa.COLUMNA_ID + " in (" + TextUtils.join(",", idsApatxasBorrar) + ")";
        database.execSQL(sqlDeleteApatxas);
    }

    public List<String> recuperarTodosTitulos() {
        List<String> titulos = new ArrayList<>();
        String[] COLUMNA_NOMBRE = {TablaApatxa.COLUMNA_NOMBRE};
        Cursor cursor = database.query(true, TablaApatxa.NOMBRE_TABLA, COLUMNA_NOMBRE, null, null, null, null, TablaApatxa.COLUMNA_NOMBRE + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            titulos.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return titulos;
    }

}
