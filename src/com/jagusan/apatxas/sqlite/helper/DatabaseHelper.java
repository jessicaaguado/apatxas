package com.jagusan.apatxas.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jagusan.apatxas.sqlite.tables.TablaApatxa;
import com.jagusan.apatxas.sqlite.tables.TablaApatxaPersonas;
import com.jagusan.apatxas.sqlite.tables.TablaGasto;
import com.jagusan.apatxas.sqlite.tables.TablaPersona;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String NOMBRE_BASE_DATOS = "apatxas.db";
	private static final int VERSION_BASE_DATOS = 2;

	public DatabaseHelper(Context context) {
		super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("APATXAS", "BD: Creando la base de datos");
		db.execSQL(TablaApatxa.CREATE_TABLE);
		db.execSQL(TablaGasto.CREATE_TABLE);
		db.execSQL(TablaPersona.CREATE_TABLE);
		db.execSQL(TablaApatxaPersonas.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("APATXAS", "BD: Actualizando la base de datos de "+oldVersion+" a "+newVersion);
		db.execSQL(TablaApatxaPersonas.DROP_TABLE);
		db.execSQL(TablaGasto.DROP_TABLE);
		db.execSQL(TablaPersona.DROP_TABLE);
		db.execSQL(TablaApatxa.DROP_TABLE);
        onCreate(db);
	}
	

}
