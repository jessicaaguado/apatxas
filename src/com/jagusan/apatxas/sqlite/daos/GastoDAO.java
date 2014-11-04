package com.jagusan.apatxas.sqlite.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.cursorReader.ExtraerInformacionGastoDeCursor;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;
import com.jagusan.apatxas.sqlite.tables.TablaGasto;
import com.jagusan.apatxas.sqlite.tables.TablaPersona;

public class GastoDAO {

	private static final String NOMBRE_TABLA_GASTO = TablaGasto.NOMBRE_TABLA;

	private static final String[] COLUMNAS_GASTO = { TablaGasto.COLUMNA_FULL_ID, TablaGasto.COLUMNA_CONCEPTO, TablaGasto.COLUMNA_TOTAL, TablaGasto.COLUMNA_ID_PAGADO_POR, TablaPersona.COLUMNA_NOMBRE };

	private static final String ORDEN_GASTOS_DEFECTO = TablaGasto.COLUMNA_CONCEPTO + " ASC";

	private SQLiteDatabase database;

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public Long crearGasto(String concepto, Double total, Long idApatxa, Long idPersona) {
		ContentValues values = new ContentValues();
		values.put(TablaGasto.COLUMNA_CONCEPTO, concepto);
		values.put(TablaGasto.COLUMNA_TOTAL, total);
		values.put(TablaGasto.COLUMNA_ID_PAGADO_POR, idPersona);
		values.put(TablaGasto.COLUMNA_ID_APATXA, idApatxa);

		long insertId = database.insert(NOMBRE_TABLA_GASTO, null, values);
		Log.d("APATXAS", "DAO: Creado gasto con id " + insertId);
		return insertId;
	}

	public void borrarGasto(Long id) {
		database.delete(NOMBRE_TABLA_GASTO, TablaGasto.COLUMNA_ID + " = " + id, null);
		Log.d("APATXAS", "DAO: Borrado gasto con id " + id);
	}

	public List<GastoApatxaListado> recuperarGastosApatxa(Long idApatxa) {
		List<GastoApatxaListado> gastos = new ArrayList<GastoApatxaListado>();

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();		
		queryBuilder.setTables(TablaGasto.NOMBRE_TABLA+" left outer join "+TablaPersona.NOMBRE_TABLA+ " on "+TablaGasto.COLUMNA_ID_PAGADO_POR+"="+TablaPersona.COLUMNA_FULL_ID);
		Cursor cursor = queryBuilder.query(database, COLUMNAS_GASTO, TablaGasto.NOMBRE_TABLA+"."+TablaGasto.COLUMNA_ID_APATXA + " = "+idApatxa, null, null, null, ORDEN_GASTOS_DEFECTO);		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GastoApatxaListado gasto = new GastoApatxaListado();
			gasto = ExtraerInformacionGastoDeCursor.extraer(cursor, gasto);
			gastos.add(gasto);
			cursor.moveToNext();
		}
		cursor.close();
		return gastos;
	}

}
