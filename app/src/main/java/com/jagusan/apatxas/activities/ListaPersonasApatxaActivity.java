package com.jagusan.apatxas.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.GastoService;
import com.jagusan.apatxas.logicaNegocio.servicios.PersonaService;
import com.jagusan.apatxas.modelView.PersonaListado;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaPersonasApatxaActivity extends ActionBarActivity {

    private final Boolean MOSTRAR_TITULO_PANTALLA = true;

    private Long idApatxa;
    private List<PersonaListado> personasApatxa;
    private List<String> nombresPersonasAnadidas = new ArrayList<String>();
    private List<PersonaListado> personasEliminadas = new ArrayList<PersonaListado>();

    private ListView personasListView;
    private TextView tituloPersonasListViewHeader;
    private ListaPersonasApatxaArrayAdapter listaPersonasApatxaArrayAdapter;

    private Resources resources;
    private PersonaService personaService;
    private GastoService gastoService;

    private int numPersonasApatxaAnadidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_personas_apatxa);

        inicializarServicios();

        personalizarActionBar();

        recuperarDatosPasoAnterior();

        cargarElementosLayout();
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
            case R.id.action_guardar_lista_personas_apatxa:
                actualizarPersonasAnadidasBorradas();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            case R.id.action_anadir_persona:
                anadirPersonaParaGuardar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actualizarPersonasAnadidasBorradas() {
        for (PersonaListado persona : personasEliminadas) {
            Long idPersona = persona.getId();
            if (idPersona == null) {
                nombresPersonasAnadidas.remove(persona.getNombre());
            } else {
                personaService.borrarPersona(persona.getId());
            }
        }
        for (String nombre : nombresPersonasAnadidas) {
            personaService.crearPersona(nombre, idApatxa);
        }

    }

    private void inicializarServicios() {
        resources = getResources();
        personaService = new PersonaService(this);
        gastoService = new GastoService(this);
    }

    private void personalizarActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
    }

    private void recuperarDatosPasoAnterior() {
        Intent intent = getIntent();
        personasApatxa = (ArrayList<PersonaListado>) intent.getSerializableExtra("personas");
        idApatxa = intent.getLongExtra("idApatxa", -1);
        numPersonasApatxaAnadidas = personasApatxa.size();
    }

    private void cargarElementosLayout() {
        personasListView = (ListView) findViewById(R.id.listaPersonasApatxa);
        anadirCabeceraListaPersonas(getLayoutInflater());

        listaPersonasApatxaArrayAdapter = new ListaPersonasApatxaArrayAdapter(this, R.layout.lista_personas_apatxa_row, personasApatxa);
        personasListView.setAdapter(listaPersonasApatxaArrayAdapter);
        asignarContextualActionBar(personasListView);
    }

    private void anadirCabeceraListaPersonas(LayoutInflater inflater) {
        tituloPersonasListViewHeader = (TextView) findViewById(R.id.listaPersonasApatxaCabecera);
        actualizarTituloCabeceraListaPersonas();
    }

    private void actualizarTituloCabeceraListaPersonas() {
        int numPersonas = personasApatxa.size();
        String titulo = resources.getQuantityString(R.plurals.titulo_cabecera_lista_personas, numPersonas, numPersonas);
        tituloPersonasListViewHeader.setText(titulo);
    }

    public void anadirPersonaParaGuardar() {
        String nombre = "Apatxero " + ++numPersonasApatxaAnadidas + " " + (new Date()).getTime();
        PersonaListado nuevaPersona = new PersonaListado();
        nuevaPersona.setNombre(nombre);
        nombresPersonasAnadidas.add(nombre);
        listaPersonasApatxaArrayAdapter.add(nuevaPersona);
        actualizarTituloCabeceraListaPersonas();
    }

    public void anadirPersonasParaBorrar(List<PersonaListado> personas) {
        personasEliminadas.addAll(personas);
        listaPersonasApatxaArrayAdapter.eliminarPersonasSeleccionadas();
        actualizarTituloCabeceraListaPersonas();
    }

    private void asignarContextualActionBar(final ListView personasListView) {
        personasListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        personasListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaPersonasApatxaArrayAdapter adapter = (ListaPersonasApatxaArrayAdapter) personasListView.getAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Log.d("APATXAS", "onItemCheckedStateChanged " + position + " " + checked);

                adapter.toggleSeleccion(position, checked);
                mode.setTitle("" + adapter.numeroPersonasSeleccionadas());
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
                if (this.mode == null){
                    this.mode = mode;
                }
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Log.d("APATXAS", "onActionItemClicked " + mode + " " + item);
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
                Log.d("APATXAS", "onDestroyActionMode " + mode);
                adapter.resetearSeleccion();
            }

            private void confimarBorradoPersonas() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(adapter.getContext());
                alertDialog.setMessage(R.string.mensaje_confirmacion_borrado_personas);
                alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        anadirPersonasParaBorrar(adapter.getPersonasSeleccionadas());
                        mode.finish();
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

}
