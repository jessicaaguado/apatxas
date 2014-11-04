package com.jagusan.apatxas.logicaNegocio;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.ApatxaDAO;
import com.jagusan.apatxas.sqlite.daos.GastoDAO;
import com.jagusan.apatxas.sqlite.daos.PersonaDAO;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;

public class ApatxaService {
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	private ApatxaDAO apatxaDAO;
	private PersonaDAO personaDAO;
	private GastoDAO gastoDAO;
	
	public ApatxaService (Context context) {
		dbHelper = new DatabaseHelper(context);	
		apatxaDAO = new ApatxaDAO();
		personaDAO = new PersonaDAO();
		gastoDAO = new GastoDAO();
	}
	
	public void open() throws SQLException {
		Log.d("APATXAS", "LN: Abrir trans");
		database = dbHelper.getWritableDatabase();
		apatxaDAO.setDatabase(database);
		personaDAO.setDatabase(database);
		gastoDAO.setDatabase(database);
	}

	public void close() {
		Log.d("APATXAS", "LN: Cerrar trans");
		dbHelper.close();		
	}
	
	public Long crearApatxa(String nombre, Long fecha, Double boteInicial){
		open();
		Long idApatxa = apatxaDAO.nuevoApatxa(nombre, fecha, boteInicial);
		close();
		return idApatxa;
	}
	
	public void actualizarApatxa(Long id, String nombre, Long fecha, Double boteInicial) {
		open();
		apatxaDAO.actualizarApatxa(id, nombre, fecha, boteInicial);
		close();
	}
	
	public void actualizarGastoTotalApatxa(Long id){
		open();
		apatxaDAO.actualizarGastoTotalApatxa(id);
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
		apatxa.setPersonas(personaDAO.recuperarPersonasApatxa(id));
		apatxa.setGastos(gastoDAO.recuperarGastosApatxa(id));
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
