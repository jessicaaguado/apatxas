package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaContactosArrayAdapter;
import com.jagusan.apatxas.adapters.ListaPersonasApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.ContactoService;
import com.jagusan.apatxas.logicaNegocio.servicios.GastoService;
import com.jagusan.apatxas.logicaNegocio.servicios.PersonaService;
import com.jagusan.apatxas.modelView.ContactoListado;
import com.jagusan.apatxas.modelView.PersonaListado;

import java.util.ArrayList;
import java.util.List;

public class ListaContactosActivity extends ActionBarActivity {

    private static final Boolean MOSTRAR_TITULO_PANTALLA = true;

    private SimpleCursorAdapter adapter;

    private ContactoService contactoService;

    ListView contactosListView;
    ListaContactosArrayAdapter listaContactosArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        personalizarActionBar();

        inicializarServicios();

        personalizarActionBar();

        cargarElementosLayout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_contactos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_guardar_contactos_elegidos:
                guardarContactosElegidos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void guardarContactosElegidos() {
        List<ContactoListado> contactos = listaContactosArrayAdapter.getContactosSeleccionados();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("contactosSeleccionados", (ArrayList<ContactoListado>) contactos);
        setResult(RESULT_OK, returnIntent);
        finish();
    }


    private void inicializarServicios() {
        contactoService = new ContactoService(this);
    }

    private void personalizarActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
    }


    private void cargarElementosLayout() {
        contactosListView = (ListView) findViewById(R.id.listaContactos);
        List<ContactoListado> contactos = contactoService.obtenerTodosContactosTelefono();
        listaContactosArrayAdapter = new ListaContactosArrayAdapter(this, R.layout.lista_contactos_row, contactos);
        contactosListView.setAdapter(listaContactosArrayAdapter);
    }
}
