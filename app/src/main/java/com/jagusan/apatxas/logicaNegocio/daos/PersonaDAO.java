package com.jagusan.apatxas.logicaNegocio.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jagusan.apatxas.logicaNegocio.cursorReader.ExtraerInformacionPersonaDeCursor;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.sqlite.tables.TablaGasto;
import com.jagusan.apatxas.sqlite.tables.TablaPersona;

import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    private static final String NOMBRE_TABLA_PERSONA = TablaPersona.NOMBRE_TABLA;

    private static final String[] COLUMNAS_PERSONA = {TablaPersona.COLUMNA_ID, TablaPersona.COLUMNA_NOMBRE, TablaPersona.COLUMNA_ID_APATXA, TablaPersona.COLUMNA_CUANTIA_PAGO, TablaPersona.COLUMNA_HECHO, TablaPersona.COLUMNA_ID_CONTACTO, TablaPersona.COLUMNA_FOTO_CONTACTO};

    private static final String ORDEN_PERSONAS_DEFECTO = TablaPersona.COLUMNA_NOMBRE + " ASC";

    private SQLiteDatabase database;

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void crearPersona(String nombre, Long idApatxa, Long idContacto, String fotoContacto) {
        ContentValues values = new ContentValues();
        values.put(TablaPersona.COLUMNA_NOMBRE, nombre);
        values.put(TablaPersona.COLUMNA_ID_APATXA, idApatxa);
        if (fotoContacto != null) {
            values.put(TablaPersona.COLUMNA_FOTO_CONTACTO, fotoContacto);
        }
        values.put(TablaPersona.COLUMNA_ID_CONTACTO, idContacto);

        database.insert(NOMBRE_TABLA_PERSONA, null, values);
    }

    public void borrarPersona(Long id) {
        database.delete(NOMBRE_TABLA_PERSONA, TablaPersona.COLUMNA_ID + " = " + id, null);
    }

    public void borrarPersonasDeApatxas(List<Long> idsApatxas) {
        String sqlDeletePersonas = "delete from " + TablaPersona.NOMBRE_TABLA + " where "
                + TablaPersona.COLUMNA_ID_APATXA + " in (" + TextUtils.join(",", idsApatxas) + ")";
        database.execSQL(sqlDeletePersonas);
    }


    public List<PersonaListado> recuperarPersonasApatxa(Long idApatxa) {
        List<PersonaListado> personas = new ArrayList<>();
        Cursor cursor = database.query(NOMBRE_TABLA_PERSONA, COLUMNAS_PERSONA, TablaPersona.COLUMNA_ID_APATXA + " = " + idApatxa, null, null, null, ORDEN_PERSONAS_DEFECTO);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PersonaListado persona = new PersonaListado();
            persona = ExtraerInformacionPersonaDeCursor.extraer(cursor, persona);
            personas.add(persona);
            cursor.moveToNext();
        }
        cursor.close();
        return personas;

    }

    public List<PersonaListadoReparto> obtenerResultadoRepartoPersonas(Long idApatxa) {
        List<PersonaListadoReparto> personas = new ArrayList<>();
        Cursor cursor = database.query(NOMBRE_TABLA_PERSONA, COLUMNAS_PERSONA, TablaPersona.COLUMNA_ID_APATXA + " = " + idApatxa, null, null, null, ORDEN_PERSONAS_DEFECTO);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PersonaListadoReparto persona = new PersonaListadoReparto();
            persona = ExtraerInformacionPersonaDeCursor.extraer(cursor, persona);
            personas.add(persona);
            cursor.moveToNext();
        }
        cursor.close();
        return personas;
    }

    public void asociarPagoPersona(Long idPersona, Double cuantia) {
        String subselectGastosPagados = "ifnull((select sum(" + TablaGasto.COLUMNA_TOTAL + ") from " + TablaGasto.NOMBRE_TABLA + " where " + TablaGasto.COLUMNA_ID_PAGADO_POR + " = " + idPersona
                + "),0)";
        String sqlActualizar = "update " + TablaPersona.NOMBRE_TABLA + " set " + TablaPersona.COLUMNA_CUANTIA_PAGO + " = (" + cuantia + " - " + subselectGastosPagados + "), " + TablaPersona.COLUMNA_HECHO + " = 0 where "
                + TablaPersona.COLUMNA_ID + " = " + idPersona;

        database.execSQL(sqlActualizar);
    }

    public void marcarPersonasRepartoPagado(List<Long> idsPersonas) {
        String sqlActualizar = "update " + TablaPersona.NOMBRE_TABLA + " set " + TablaPersona.COLUMNA_HECHO + " = 1 where "
                + TablaPersona.COLUMNA_ID + " in (" + TextUtils.join(",", idsPersonas) + ")";
        database.execSQL(sqlActualizar);
    }

    public void marcarPersonasRepartoPendiente(List<Long> idsPersonas) {
        String sqlActualizar = "update " + TablaPersona.NOMBRE_TABLA + " set " + TablaPersona.COLUMNA_HECHO + " = 0 where "
                + TablaPersona.COLUMNA_ID + " in (" + TextUtils.join(",", idsPersonas) + ")";
        database.execSQL(sqlActualizar);
    }

    public Long recuperarIdPersonaPorIdContacto(Long idContacto, Long idApatxa) {
        Long idPersona = null;
        Cursor cursor = database.query(NOMBRE_TABLA_PERSONA, new String[]{TablaPersona.COLUMNA_ID}, TablaPersona.COLUMNA_ID_APATXA + " = " + idApatxa + " and " + TablaPersona.COLUMNA_ID_CONTACTO
                + " = " + idContacto + "", null, null, null, ORDEN_PERSONAS_DEFECTO);
        if (cursor.moveToFirst()) {
            idPersona = cursor.getLong(0);
        }
        cursor.close();
        return idPersona;
    }

    public void actualizarMiNombre(String nuevoNombre) {
        String sqlActualizar = "update " + TablaPersona.NOMBRE_TABLA + " set " + TablaPersona.COLUMNA_NOMBRE + " = '" + nuevoNombre + "' where " + TablaPersona.COLUMNA_ID_CONTACTO + " = " + Long.MIN_VALUE;
        database.execSQL(sqlActualizar);
    }
}
