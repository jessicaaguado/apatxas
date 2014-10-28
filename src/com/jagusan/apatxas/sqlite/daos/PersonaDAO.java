package com.jagusan.apatxas.sqlite.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.cursorReader.ExtraerInformacionPersonaDeCursor;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;
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
		Log.d("APATXAS", "DAO: Creada persona con id " + insertId);
		return insertId;
	}

	public void borrarPersona(Long id) {
		database.delete(NOMBRE_TABLA_PERSONA, TablaPersona.COLUMNA_ID + " = " + id, null);
		Log.d("APATXAS", "DAO: Borrada persona con id " + id);
	}

	public Long recuperarIdPersonaPorNombre(String nombre) {
		Long idPersona = null;
		Cursor cursor = database.query(NOMBRE_TABLA_PERSONA, new String[] { TablaPersona.COLUMNA_ID }, TablaPersona.COLUMNA_NOMBRE + "= " + nombre, null, null, null, ORDEN_PERSONAS_DEFECTO);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			idPersona = cursor.getLong(0);
		}
		cursor.close();
		return idPersona;
	}

	public List<PersonaListado> recuperarPersonasApatxa(Long idApatxa) {
		List<PersonaListado> personas = new ArrayList<PersonaListado>();
		Cursor cursor = database.query(NOMBRE_TABLA_PERSONA, COLUMNAS_PERSONA, null, null, null, null, ORDEN_PERSONAS_DEFECTO);

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

	public void asociarPagoPersona(Integer idPersona, Double cuantia) {

	}

}