package com.jagusan.apatxas.logicaNegocio;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jagusan.apatxas.sqlite.daos.PersonaDAO;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;

public class PersonaService {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;

	private PersonaDAO personaDAO;

	public PersonaService(Context context) {
		dbHelper = new DatabaseHelper(context);
		personaDAO = new PersonaDAO();
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		personaDAO.setDatabase(database);
	}

	public void close() {
		dbHelper.close();
	}

	public Long crearPersona(String nombre, Long idApatxa) {
		open();
		Long idPersona = personaDAO.crearPersona(nombre, idApatxa);
		close();
		return idPersona;
	}

	public List<PersonaListado> getTodasPersonasApatxa(Long idApatxa) {
		open();
		List<PersonaListado> listaPersonas = personaDAO.recuperarPersonasApatxa(idApatxa);
		close();
		return listaPersonas;
	}

}
