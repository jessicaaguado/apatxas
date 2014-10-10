package com.jagusan.apatxas.sqlite.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.cursorReader.ExtraerInformacionApatxaDeCursor;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;
import com.jagusan.apatxas.sqlite.tables.TablaApatxa;

public class ApatxaDAO {

	private SQLiteDatabase database;

	private static final String[] COLUMNAS_APATXA = { TablaApatxa.COLUMNA_ID, TablaApatxa.COLUMNA_NOMBRE, TablaApatxa.COLUMNA_FECHA, 
													  TablaApatxa.COLUMNA_BOTE_INICIAL, TablaApatxa.COLUMNA_GASTO_TOTAL, TablaApatxa.COLUMNA_GASTO_PAGADO };

	private static final String ORDEN_APATXAS_DEFECTO = TablaApatxa.COLUMNA_FECHA + " DESC";

	public ApatxaDAO(SQLiteDatabase dataBase) {
		this.database = dataBase;
	}

	public void nuevoApatxa(String nombre, Long fecha, Double boteInicial) {
		ContentValues values = new ContentValues();
		values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
		values.put(TablaApatxa.COLUMNA_FECHA, fecha);
		values.put(TablaApatxa.COLUMNA_BOTE_INICIAL, boteInicial);

		long insertId = database.insert(TablaApatxa.NOMBRE_TABLA, null, values);
		Log.d("APATXAS", "DAO: Creado apatxa con id " + insertId);
	}
	
	public void actualizarApatxa(Long id, String nombre, Long fecha, Double boteInicial) {
		ContentValues values = new ContentValues();
		values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
		values.put(TablaApatxa.COLUMNA_FECHA, fecha);
		values.put(TablaApatxa.COLUMNA_BOTE_INICIAL, boteInicial);

		String where = TablaApatxa.COLUMNA_ID + " = ?";
		String[] whereArgs = { String.valueOf(id) };
		database.update(TablaApatxa.NOMBRE_TABLA, values, where, whereArgs);
		Log.d("APATXAS", "DAO: Actualizado apatxa con id " + id);
	}

	public void borrarApatxa(Long id) {
		database.delete(TablaApatxa.NOMBRE_TABLA, TablaApatxa.COLUMNA_ID + " = " + id, null);
		Log.d("APATXAS", "DAO: Borrado apatxa con id " + id);

	}
	
	public ApatxaDetalle getApatxa(Long id){
		ApatxaDetalle apatxa = null;
		Cursor cursor = database.query(TablaApatxa.NOMBRE_TABLA, COLUMNAS_APATXA, TablaApatxa.COLUMNA_ID + "= "+id, null, null, null, ORDEN_APATXAS_DEFECTO);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			apatxa = new ApatxaDetalle();
			apatxa = (ApatxaDetalle) ExtraerInformacionApatxaDeCursor.extraer(cursor, apatxa);			
			cursor.moveToNext();
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


	
	
}
