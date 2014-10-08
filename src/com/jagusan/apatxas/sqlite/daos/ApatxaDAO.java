package com.jagusan.apatxas.sqlite.daos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;
import com.jagusan.apatxas.sqlite.tables.TablaApatxa;

public class ApatxaDAO {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	private static final String[] COLUMNAS_APATXA_LISTADO = { TablaApatxa.COLUMNA_ID, TablaApatxa.COLUMNA_NOMBRE, TablaApatxa.COLUMNA_FECHA, TablaApatxa.COLUMNA_BOTE_INICIAL };
	private static final String ORDEN_APATXAS_DEFECTO = TablaApatxa.COLUMNA_FECHA + " DESC";

	public ApatxaDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void nuevoApatxa(String nombre, Long fecha, Double boteInicial) {
		ContentValues values = new ContentValues();
		values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
		values.put(TablaApatxa.COLUMNA_FECHA, fecha);
		values.put(TablaApatxa.COLUMNA_BOTE_INICIAL, boteInicial);

		long insertId = database.insert(TablaApatxa.NOMBRE_TABLA, null, values);
		Log.d("APATXAS", "DAO: Creado apatxa con id " + insertId);
	}

	public void borrarApatxa(Long id) {
		database.delete(TablaApatxa.NOMBRE_TABLA, TablaApatxa.COLUMNA_ID + " = " + id, null);
		Log.d("APATXAS", "DAO: Borrado apatxa con id " + id);

	}

	public List<ApatxaListado> getTodosApatxas() {
		List<ApatxaListado> apatxas = new ArrayList<ApatxaListado>();
		Cursor cursor = database.query(TablaApatxa.NOMBRE_TABLA, COLUMNAS_APATXA_LISTADO, null, null, null, null, ORDEN_APATXAS_DEFECTO);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ApatxaListado apatxaListado = cursorToApatxaListado(cursor);
			apatxas.add(apatxaListado);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return apatxas;
	}

	private ApatxaListado cursorToApatxaListado(Cursor cursor) {
		ApatxaListado apatxaListado = new ApatxaListado();
		int i = 0;
		apatxaListado.setId(cursor.getLong(i++));
		apatxaListado.setNombre(cursor.getString(i++));
		Long fecha = cursor.getLong(i++);
		if (fecha != null){			
			apatxaListado.setFecha(new Date(fecha));
		}		
		return apatxaListado;
	}
}
