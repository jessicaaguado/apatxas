package com.jagusan.apatxas.sqlite.tables;

import android.provider.BaseColumns;

public class TablaApatxa implements BaseColumns {

	public static final String NOMBRE_TABLA = "apatxa";
	public static final String COLUMNA_ID = "id";
	public static final String COLUMNA_FULL_ID = NOMBRE_TABLA + "." + COLUMNA_ID;
	public static final String COLUMNA_NOMBRE = "nombre";
	public static final String COLUMNA_FECHA = "fecha";
	public static final String COLUMNA_BOTE_INICIAL = "boteInicial";
	public static final String COLUMNA_REPARTO_REALIZADO = "estaRealizadoReparto";
	// TODO hay que calcular estos campos
	// TODO siempre que se anada un gasto, se guarde apatxa
	public static final String COLUMNA_GASTO_TOTAL = "gastoTotal";
	// TODO siempre que se anada un pago, se guarde apatxa
	public static final String COLUMNA_GASTO_PAGADO = "gastoPagado";

	public static final String CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "(" + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNA_NOMBRE + " TEXT," + COLUMNA_FECHA + " INTEGER,"
			+ COLUMNA_BOTE_INICIAL + " REAL DEFAULT 0," + COLUMNA_REPARTO_REALIZADO + " INTEGER DEFAULT 0," + COLUMNA_GASTO_TOTAL + " REAL DEFAULT 0," + COLUMNA_GASTO_PAGADO + " REAL DEFAULT 0" + ")";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;

}
