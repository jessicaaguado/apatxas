package com.jagusan.apatxas.sqlite.daos;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.tables.TablaApatxa;
import com.jagusan.apatxas.sqlite.tables.TablaApatxaPersonas;

public class ApatxaPersonasDAO {
	
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	private static final String[] COLUMNAS_APATXA_PERSONA = { TablaApatxa.COLUMNA_ID, TablaApatxa.COLUMNA_NOMBRE, TablaApatxa.COLUMNA_FECHA, 
													  TablaApatxa.COLUMNA_BOTE_INICIAL, TablaApatxa.COLUMNA_GASTO_TOTAL, TablaApatxa.COLUMNA_GASTO_PAGADO };
	

	public ApatxaPersonasDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		Log.d("APATXAS", "DAO: Open");
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		Log.d("APATXAS", "DAO: Close");
		dbHelper.close();
	}
	
	public Integer numeroPersonasApatxa(Long idApatxa){
		Integer numeroPersonas = 0;
		DatabaseUtils.queryNumEntries(database, TablaApatxaPersonas.NOMBRE_TABLA, TablaApatxaPersonas.COLUMNA_ID_APATXA + " = "+idApatxa);
		return numeroPersonas;
	}
}
