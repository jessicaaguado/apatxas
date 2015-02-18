package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaContactosArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.ContactoService;
import com.jagusan.apatxas.modelView.ContactoListado;

import java.util.ArrayList;
import java.util.List;

public class ListaContactosActivity extends ApatxasActionBarActivity {


    private ContactoService contactoService;

    ListView contactosListView;
    ListaContactosArrayAdapter listaContactosArrayAdapter;
    private ArrayList<Long> contactosYaElegidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_lista_contactos,MostrarTituloPantalla.LISTA_CONTACTOS);

        recuperarDatosPasoAnterior();

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
            case android.R.id.home:
                finish();
                return true;
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

    private void recuperarDatosPasoAnterior() {
        Intent intent = getIntent();
        contactosYaElegidos = (ArrayList<Long>) intent.getSerializableExtra("idsContactosSeleccionados");
    }

    private void cargarElementosLayout() {
        contactosListView = (ListView) findViewById(R.id.listaContactos);
        List<ContactoListado> contactos = contactoService.obtenerTodosContactosTelefono(contactosYaElegidos);
        listaContactosArrayAdapter = new ListaContactosArrayAdapter(this, R.layout.lista_contactos_row, contactos);
        contactosListView.setAdapter(listaContactosArrayAdapter);

        gestionarListaVacia();
    }

    private void gestionarListaVacia() {
        listaContactosArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                toggleInformacionListaVacia();
            }
        });
        toggleInformacionListaVacia();
    }

    private void toggleInformacionListaVacia() {
        int visibilidad = listaContactosArrayAdapter.getCount() == 0 ? View.VISIBLE : View.GONE;
        findViewById(R.id.imagen_lista_vacia).setVisibility(visibilidad);
        ((TextView)findViewById(R.id.informacion_lista_vacia)).setText(R.string.lista_vacia_contactos);
        findViewById(R.id.informacion_lista_vacia).setVisibility(visibilidad);
        findViewById(R.id.anadir_elementos_mas_tarde).setVisibility(View.GONE);
    }
}
