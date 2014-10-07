package com.jagusan.apatxas.sqlite.tables;

import android.provider.BaseColumns;

public class TablaPersona implements BaseColumns {
	
	public static final String NOMBRE_TABLA = "persona";
	public static final String COLUMNA_ID = "id";
	public static final String COLUMNA_NOMBRE = "nombre";
	
	public static final String CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "(" + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
			 																		 COLUMNA_NOMBRE + " TEXT" + ")";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
	
}
