package com.jagusan.apatxas.sqlite.tables;

import android.provider.BaseColumns;

public class TablaApatxa implements BaseColumns {

	public static final String NOMBRE_TABLA = "apatxa";
	public static final String COLUMNA_ID = "id";
	public static final String COLUMNA_NOMBRE = "nombre";
	public static final String COLUMNA_FECHA = "fecha";
	public static final String COLUMNA_BOTE_INICIAL = "boteInicial";
	// TODO hay que calcular estos campos
	// TODO siempre que se anada un gasto o pago, se guarde apatxa
	public static final String COLUMNA_BOTE_FINAL = "boteFinal";
	// TODO siempre que se anada un gasto, se guarde apatxa
	public static final String COLUMNA_TOTAL = "total";
	// TODO siempre que se anada un gasto o pago, se guarde apatxa
	public static final String COLUMNA_ESTADO = "estado"; 

			
	public static final String CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "(" + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
																					 COLUMNA_NOMBRE + " TEXT," + 
																					 COLUMNA_FECHA + " INTEGER," + 
																					 COLUMNA_BOTE_INICIAL + " REAL,"+
																					 COLUMNA_BOTE_FINAL + " REAL,"+
																					 COLUMNA_TOTAL + " REAL,"+
																					 COLUMNA_ESTADO + " INTEGER"+
																					 ")";		

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;


}
