package com.jagusan.apatxas.sqlite.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.cursorReader.ExtraerInformacionApatxaDeCursor;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;
import com.jagusan.apatxas.sqlite.tables.TablaApatxa;
import com.jagusan.apatxas.sqlite.tables.TablaGasto;
import com.jagusan.apatxas.sqlite.tables.TablaPersona;

public class ApatxaDAO {

	private SQLiteDatabase database;

	private static final String SQL_NUM_PERSONAS_PTES_APATXA = "(select count(*) from "+TablaPersona.NOMBRE_TABLA+" where "+TablaPersona.COLUMNA_ID_APATXA +" = "+TablaApatxa.COLUMNA_FULL_ID+" and ("+TablaPersona.COLUMNA_HECHO+" = 0 or "+TablaPersona.COLUMNA_HECHO+" is null))";
	
	private static final String[] COLUMNAS_APATXA = { TablaApatxa.COLUMNA_ID, TablaApatxa.COLUMNA_NOMBRE, TablaApatxa.COLUMNA_FECHA, 
													  TablaApatxa.COLUMNA_BOTE_INICIAL, TablaApatxa.COLUMNA_GASTO_TOTAL, TablaApatxa.COLUMNA_GASTO_PAGADO, TablaApatxa.COLUMNA_REPARTO_REALIZADO,SQL_NUM_PERSONAS_PTES_APATXA };

	private static final String ORDEN_APATXAS_DEFECTO = TablaApatxa.COLUMNA_FECHA + " DESC";
	
	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public Long nuevoApatxa(String nombre, Long fecha, Double boteInicial) {
		ContentValues values = new ContentValues();
		values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
		values.put(TablaApatxa.COLUMNA_FECHA, fecha);
		values.put(TablaApatxa.COLUMNA_BOTE_INICIAL, boteInicial);

		long insertId = database.insert(TablaApatxa.NOMBRE_TABLA, null, values);
		return insertId;
	}
	
	public void actualizarApatxa(Long id, String nombre, Long fecha, Double boteInicial) {
		ContentValues values = new ContentValues();
		values.put(TablaApatxa.COLUMNA_NOMBRE, nombre);
		values.put(TablaApatxa.COLUMNA_FECHA, fecha);
		values.put(TablaApatxa.COLUMNA_BOTE_INICIAL, boteInicial);

		String where = TablaApatxa.COLUMNA_ID + " = ?";
		String[] whereArgs = { String.valueOf(id) };
		database.update(TablaApatxa.NOMBRE_TABLA, values, where, whereArgs);
	}

	public void borrarApatxa(Long id) {
		database.delete(TablaApatxa.NOMBRE_TABLA, TablaApatxa.COLUMNA_ID + " = " + id, null);
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

	public void actualizarGastoTotalApatxa(Long idApatxa){
		String sqlCalculoGastoTotal = "select sum("+TablaGasto.COLUMNA_TOTAL+") from "+TablaGasto.NOMBRE_TABLA+" where "+TablaGasto.COLUMNA_ID_APATXA+" = "+idApatxa;
		String sqlActualizar = "update "+TablaApatxa.NOMBRE_TABLA+" set "+TablaApatxa.COLUMNA_GASTO_TOTAL+" = ("+sqlCalculoGastoTotal+") where "+TablaApatxa.COLUMNA_ID + " = "+idApatxa;
		database.execSQL(sqlActualizar);
	}

	public void cambiarEstadoRepartoApatxa(Long idApatxa, boolean repartoHecho) {
		Integer valorRepartoHecho = repartoHecho ? 1 : 0;
		String sqlActualizar = "update "+TablaApatxa.NOMBRE_TABLA+" set "+TablaApatxa.COLUMNA_REPARTO_REALIZADO +" = "+valorRepartoHecho+" where "+TablaApatxa.COLUMNA_ID+" = "+idApatxa;
		database.execSQL(sqlActualizar);
		
	}
	
	
}
