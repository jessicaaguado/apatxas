package com.jagusan.apatxas.sqlite.tables;

import android.provider.BaseColumns;

public class TablaApatxaPersonas implements BaseColumns {
	
	public static final String NOMBRE_TABLA = "apatxa_personas";
	public static final String COLUMNA_ID_APATXA = "idApatxa";
	public static final String COLUMNA_ID_PERSONA = "idPersona";
	public static final String COLUMNA_CUANTIA_PAGO = "cantidadPago";
	public static final String COLUMNA_ESTADO_PAGO = "estadoPago";
	public static final String COLUMNA_ESTA_EXENTO = "exento";
	
	public static final String CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "(" + COLUMNA_ID_APATXA + " INTEGER," + 
																					 COLUMNA_ID_PERSONA + " INTEGER," + 
																					 COLUMNA_CUANTIA_PAGO + " REAL," + 
																					 COLUMNA_ESTADO_PAGO + " INTEGER," +																					  
																					 COLUMNA_ESTA_EXENTO + " INTEGER" + ")";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
