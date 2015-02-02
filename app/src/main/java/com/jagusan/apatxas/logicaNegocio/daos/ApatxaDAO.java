package com.jagusan.apatxas.logicaNegocio.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.jagusan.apatxas.logicaNegocio.cursorReader.ExtraerInformacionApatxaDeCursor;
import com.jagusan.apatxas.modelView.ApatxaDetalle;
import com.jagusan.apatxas.modelView.ApatxaListado;
import com.jagusan.apatxas.sqlite.tables.TablaApatxa;
import com.jagusan.apatxas.sqlite.tables.TablaGasto;
import com.jagusan.apatxas.sqlite.tables.TablaPersona;

public class ApatxaDAO {

    private SQLiteDatabase database;

    private static final String SQL_NUM_PERSONAS_PTES_APATXA = "(select count(*) from " + TablaPersona.NOMBRE_TABLA + " where " + TablaPersona.COLUMNA_ID_APATXA + " = " + TablaApatxa.COLUMNA_FULL_ID
            + " and (" + TablaPersona.COLUMNA_HECHO + " = 0 or " + TablaPersona.COLUMNA_HECHO + " is null))";

    private static final String SQL_GASTO_TOTAL_APATXA = "(select sum(" + TablaGasto.COLUMNA_TOTAL + ") from " + TablaGasto.NOMBRE_TABLA + " where " + TablaGasto.COLUMNA_ID_APATXA + " = " + TablaApatxa.COLUMNA_FULL_ID + ")";

    private static final String[] COLUMNAS_APATXA = {TablaApatxa.COLUMNA_ID, TablaApatxa.COLUMNA_NOMBRE, TablaApatxa.COLUMNA_FECHA, TablaApatxa.COLUMNA_BOTE_INICIAL, TablaApatxa.COLUMNA_REPARTO_REALIZADO, SQL_NUM_PERSONAS_PTES_APATXA, TablaApatxa.COLUMNA_DESCONTAR_BOTE_INICIAL, SQL_GASTO_TOTAL_APATXA};

    private static final String ORDEN_APATXAS_DEFECTO = TablaApatxa.COLUMNA_FECHA + " DESC";

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public Long nuevoApatxa(String nombre, Long fecha, Double boteInicial, Integer descontarBoteInicialDeGastoTotal) {
        ContentValues values = new ContentValues();
        values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
        values.put(TablaApatxa.COLUMNA_FECHA, fecha);
        values.put(TablaApatxa.COLUMNA_BOTE_INICIAL, boteInicial);
        values.put(TablaApatxa.COLUMNA_DESCONTAR_BOTE_INICIAL, descontarBoteInicialDeGastoTotal);

        long insertId = database.insert(TablaApatxa.NOMBRE_TABLA, null, values);
        return insertId;
    }

    public void actualizarApatxa(Long id, String nombre, Long fecha, Double boteInicial, Integer descontarBoteInicialDeGastoTotal) {
        ContentValues values = new ContentValues();
        values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
        values.put(TablaApatxa.COLUMNA_FECHA, fecha);
        values.put(TablaApatxa.COLUMNA_BOTE_INICIAL, boteInicial);
        values.put(TablaApatxa.COLUMNA_DESCONTAR_BOTE_INICIAL, descontarBoteInicialDeGastoTotal);

        String where = TablaApatxa.COLUMNA_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        database.update(TablaApatxa.NOMBRE_TABLA, values, where, whereArgs);
    }

    public ApatxaDetalle getApatxa(Long id) {
        //TODO
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
        List<ApatxaListado> apatxas = new ArrayList<ApatxaListado>();
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
        List<String> titulos = new ArrayList<String>();
        String[] COLUMNA_NOMBRE = {TablaApatxa.COLUMNA_NOMBRE};
        Cursor cursor = database.query(true,TablaApatxa.NOMBRE_TABLA, COLUMNA_NOMBRE, null, null, null, null, TablaApatxa.COLUMNA_NOMBRE + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            titulos.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return titulos;
    }

}
