package com.jagusan.apatxas.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasApatxaArrayAdapter;
import com.jagusan.apatxas.modelView.ContactoListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.RecupararInformacionPersonas;

import java.util.ArrayList;
import java.util.List;

public class NuevoApatxaPaso2Activity extends ApatxasActionBarActivity {

    private String tituloApatxa;
    private Long fechaInicioApatxa;
    private Long fechaFinApatxa;
    private Boolean soloUnDiaApatxa;

    private List<PersonaListado> personasApatxa = new ArrayList<>();
    private ListaPersonasApatxaArrayAdapter listaPersonasApatxaArrayAdapter;
    private TextView tituloPersonasListViewHeader;

    private Resources resources;

    private int SELECCIONAR_CONTACTOS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_apatxa_paso2);

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_nuevo_apatxa_paso2, MostrarTituloPantalla.NUEVO_APATXA_PASO2);

        cargarElementosLayout();

        recuperarDatosPasoAnterior();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nuevo_apatxa_paso2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_anadir_persona:
                seleccionarContactos();
                return true;
            case R.id.action_siguiente_paso:
                continuarAnadirApatxas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void seleccionarContactos() {
        Intent intent = new Intent(this, ListaContactosActivity.class);
        intent.putExtra("idsContactosSeleccionados", (ArrayList<Long>) RecupararInformacionPersonas.obtenerIdsContactos(listaPersonasApatxaArrayAdapter.getPersonas()));
        startActivityForResult(intent, SELECCIONAR_CONTACTOS_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECCIONAR_CONTACTOS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                anadirContactosSeleccionados(data);
            }
        }
    }

    public void anadirContactosSeleccionados(Intent data) {
        List<ContactoListado> contactosSeleccionados = (ArrayList<ContactoListado>) data.getSerializableExtra("contactosSeleccionados");
        List<PersonaListado> personasAnadir = new ArrayList<>();
        for (ContactoListado contacto : contactosSeleccionados) {
            PersonaListado persona = new PersonaListado();
            persona.nombre = contacto.nombre;
            persona.idContacto = contacto.id;
            persona.uriFoto = contacto.fotoURI;
            personasAnadir.add(persona);
        }
        listaPersonasApatxaArrayAdapter.addAll(personasAnadir);
        actualizarTituloCabeceraListaPersonas();
    }


    public void borrarPersonas() {
        listaPersonasApatxaArrayAdapter.eliminarPersonasSeleccionadas();
        actualizarTituloCabeceraListaPersonas();
    }

    private void anadirCabeceraListaPersonas() {
        tituloPersonasListViewHeader = (TextView) findViewById(R.id.listaPersonasApatxaCabecera);
        tituloPersonasListViewHeader.setVisibility(View.VISIBLE);
        actualizarTituloCabeceraListaPersonas();
    }

    private void actualizarTituloCabeceraListaPersonas() {
        int numPersonas = personasApatxa.size();
        String titulo = resources.getQuantityString(R.plurals.titulo_cabecera_lista_personas, numPersonas, numPersonas);
        tituloPersonasListViewHeader.setText(titulo);
    }


    private void continuarAnadirApatxas() {
        Intent intent = new Intent(this, NuevoApatxaPaso3Activity.class);
        intent.putExtra("titulo", tituloApatxa);
        intent.putExtra("fechaInicio", fechaInicioApatxa);
        intent.putExtra("fechaFin", fechaFinApatxa);
        intent.putExtra("soloUnDia", soloUnDiaApatxa);
        intent.putExtra("personas", (ArrayList<PersonaListado>) listaPersonasApatxaArrayAdapter.getPersonas());
        startActivity(intent);

    }


    private void inicializarServicios() {
        resources = getResources();
    }

    private void cargarElementosLayout() {
        ListView personasListView = (ListView) findViewById(R.id.listaPersonasApatxa);
        anadirCabeceraListaPersonas();

        listaPersonasApatxaArrayAdapter = new ListaPersonasApatxaArrayAdapter(this, R.layout.lista_personas_apatxa_row, personasApatxa);
        personasListView.setAdapter(listaPersonasApatxaArrayAdapter);
        asignarContextualActionBar(personasListView);

        gestionarListaVacia(listaPersonasApatxaArrayAdapter, true, R.string.lista_vacia_nuevo_apatxas_paso2, R.string.lista_vacia_anadir_mas_tarde_nuevo_apatxas_paso2);
    }



    private void recuperarDatosPasoAnterior() {
        Intent intent = getIntent();
        tituloApatxa = intent.getStringExtra("titulo");
        fechaInicioApatxa = intent.getLongExtra("fechaInicio", -1);
        fechaFinApatxa = intent.getLongExtra("fechaFin", -1);
        soloUnDiaApatxa = intent.getBooleanExtra("soloUnDia", false);
    }


    private void asignarContextualActionBar(final ListView personasListView) {
        personasListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        personasListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaPersonasApatxaArrayAdapter adapter = (ListaPersonasApatxaArrayAdapter) personasListView.getAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                adapter.toggleSeleccion(position, checked);
                mode.setTitle(resources.getQuantityString(R.plurals.seleccionadas, adapter.numeroPersonasSeleccionadas(), adapter.numeroPersonasSeleccionadas()));
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                this.mode = mode;
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu_persona_apatxa, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if (this.mode == null) {
                    this.mode = mode;
                }
                return false;
            }


            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_persona_apatxa_borrar:
                        confimarBorradoPersonas();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.resetearSeleccion();
            }

            private void confimarBorradoPersonas() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(adapter.getContext());
                alertDialog.setMessage(R.string.mensaje_confirmacion_borrado_personas);
                alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int numeroPersonasBorrar = adapter.numeroPersonasSeleccionadas();
                        borrarPersonas();
                        mode.finish();
                        MensajesToast.mostrarConfirmacionBorrados(adapter.getContext(), R.plurals.mensaje_confirmacion_borrado_personas_realizado, numeroPersonasBorrar);
                    }
                });
                alertDialog.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alertDialog.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void continuarSinAnadirElementos() {
        continuarAnadirApatxas();
    }

}
