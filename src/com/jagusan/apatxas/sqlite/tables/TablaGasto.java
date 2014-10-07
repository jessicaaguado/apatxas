package com.jagusan.apatxas.sqlite.tables;

import android.provider.BaseColumns;

public class TablaGasto implements BaseColumns {

	public static final String NOMBRE_TABLA = "gasto";
	public static final String COLUMNA_ID = "id";
	public static final String COLUMNA_NOMBRE = "nombre";
	public static final String COLUMNA_TOTAL = "total";
	public static final String COLUMNA_ID_PAGADO_POR = "idPersona";
	public static final String COLUMNA_ID_APATXA = "idApatxa";

	public static final String CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "(" + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
																					 COLUMNA_NOMBRE + " TEXT," + 
																					 COLUMNA_TOTAL + " REAL," + 
																					 COLUMNA_ID_PAGADO_POR + " INTEGER," +																					  
																					 COLUMNA_ID_APATXA + " INTEGER" + ")";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
