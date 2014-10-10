package com.jagusan.apatxas.sqlite.daos;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.tables.TablaApatxa;
import com.jagusan.apatxas.sqlite.tables.TablaApatxaPersonas;

public class ApatxaPersonasDAO {
	
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	private static final String[] COLUMNAS_APATXA_PERSONA = { TablaApatxa.COLUMNA_ID, TablaApatxa.COLUMNA_NOMBRE, TablaApatxa.COLUMNA_FECHA, 
													  TablaApatxa.COLUMNA_BOTE_INICIAL, TablaApatxa.COLUMNA_GASTO_TOTAL, TablaApatxa.COLUMNA_GASTO_PAGADO };
	

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}
	
	public Integer numeroPersonasApatxa(Long idApatxa){		
		return (int) DatabaseUtils.queryNumEntries(database, TablaApatxaPersonas.NOMBRE_TABLA, TablaApatxaPersonas.COLUMNA_ID_APATXA + " = "+idApatxa);		
	}
}
