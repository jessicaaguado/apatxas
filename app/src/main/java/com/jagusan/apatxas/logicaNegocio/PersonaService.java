package com.jagusan.apatxas.logicaNegocio;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jagusan.apatxas.sqlite.daos.GastoDAO;
import com.jagusan.apatxas.sqlite.daos.PersonaDAO;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;
import com.jagusan.apatxas.sqlite.modelView.PersonaListadoReparto;

public class PersonaService {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;

	private PersonaDAO personaDAO;
	private GastoDAO gastoDAO;

	public PersonaService(Context context) {
		dbHelper = new DatabaseHelper(context);
		personaDAO = new PersonaDAO();
		gastoDAO = new GastoDAO();
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		personaDAO.setDatabase(database);
		gastoDAO.setDatabase(database);
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

    public void crearPersonas(List<PersonaListado> personas, Long idApatxa) {
        open();
        for (PersonaListado persona: personas){
            personaDAO.crearPersona(persona.getNombre(), idApatxa);
        }
        close();
    }

	public List<PersonaListado> getTodasPersonasApatxa(Long idApatxa) {
		open();
		List<PersonaListado> listaPersonas = personaDAO.recuperarPersonasApatxa(idApatxa);
		close();
		return listaPersonas;
	}

	public void borrarPersona(Long idPersona) {
		open();
		personaDAO.borrarPersona(idPersona);
		gastoDAO.establecerComoNoPagadosGastosPagadosPorPersona(idPersona);
		close();
	}

	public Long recuperarIdPersonaConNombre(String nombre, Long idApatxa) {
		open();
		Long idPersona = personaDAO.recuperarIdPersonaPorNombre(nombre, idApatxa);
		close();
		return idPersona;
	}

    public void marcarPersonasEstadoReparto(List<PersonaListadoReparto> listaPersonas, boolean estadoPagado) {
        open();
        List<Long> idsPersonas = new ArrayList<Long>(listaPersonas.size());
        for (PersonaListadoReparto persona: listaPersonas){
            idsPersonas.add(persona.getId());
        }
        if (estadoPagado){
            personaDAO.marcarPersonasRepartoPagado(idsPersonas);
        }else{
            personaDAO.marcarPersonasRepartoPendiente(idsPersonas);
        }
        close();
    }

}
