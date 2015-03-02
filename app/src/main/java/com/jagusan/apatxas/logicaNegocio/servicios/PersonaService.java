package com.jagusan.apatxas.logicaNegocio.servicios;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.logicaNegocio.daos.GastoDAO;
import com.jagusan.apatxas.logicaNegocio.daos.PersonaDAO;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.sqlite.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class PersonaService {

    private DatabaseHelper dbHelper;

    private PersonaDAO personaDAO;
    private GastoDAO gastoDAO;
    private Context context;

    public PersonaService(Context context) {
        dbHelper = new DatabaseHelper(context);
        personaDAO = new PersonaDAO();
        gastoDAO = new GastoDAO();
        this.context = context;
    }

    public void open() throws SQLException {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        personaDAO.setDatabase(database);
        gastoDAO.setDatabase(database);
    }

    public void close() {
        dbHelper.close();
    }


    public void crearPersonas(List<PersonaListado> personas, Long idApatxa) {
        open();
        for (PersonaListado persona : personas) {
            personaDAO.crearPersona(persona.nombre, idApatxa, persona.idContacto, persona.uriFoto);
        }
        close();
    }

    public Long crearPersona(String nombre, Long idContacto, String uriFoto, Long idApatxa) {
        open();
        Long idPersona = personaDAO.crearPersona(nombre, idApatxa, idContacto, uriFoto);
        close();
        return idPersona;
    }


    public void borrarPersona(Long idPersona) {
        open();
        personaDAO.borrarPersona(idPersona);
        gastoDAO.establecerComoNoPagadosGastosPagadosPorPersona(idPersona);
        close();
    }

    public void borrarPersonas(List<PersonaListado> listaPersonas) {
        open();
        for (PersonaListado persona : listaPersonas) {
            borrarPersona(persona.id);
        }
        close();
    }


    public void marcarPersonasEstadoReparto(List<PersonaListadoReparto> listaPersonas, boolean estadoPagado) {
        open();
        List<Long> idsPersonas = new ArrayList<>(listaPersonas.size());
        for (PersonaListadoReparto persona : listaPersonas) {
            idsPersonas.add(persona.id);
        }
        if (estadoPagado) {
            personaDAO.marcarPersonasRepartoPagado(idsPersonas);
        } else {
            personaDAO.marcarPersonasRepartoPendiente(idsPersonas);
        }
        close();
    }

    public void actualizarMiNombre() {
        open();
        String[] columnasPerfil = new String[]{ContactsContract.Profile.DISPLAY_NAME_PRIMARY};
        Cursor perfilCursor = context.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, columnasPerfil, null, null, null);
        perfilCursor.moveToFirst();
        String nombre = context.getString(R.string.yo_mayusculas_nombre, perfilCursor.getString(0));
        perfilCursor.close();
        personaDAO.actualizarMiNombre(nombre);
        close();
    }

}
