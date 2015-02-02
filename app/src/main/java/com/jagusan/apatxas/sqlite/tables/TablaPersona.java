package com.jagusan.apatxas.sqlite.tables;

import android.provider.BaseColumns;

public class TablaPersona implements BaseColumns {

    public static final String NOMBRE_TABLA = "persona";

    public static final String COLUMNA_ID = "id";
    public static final String COLUMNA_FULL_ID = NOMBRE_TABLA + "." + COLUMNA_ID;
    public static final String COLUMNA_NOMBRE = "nombre";

    // una persona no existe si no es asociada a un apatxa
    public static final String COLUMNA_ID_APATXA = "idApatxa";
    // una vez hecho el reparto
    public static final String COLUMNA_CUANTIA_PAGO = "cantidadPago";
    public static final String COLUMNA_PAGADO = "pagado";
    public static final String COLUMNA_HECHO = "hecho";

    public static final String COLUMNA_ID_CONTACTO = "contactoId";
    public static final String COLUMNA_FOTO_CONTACTO = "contactoFoto";

    public static final String CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "(" + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNA_NOMBRE + " TEXT," + COLUMNA_ID_APATXA + " INTEGER,"
            + COLUMNA_ID_CONTACTO + " INTEGER, " + COLUMNA_FOTO_CONTACTO + " TEXT,"
            + COLUMNA_CUANTIA_PAGO + " REAL DEFAULT 0," + COLUMNA_PAGADO + " REAL DEFAULT 0," + COLUMNA_HECHO + " INTEGER DEFAULT 0)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;

}

