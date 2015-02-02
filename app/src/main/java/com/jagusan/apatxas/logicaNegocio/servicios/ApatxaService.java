package com.jagusan.apatxas.logicaNegocio.servicios;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jagusan.apatxas.logicaNegocio.daos.ApatxaDAO;
import com.jagusan.apatxas.logicaNegocio.daos.GastoDAO;
import com.jagusan.apatxas.logicaNegocio.daos.PersonaDAO;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.modelView.ApatxaDetalle;
import com.jagusan.apatxas.modelView.ApatxaListado;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;

public class ApatxaService {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;

	private ApatxaDAO apatxaDAO;
	private PersonaDAO personaDAO;
	private GastoDAO gastoDAO;

	private CalcularRepartoService calcularRepartoService;


	public ApatxaService(Context context) {
		dbHelper = new DatabaseHelper(context);
		apatxaDAO = new ApatxaDAO();
		personaDAO = new PersonaDAO();
		gastoDAO = new GastoDAO();
		calcularRepartoService = new CalcularRepartoService();
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
        apatxaDAO.setDatabase(database);
		personaDAO.setDatabase(database);
		gastoDAO.setDatabase(database);
	}

	public void close() {
        dbHelper.close();
	}

	public Long crearApatxa(String nombre, Long fechaInicio, Long fechaFin, Boolean soloUnDia, Double boteInicial, Boolean descontarBoteInicialDeGastoTotal) {
		open();
		Integer descontarBoteInicial = descontarBoteInicialDeGastoTotal ? 1 : 0;
		Long idApatxa = apatxaDAO.nuevoApatxa(nombre, fechaInicio, fechaFin, soloUnDia, boteInicial, descontarBoteInicial);
		close();
		return idApatxa;
	}

	public void actualizarApatxa(Long id, String nombre, Long fechaInicio, Long fechaFin, Boolean soloUnDia, Double boteInicial, Boolean descontarBoteInicialDeGastoTotal) {
		open();
		Integer descontarBoteInicial = descontarBoteInicialDeGastoTotal ? 1 : 0;
		apatxaDAO.actualizarApatxa(id, nombre, fechaInicio, fechaFin, soloUnDia, boteInicial, descontarBoteInicial);
		close();
	}

	public ApatxaDetalle getApatxaDetalle(Long id) {
		open();
		ApatxaDetalle apatxa = apatxaDAO.getApatxa(id);
		apatxa.setPersonas(personaDAO.recuperarPersonasApatxa(id));
		apatxa.setGastos(gastoDAO.recuperarGastosApatxa(id));
		close();
		return apatxa;
	}

	public List<ApatxaListado> getTodosApatxasListado() {
		open();
		List<ApatxaListado> listaApatxas = apatxaDAO.getTodosApatxas();
		close();
		return listaApatxas;
	}

	public void realizarRepartoSiNecesario(ApatxaDetalle apatxaDetalle) {
		if (!apatxaDetalle.repartoRealizado) {
			realizarReparto(apatxaDetalle);
		}
	}

	public void realizarReparto(ApatxaDetalle apatxaDetalle) {
		open();
		Double gastoProporcional = calcularRepartoService.calcularParteProporcional(apatxaDetalle);
		for (int i = 0; i < apatxaDetalle.getPersonas().size(); i++) {
			personaDAO.asociarPagoPersona(apatxaDetalle.getPersonas().get(i).id, gastoProporcional);
		}
		apatxaDAO.cambiarEstadoRepartoApatxa(apatxaDetalle.id, true);
		close();
	}

	public List<PersonaListadoReparto> getResultadoReparto(Long idApatxa) {
		open();
		List<PersonaListadoReparto> resultadoReparto = personaDAO.obtenerResultadoRepartoPersonas(idApatxa);
		close();
		return resultadoReparto;
	}

    public void borrarApatxas(List<ApatxaListado> listaApatxas){
        open();
        List<Long> idsApatxasBorrar = new ArrayList<Long>(listaApatxas.size());
        for (ApatxaListado apatxa:listaApatxas){
            idsApatxasBorrar.add(apatxa.id);
        }
        personaDAO.borrarPersonasDeApatxas(idsApatxasBorrar);
        gastoDAO.borrarGastosDeApatxas(idsApatxasBorrar);
        apatxaDAO.borrarApatxas(idsApatxasBorrar);
        close();
    }

    public List<String> recuperarTodosTitulos(){
        open();
        List<String> titulos = apatxaDAO.recuperarTodosTitulos();
        close();
        return titulos;
    }

}
