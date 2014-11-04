package com.jagusan.apatxas.logicaNegocio;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.GastoDAO;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;

public class GastoService {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;

	private GastoDAO gastoDAO;

	public GastoService(Context context) {
		dbHelper = new DatabaseHelper(context);
		gastoDAO = new GastoDAO();
	}

	public void open() throws SQLException {
		Log.d("APATXAS", "LN: Abrir trans");
		database = dbHelper.getWritableDatabase();
		gastoDAO.setDatabase(database);
	}

	public void close() {
		Log.d("APATXAS", "LN: Cerrar trans");
		dbHelper.close();
	}

	public Long crearGasto(String concepto, Double total, Long idApatxa, Long idPersona) {
		open();
		Long idGasto = gastoDAO.crearGasto(concepto, total, idApatxa, idPersona);
		close();
		return idGasto;
	}

	public List<GastoApatxaListado> getTodosGastosApatxa(Long idApatxa) {
		open();
		List<GastoApatxaListado> listaGastos = gastoDAO.recuperarGastosApatxa(idApatxa);
		close();
		return listaGastos;
	}

}
