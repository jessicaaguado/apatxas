package com.jagusan.apatxas.sqlite.tables;

import android.provider.BaseColumns;

public class TablaApatxa implements BaseColumns {

    public static final String NOMBRE_TABLA = "apatxa";
    public static final String COLUMNA_ID = "id";
    public static final String COLUMNA_FULL_ID = NOMBRE_TABLA + "." + COLUMNA_ID;
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final String COLUMNA_FECHA_INICIO = "fechaInicio";
    public static final String COLUMNA_FECHA_FIN = "fechaFin";
    public static final String COLUMNA_SOLO_UN_DIA = "soloUnDia";
    public static final String COLUMNA_BOTE_INICIAL = "boteInicial";
    public static final String COLUMNA_DESCONTAR_BOTE_INICIAL = "descontarBoteInicial";
    public static final String COLUMNA_REPARTO_REALIZADO = "estaRealizadoReparto";

    public static final String CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "(" + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNA_NOMBRE + " TEXT,"
            + COLUMNA_FECHA_INICIO + " INTEGER,"+ COLUMNA_FECHA_FIN + " INTEGER,"+ COLUMNA_SOLO_UN_DIA + " INTEGER DEFAULT 0,"
            + COLUMNA_BOTE_INICIAL + " REAL DEFAULT 0," + COLUMNA_DESCONTAR_BOTE_INICIAL + " INTEGER DEFAULT 0," + COLUMNA_REPARTO_REALIZADO + " INTEGER DEFAULT 0" + ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;

}
