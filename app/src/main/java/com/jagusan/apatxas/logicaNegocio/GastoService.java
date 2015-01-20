package com.jagusan.apatxas.logicaNegocio;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jagusan.apatxas.sqlite.daos.GastoDAO;
import com.jagusan.apatxas.sqlite.daos.PersonaDAO;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;

import java.util.List;

public class GastoService {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private GastoDAO gastoDAO;
    private PersonaDAO personaDAO;


    public GastoService(Context context) {
        dbHelper = new DatabaseHelper(context);
        gastoDAO = new GastoDAO();
        personaDAO = new PersonaDAO();

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        gastoDAO.setDatabase(database);
        personaDAO.setDatabase(database);
    }

    public void close() {
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

    public void actualizarGasto(Long idGasto, String conceptoGasto, Double totalGasto, Long idPersona) {
        open();
        gastoDAO.actualizarGasto(idGasto, conceptoGasto, totalGasto, idPersona);
        close();
    }

    public void borrarGastos(List<GastoApatxaListado> listaGastos) {
        open();
        for (GastoApatxaListado gasto : listaGastos) {
            Long idGasto = gasto.getId();
            if (idGasto != null) {
                gastoDAO.borrarGasto(idGasto);
            }
        }
        close();
    }

    public void borrarGasto(Long idGasto) {
        open();
        gastoDAO.borrarGasto(idGasto);
        close();
    }

    public boolean hayGastosAsociadosA(Long id) {
        open();
        Boolean hayGastos = gastoDAO.hayGastosPagadosPorIdPersona(id);
        close();
        return hayGastos;
    }

    public void crearGastos(List<GastoApatxaListado> gastosAnadidos, Long idApatxa) {
        open();
        for (GastoApatxaListado gasto : gastosAnadidos) {
            Long idPersonaPagado = personaDAO.recuperarIdPersonaPorNombre(gasto.getPagadoPor(), idApatxa);
            gastoDAO.crearGasto(gasto.getConcepto(), gasto.getTotal(), idApatxa, idPersonaPagado);
        }
        close();
    }

    public void actualizarGastos(List<GastoApatxaListado> gastosModificados, Long idApatxa) {
        open();
        for (GastoApatxaListado gasto : gastosModificados) {
            Long idPersonaPagado = personaDAO.recuperarIdPersonaPorNombre(gasto.getPagadoPor(), idApatxa);
            gastoDAO.actualizarGasto(gasto.getId(), gasto.getConcepto(), gasto.getTotal(), idPersonaPagado);
        }
        close();
    }
}
