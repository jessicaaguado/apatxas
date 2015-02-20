package com.jagusan.apatxas.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.modelView.ContactoListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.LogTags;
import com.jagusan.apatxas.utils.RecupararInformacionPersonas;
import com.jagusan.apatxas.utils.ValidacionActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NuevoApatxaPaso1Activity extends ApatxasActionBarActivity {

    private AutoCompleteTextView nombreApatxaAutoComplete;
    private TextView fechaInicioApatxaTextView;
    private TextView fechaFinApatxaTextView;
    private DatePickerDialog fechaInicioDatePickerDialog;
    private DatePickerDialog fechaFinDatePickerDialog;
    private Switch soloUnDiaSwitch;

    private ListView personasListView;
    private ViewGroup tituloPersonasListViewHeader;
    private List<PersonaListado> personasApatxa = new ArrayList<>();
    private ListaPersonasApatxaArrayAdapter listaPersonasApatxaArrayAdapter;

    private Resources resources;
    private ApatxaService apatxaService;

    private int SELECCIONAR_CONTACTOS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_apatxa_paso1);

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_nuevo_apatxa_paso1, MostrarTituloPantalla.NUEVO_APATXA_PASO1);

        cargarElementosLayout();

        inicializarElementosLayout();

        cargarInformacionPersonas();
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

    private void actualizarTituloCabeceraListaPersonas() {
        int numPersonas = personasApatxa.size();
        String titulo = resources.getQuantityString(R.plurals.titulo_cabecera_lista_personas, numPersonas, numPersonas);
        ((TextView) tituloPersonasListViewHeader.findViewById(R.id.listaPersonasApatxaCabecera)).setText(titulo);
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
        cargarInformacionPersonas();
    }


    public void borrarPersonas() {
        listaPersonasApatxaArrayAdapter.eliminarPersonasSeleccionadas();
        cargarInformacionPersonas();
    }

    private void continuarAnadirApatxas() {
        String titulo = nombreApatxaAutoComplete.getText().toString();
        Long fechaInicio = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaInicio = sdf.parse(fechaInicioApatxaTextView.getText().toString()).getTime();
        } catch (Exception e) {
            // sin fecha
        }
        Boolean soloUnDia = soloUnDiaSwitch.isChecked();
        Long fechaFin = fechaInicio;
        if (!soloUnDia) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                fechaFin = sdf.parse(fechaFinApatxaTextView.getText().toString()).getTime();
            } catch (Exception e) {
                // sin fecha
            }
        }

        if (validacionesCorrectas()) {
            Intent intent = new Intent(this, NuevoApatxaPaso2Activity.class);
            intent.putExtra("titulo", titulo);
            intent.putExtra("fechaInicio", fechaInicio);
            intent.putExtra("fechaFin", fechaFin);
            intent.putExtra("soloUnDia", soloUnDia);
            intent.putExtra("personas", (ArrayList<PersonaListado>) personasApatxa);

            startActivity(intent);
        }
    }

    private Boolean validacionesCorrectas() {
        return ValidacionActivity.validarTituloObligatorio(nombreApatxaAutoComplete, resources);
    }

    private void inicializarServicios() {
        resources = getResources();
        apatxaService = new ApatxaService(this);
    }

    private void cargarElementosLayout() {
        ViewGroup informacionBasica = (ViewGroup) getLayoutInflater().inflate(R.layout.subactivity_informacion_basica_apatxa, null);
        nombreApatxaAutoComplete = (AutoCompleteTextView) informacionBasica.findViewById(R.id.nombreApatxa);
        fechaInicioApatxaTextView = (TextView) informacionBasica.findViewById(R.id.fechaInicioApatxa);
        fechaFinApatxaTextView = (TextView) informacionBasica.findViewById(R.id.fechaFinApatxa);

        personasListView = (ListView) findViewById(R.id.listaPersonasApatxa);
        personasListView.addHeaderView(informacionBasica);
        tituloPersonasListViewHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.nuevo_apatxa_paso1_lista_personas_header, null);
        personasListView.addHeaderView(tituloPersonasListViewHeader);


        View listaVacia = findViewById(R.id.listaVaciaInfoSubactivity);
        ((RelativeLayout) listaVacia.getParent()).removeView(listaVacia);
        personasListView.addHeaderView(getLayoutInflater().inflate(R.layout.subactivity_lista_vacia, null));

        soloUnDiaSwitch = (Switch) findViewById(R.id.switchUnSoloDia);
        soloUnDiaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gestionarSoloUnDiaSwitch(isChecked);
            }
        });

    }

    private void cargarInformacionPersonas() {
        listaPersonasApatxaArrayAdapter = new ListaPersonasApatxaArrayAdapter(this, R.layout.lista_personas_apatxa_row, personasApatxa);
        personasListView.setAdapter(listaPersonasApatxaArrayAdapter);
        asignarContextualActionBar(personasListView);

        actualizarTituloCabeceraListaPersonas();
        gestionarListaVacia();
    }

    private void inicializarElementosLayout() {
        Calendar hoy = Calendar.getInstance();

        fechaInicioApatxaTextView.setText(FormatearFecha.formatearFecha(resources, hoy.getTime()));
        fechaFinApatxaTextView.setText(FormatearFecha.formatearFecha(resources, hoy.getTime()));

        fechaInicioDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar fechaSeleccionada = Calendar.getInstance();
                fechaSeleccionada.set(year, monthOfYear, dayOfMonth);
                fechaInicioApatxaTextView.setText(FormatearFecha.formatearFecha(resources, fechaSeleccionada.getTime()));
            }
        }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));

        fechaFinDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar fechaSeleccionada = Calendar.getInstance();
                fechaSeleccionada.set(year, monthOfYear, dayOfMonth);
                fechaFinApatxaTextView.setText(FormatearFecha.formatearFecha(resources, fechaSeleccionada.getTime()));
            }
        }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));


        List<String> todosTitulos = apatxaService.recuperarTodosTitulos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todosTitulos);
        nombreApatxaAutoComplete.setAdapter(adapter);
        nombreApatxaAutoComplete.setThreshold(3);
    }

    public void mostrarDatePicker(View view) {
        if (view == fechaInicioApatxaTextView) {
            fechaInicioDatePickerDialog.show();
        }
        if (view == fechaFinApatxaTextView) {
            fechaFinDatePickerDialog.show();
        }
    }


    private void asignarContextualActionBar(final ListView personasListView) {
        personasListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        personasListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaPersonasApatxaArrayAdapter adapter = (ListaPersonasApatxaArrayAdapter) ((HeaderViewListAdapter) personasListView.getAdapter()).getWrappedAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                //ponemos -numHeaders porque tenemos header
                int numHeaders = ((HeaderViewListAdapter) personasListView.getAdapter()).getHeadersCount();
                adapter.toggleSeleccion(position - numHeaders, checked);
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

    private void gestionarSoloUnDiaSwitch(boolean soloUnDia) {
        if (soloUnDia) {
            soloUnDiaSwitch.setText(resources.getString(R.string.solo_un_dia));
            soloUnDiaSwitch.setTextColor(resources.getColor(R.color.apatxascolors_color));
            fechaFinApatxaTextView.setVisibility(View.GONE);
        } else {
            soloUnDiaSwitch.setText(resources.getString(R.string.varios_dias));
            soloUnDiaSwitch.setTextColor(resources.getColor(R.color.apatxascolors_gris_medio));
            fechaFinApatxaTextView.setVisibility(View.VISIBLE);
        }
    }

    private void gestionarListaVacia() {
        listaPersonasApatxaArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                toggleInformacionListaVacia();
            }
        });
        toggleInformacionListaVacia();
    }

    private void toggleInformacionListaVacia() {

        int visibilidad = listaPersonasApatxaArrayAdapter.getCount() == 0 ? View.VISIBLE : View.GONE;
        findViewById(R.id.imagen_lista_vacia).setVisibility(visibilidad);
        ((TextView) findViewById(R.id.informacion_lista_vacia)).setText(R.string.lista_vacia_nuevo_apatxas_paso1);
        findViewById(R.id.informacion_lista_vacia).setVisibility(visibilidad);
        ((Button) findViewById(R.id.anadir_elementos_mas_tarde)).setText(R.string.lista_vacia_anadir_mas_tarde_nuevo_apatxas_paso1);
        findViewById(R.id.anadir_elementos_mas_tarde).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuarAnadirApatxas();
            }
        });
        findViewById(R.id.anadir_elementos_mas_tarde).setVisibility(visibilidad);
    }

}
