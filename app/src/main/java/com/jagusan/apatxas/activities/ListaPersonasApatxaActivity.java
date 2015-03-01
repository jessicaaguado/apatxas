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
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.logicaNegocio.servicios.PersonaService;
import com.jagusan.apatxas.modelView.ApatxaDetalle;
import com.jagusan.apatxas.modelView.ContactoListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.RecupararInformacionPersonas;

import java.util.ArrayList;
import java.util.List;

public class ListaPersonasApatxaActivity extends ApatxasActionBarActivity {

    private ApatxaDetalle apatxa;
    private Long idApatxa;
    private List<PersonaListado> personasApatxa;

    private ListView personasListView;
    private TextView tituloPersonasListViewHeader;
    private ListaPersonasApatxaArrayAdapter listaPersonasApatxaArrayAdapter;

    private Resources resources;
    private PersonaService personaService;
    private ApatxaService apatxaService;

    private ActionMode actionMode;
    private boolean hayModificacionesEnPersonas;

    private int SELECCIONAR_CONTACTOS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_personas_apatxa);

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_lista_personas, MostrarTituloPantalla.LISTA_PERSONAS);

        recuperarDatosPasoAnterior();

        cargarElementosLayout();

        hayModificacionesEnPersonas = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_personas_apatxa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                volverPantallaAnterior();
                finish();
                return true;
            case R.id.action_anadir_persona:
                continuarMostrandoAvisoSiNecesario(id, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void volverPantallaAnterior() {
        Intent returnIntent = new Intent();
        if (hayModificacionesEnPersonas) {
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(RESULT_CANCELED, returnIntent);
        }
        finish();
    }

    private void inicializarServicios() {
        resources = getResources();
        personaService = new PersonaService(this);
        apatxaService = new ApatxaService(this);
    }


    private void recuperarDatosPasoAnterior() {
        Intent intent = getIntent();
        personasApatxa = (ArrayList<PersonaListado>) intent.getSerializableExtra("personas");
        idApatxa = intent.getLongExtra("idApatxa", -1);
        apatxa = apatxaService.getApatxaDetalle(idApatxa);
    }

    private void cargarElementosLayout() {
        personasListView = (ListView) findViewById(R.id.listaPersonasApatxa);
        anadirCabeceraListaPersonas();

        listaPersonasApatxaArrayAdapter = new ListaPersonasApatxaArrayAdapter(this, R.layout.lista_personas_apatxa_row, personasApatxa);
        personasListView.setAdapter(listaPersonasApatxaArrayAdapter);
        personasListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        personasListView.setMultiChoiceModeListener(new ModeCallback());

        gestionarListaVacia(listaPersonasApatxaArrayAdapter, false, R.string.lista_vacia_personas, null);
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


    private void continuarMostrandoAvisoSiNecesario(final int accionSobrePersonas, final List<PersonaListado> personas) {
        if (!hayModificacionesEnPersonas && apatxa.personasPendientesPagarCobrar != apatxa.personas.size()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(R.string.mensaje_confirmacion_resetear_pagos_cobros_del_reparto_personas);
            alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    continuarAccionSobrePersonas(accionSobrePersonas, personas);
                }
            });
            alertDialog.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog.create();
            alertDialog.show();
        } else {
            continuarAccionSobrePersonas(accionSobrePersonas, personas);
        }
    }


    private void continuarAccionSobrePersonas(int accionSobrePersonas, List<PersonaListado> personas) {
        switch (accionSobrePersonas) {
            case R.id.action_anadir_persona:
                seleccionarNuevosContactos();
                break;
            case R.id.action_persona_apatxa_borrar:
                confirmarBorradoPersonas(personas);
                break;
            default:
                break;
        }
    }

    public void seleccionarNuevosContactos() {
        Intent intent = new Intent(this, ListaContactosActivity.class);
        intent.putExtra("idsContactosSeleccionados", (ArrayList<Long>) RecupararInformacionPersonas.obtenerIdsContactos(listaPersonasApatxaArrayAdapter.getPersonas()));
        startActivityForResult(intent, SELECCIONAR_CONTACTOS_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECCIONAR_CONTACTOS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                anadirContactosSeleccionados(data);
                MensajesToast.mostrarConfirmacionAnadidos(getApplicationContext(), R.plurals.mensaje_confimacion_personas_anadidas_realizado, ((ArrayList<ContactoListado>) data.getSerializableExtra("contactosSeleccionados")).size());
            }
        }
    }

    public void anadirContactosSeleccionados(Intent data) {
        List<ContactoListado> contactosSeleccionados = (ArrayList<ContactoListado>) data.getSerializableExtra("contactosSeleccionados");
        for (ContactoListado contacto : contactosSeleccionados) {
            Long idPersona = personaService.crearPersona(contacto.nombre, contacto.id, contacto.fotoURI, idApatxa);
            PersonaListado persona = new PersonaListado(idPersona, contacto.nombre, contacto.id, contacto.fotoURI);
            listaPersonasApatxaArrayAdapter.add(persona);
        }
        hayModificacionesEnPersonas = true;
        actualizarTituloCabeceraListaPersonas();
    }

    private void confirmarBorradoPersonas(final List<PersonaListado> personas) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(R.string.mensaje_confirmacion_borrado_personas);
        alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                borrarPersonas(personas);
                actionMode.finish();
                MensajesToast.mostrarConfirmacionBorrados(getApplicationContext(), R.plurals.mensaje_confirmacion_borrado_personas_realizado, personas.size());
            }
        });
        alertDialog.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    private void borrarPersonas(List<PersonaListado> personas) {
        personaService.borrarPersonas(personas);
        hayModificacionesEnPersonas = true;
        listaPersonasApatxaArrayAdapter.eliminarPersonasSeleccionadas();
        actualizarTituloCabeceraListaPersonas();
    }


    private final class ModeCallback implements AbsListView.MultiChoiceModeListener {

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            listaPersonasApatxaArrayAdapter.toggleSeleccion(position, checked);
            mode.setTitle(resources.getQuantityString(R.plurals.seleccionadas, listaPersonasApatxaArrayAdapter.numeroPersonasSeleccionadas(), listaPersonasApatxaArrayAdapter.numeroPersonasSeleccionadas()));
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu_persona_apatxa, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (actionMode == null) {
                actionMode = mode;
            }
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_persona_apatxa_borrar:
                    continuarMostrandoAvisoSiNecesario(item.getItemId(), listaPersonasApatxaArrayAdapter.getPersonasSeleccionadas());
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listaPersonasApatxaArrayAdapter.resetearSeleccion();
        }

    }

}
