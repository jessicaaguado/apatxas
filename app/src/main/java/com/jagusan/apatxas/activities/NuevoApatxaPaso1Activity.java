package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.modelView.ContactoListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.RecupararInformacionPersonas;
import com.jagusan.apatxas.utils.ValidacionActivity;

public class NuevoApatxaPaso1Activity extends ActionBarActivity {

    private final Boolean MOSTRAR_TITULO_PANTALLA = true;

    private AutoCompleteTextView nombreApatxaAutoComplete;
    private EditText fechaApatxaEditText;
    private EditText boteInicialEditText;
    private CheckBox descontarBoteInicialCheckBox;

    private ListView personasListView;
    private TextView tituloPersonasListViewHeader;
    private List<PersonaListado> personasApatxa = new ArrayList<PersonaListado>();
    private ListaPersonasApatxaArrayAdapter listaPersonasApatxaArrayAdapter;

    private Resources resources;
    private ApatxaService apatxaService;

    private int SELECCIONAR_CONTACTOS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_apatxa_paso1);

        inicializarServicios();

        personalizarActionBar();

        cargarElementosLayout();
        inicializarElementosLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nuevo_apatxa_paso1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_siguiente_paso:
                continuarAnadirApatxas();
                return true;
            case R.id.action_anadir_persona:
                seleccionarContactos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
        for (ContactoListado contacto : contactosSeleccionados) {
            PersonaListado persona = new PersonaListado();
            persona.nombre = contacto.nombre;
            persona.idContacto = contacto.id;
            persona.uriFoto = contacto.fotoURI;
            listaPersonasApatxaArrayAdapter.add(persona);
        }
        actualizarTituloCabeceraListaPersonas();
    }


    public void borrarPersonas() {
        listaPersonasApatxaArrayAdapter.eliminarPersonasSeleccionadas();
        actualizarTituloCabeceraListaPersonas();
    }

    private void continuarAnadirApatxas() {
        String titulo = nombreApatxaAutoComplete.getText().toString();
        Long fecha = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fecha = sdf.parse(fechaApatxaEditText.getText().toString()).getTime();
        } catch (Exception e) {
            // sin fecha
        }
        Double boteInicial = 0.0;
        try {
            boteInicial = Double.parseDouble(boteInicialEditText.getText().toString());
        } catch (Exception e) {
            // mantenemos bote inicial a 0
        }
        Boolean descontarBoteInicial = descontarBoteInicialCheckBox.isChecked();
        if (validacionesCorrectas()) {
            Intent intent = new Intent(this, NuevoApatxaPaso2Activity.class);
            intent.putExtra("titulo", titulo);
            intent.putExtra("fecha", fecha);
            intent.putExtra("boteInicial", boteInicial);
            intent.putExtra("descontarBoteInicial", descontarBoteInicial);
            intent.putExtra("personas", (ArrayList<PersonaListado>) personasApatxa);

            startActivity(intent);
        }
    }

    private Boolean validacionesCorrectas() {
        Boolean tituloOk = ValidacionActivity.validarTituloObligatorio(nombreApatxaAutoComplete, resources);
        Boolean fechaOk = ValidacionActivity.validarFechaObligatoria(fechaApatxaEditText, resources);
        return tituloOk && fechaOk;
    }

    private void inicializarServicios() {
        resources = getResources();
        apatxaService = new ApatxaService(this);
    }

    private void personalizarActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
    }

    private void cargarElementosLayout() {
        nombreApatxaAutoComplete = (AutoCompleteTextView) findViewById(R.id.nombreApatxa);
        fechaApatxaEditText = (EditText) findViewById(R.id.fechaApatxa);
        boteInicialEditText = (EditText) findViewById(R.id.boteInicialApatxa);
        descontarBoteInicialCheckBox = (CheckBox) findViewById(R.id.descontarBoteInicial);

        personasListView = (ListView) findViewById(R.id.listaPersonasApatxa);
        anadirCabeceraListaPersonas(getLayoutInflater());

        listaPersonasApatxaArrayAdapter = new ListaPersonasApatxaArrayAdapter(this, R.layout.lista_personas_apatxa_row, personasApatxa);
        personasListView.setAdapter(listaPersonasApatxaArrayAdapter);
        asignarContextualActionBar(personasListView);
    }

    private void inicializarElementosLayout() {
        fechaApatxaEditText.setText(FormatearFecha.formatearHoy(resources));

        List<String> todosTitulos = apatxaService.recuperarTodosTitulos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todosTitulos);
        nombreApatxaAutoComplete.setAdapter(adapter);
        nombreApatxaAutoComplete.setThreshold(3);
    }

    private void asignarContextualActionBar(final ListView personasListView) {
        personasListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        personasListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaPersonasApatxaArrayAdapter adapter = (ListaPersonasApatxaArrayAdapter) personasListView.getAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                adapter.toggleSeleccion(position, checked);
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
                        borrarPersonas();
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
