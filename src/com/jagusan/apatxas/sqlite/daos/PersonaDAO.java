package com.jagusan.apatxas.sqlite.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.cursorReader.ExtraerInformacionPersonaDeCursor;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;
import com.jagusan.apatxas.sqlite.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.sqlite.tables.TablaGasto;
import com.jagusan.apatxas.sqlite.tables.TablaPersona;

public class PersonaDAO {

	private static final String NOMBRE_TABLA_PERSONA = TablaPersona.NOMBRE_TABLA;

	private static final String[] COLUMNAS_PERSONA = { TablaPersona.COLUMNA_ID, TablaPersona.COLUMNA_NOMBRE, TablaPersona.COLUMNA_ID_APATXA, TablaPersona.COLUMNA_CUANTIA_PAGO,
			TablaPersona.COLUMNA_PAGADO };

	private static final String ORDEN_PERSONAS_DEFECTO = TablaPersona.COLUMNA_NOMBRE + " ASC";

	private SQLiteDatabase database;

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public Long crearPersona(String nombre, Long idApatxa) {
		ContentValues values = new ContentValues();
		values.put(TablaPersona.COLUMNA_NOMBRE, nombre);
		values.put(TablaPersona.COLUMNA_ID_APATXA, idApatxa);

		long insertId = database.insert(NOMBRE_TABLA_PERSONA, null, values);
		return insertId;
	}

	public void borrarPersona(Long id) {
		database.delete(NOMBRE_TABLA_PERSONA, TablaPersona.COLUMNA_ID + " = " + id, null);
	}

	public Long recuperarIdPersonaPorNombre(String nombre, Long idApatxa) {
		Long idPersona = null;
		Log.d("APATXAS", "DAO recuperarIdpersona " + nombre + " " + idApatxa);
		Cursor cursor = database.query(NOMBRE_TABLA_PERSONA, new String[] { TablaPersona.COLUMNA_ID }, TablaPersona.COLUMNA_ID_APATXA + " = " + idApatxa + " and " + TablaPersona.COLUMNA_NOMBRE
				+ " = '" + nombre + "'", null, null, null, ORDEN_PERSONAS_DEFECTO);
		Log.d("APATXAS", "DAO CURSOR " + cursor);
		if (cursor.moveToFirst()){			
			idPersona = cursor.getLong(0);
		}
		cursor.close();
		return idPersona;
	}

	public List<PersonaListado> recuperarPersonasApatxa(Long idApatxa) {
		List<PersonaListado> personas = new ArrayList<PersonaListado>();
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
		List<PersonaListadoReparto> personas = new ArrayList<PersonaListadoReparto>();
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
		String sqlActualizar = "update " + TablaPersona.NOMBRE_TABLA + " set " + TablaPersona.COLUMNA_CUANTIA_PAGO + " = (" + cuantia + " - " + subselectGastosPagados + ") where "
				+ TablaPersona.COLUMNA_ID + " = " + idPersona;
		database.execSQL(sqlActualizar);
	}

}
