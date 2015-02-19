package com.jagusan.apatxas.logicaNegocio.servicios;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jagusan.apatxas.logicaNegocio.daos.GastoDAO;
import com.jagusan.apatxas.logicaNegocio.daos.PersonaDAO;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;

import java.util.List;

public class GastoService {

    private DatabaseHelper dbHelper;

    private GastoDAO gastoDAO;
    private PersonaDAO personaDAO;


    public GastoService(Context context) {
        dbHelper = new DatabaseHelper(context);
        gastoDAO = new GastoDAO();
        personaDAO = new PersonaDAO();

    }

    public void open() throws SQLException {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        gastoDAO.setDatabase(database);
        personaDAO.setDatabase(database);
    }

    public void close() {
        dbHelper.close();
    }

    public void crearGasto(String concepto, Double total, Long idApatxa, Long idPersona) {
        open();
        gastoDAO.crearGasto(concepto, total, idApatxa, idPersona);
        close();
    }

    public void actualizarGasto(Long idGasto, String conceptoGasto, Double totalGasto, Long idPersona) {
        open();
        gastoDAO.actualizarGasto(idGasto, conceptoGasto, totalGasto, idPersona);
        close();
    }

    public void borrarGastos(List<GastoApatxaListado> listaGastos) {
        open();
        for (GastoApatxaListado gasto : listaGastos) {
            Long idGasto = gasto.id;
            if (idGasto != null) {
                gastoDAO.borrarGasto(idGasto);
            }
        }
        close();
    }


    public void crearGastos(List<GastoApatxaListado> gastosAnadidos, Long idApatxa) {
        open();
        for (GastoApatxaListado gasto : gastosAnadidos) {
            Long idPersonaPagado = personaDAO.recuperarIdPersonaPorIdContacto(gasto.idContactoPersonaPagadoPor, idApatxa);
            gastoDAO.crearGasto(gasto.concepto, gasto.total, idApatxa, idPersonaPagado);
        }
        close();
    }

    public void actualizarGastos(List<GastoApatxaListado> gastosModificados, Long idApatxa) {
        open();
        for (GastoApatxaListado gasto : gastosModificados) {
            Long idPersonaPagado = personaDAO.recuperarIdPersonaPorIdContacto(gasto.idContactoPersonaPagadoPor, idApatxa);
            gastoDAO.actualizarGasto(gasto.id, gasto.concepto, gasto.total, idPersonaPagado);
        }
        close();
    }

    public List<String> recuperarTodosConceptos() {
        open();
        List<String> conceptos = gastoDAO.recuperarTodosConceptos();
        close();
        return conceptos;
    }
}
