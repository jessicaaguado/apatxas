package com.jagusan.apatxas.logicaNegocio;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.ApatxaDAO;
import com.jagusan.apatxas.sqlite.daos.ApatxaPersonasDAO;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;

public class ApatxaService {
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	private ApatxaDAO apatxaDAO;
	private ApatxaPersonasDAO apatxaPersonasDAO;
	
	public ApatxaService (Context context) {
		dbHelper = new DatabaseHelper(context);	
		apatxaDAO = new ApatxaDAO();
		apatxaPersonasDAO = new ApatxaPersonasDAO();
	}
	
	public void open() throws SQLException {
		Log.d("APATXAS", "LN: Abrir trans");
		database = dbHelper.getWritableDatabase();
		apatxaDAO.setDatabase(database);
		apatxaPersonasDAO.setDatabase(database);
	}

	public void close() {
		Log.d("APATXAS", "LN: Cerrar trans");
		dbHelper.close();		
	}
	
	public void crearApatxa(String nombre, Long fecha, Double boteInicial){
		open();
		apatxaDAO.nuevoApatxa(nombre, fecha, boteInicial);
		close();
	}
	
	public void actualizarApatxa(Long id, String nombre, Long fecha, Double boteInicial) {
		open();
		apatxaDAO.actualizarApatxa(id, nombre, fecha, boteInicial);
		close();
	}
	
	public void borrarApatxa(Long id){
		open();
		apatxaDAO.borrarApatxa(id);
		close();
	}
	
	public ApatxaDetalle getApatxaDetalle(Long id){
		open();
		ApatxaDetalle apatxa = apatxaDAO.getApatxa(id);
		Integer numeroPersonasApatxa = apatxaPersonasDAO.numeroPersonasApatxa(id);
		apatxa.setNumeroPersonas(numeroPersonasApatxa);
		close();
		return apatxa;
	}
	
	public List<ApatxaListado> getTodosApatxasListado(){
		open();
		List<ApatxaListado> listaApatxas = apatxaDAO.getTodosApatxas();
		close();
		return listaApatxas;
	}

}
