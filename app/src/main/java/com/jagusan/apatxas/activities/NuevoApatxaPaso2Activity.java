package com.jagusan.apatxas.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
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
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.logicaNegocio.servicios.GastoService;
import com.jagusan.apatxas.logicaNegocio.servicios.PersonaService;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.CalcularSumaTotalGastos;

import java.util.ArrayList;
import java.util.List;

public class NuevoApatxaPaso2Activity extends ApatxasActionBarActivity {


    private ApatxaService apatxaService;
    private PersonaService personaService;
    private GastoService gastoService;
    private Resources resources;

    private String tituloApatxa;
    private Long fechaInicioApatxa;
    private Long fechaFinApatxa;
    private Boolean soloUnDiaApatxa;
    private Double boteInicialApatxa;
    private ArrayList<PersonaListado> personasApatxa;
    private Boolean descontarBoteInicial;

    private int NUEVO_GASTO_REQUEST_CODE = 1;
    private int EDITAR_GASTO_REQUEST_CODE = 2;

    private TextView tituloGastosApatxaListViewHeader;
    private List<GastoApatxaListado> listaGastos = new ArrayList<>();
    private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_apatxa_paso2);

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_nuevo_apatxa_paso2,MostrarTituloPantalla.NUEVO_APATXA_PASO2);

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
            case R.id.action_guardar:
                guardarApatxa();
                return true;
            case R.id.action_anadir_gasto:
                anadirGasto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NUEVO_GASTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                anadirGastoAListaDeGastos(data);
            }
        }
        if (requestCode == EDITAR_GASTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                actualizarGastoListaDeGastos(data);
            }
        }
    }

    public void anadirGasto() {
        Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
        intent.putExtra("personas", personasApatxa);
        startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);
    }

    public void guardarApatxa() {
        Long idApatxa = apatxaService.crearApatxa(tituloApatxa, fechaInicioApatxa, fechaFinApatxa, soloUnDiaApatxa, boteInicialApatxa, descontarBoteInicial);
        personaService.crearPersonas(personasApatxa, idApatxa);
        gastoService.crearGastos(listaGastos, idApatxa);
        irListadoApatxasPrincipal();
        MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_apatxa_guardada);
    }

    private void irListadoApatxasPrincipal() {
        Intent intent = new Intent(this, ListaApatxasActivity.class);
        finish();
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void inicializarServicios() {
        apatxaService = new ApatxaService(this);
        personaService = new PersonaService(this);
        gastoService = new GastoService(this);
        resources = getResources();
    }


    private void cargarElementosLayout() {
        ListView gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxa);
        anadirCabeceraListaGastos();

        listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_apatxa_row, listaGastos);
        gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
        asignarContextualActionBar(gastosApatxaListView);

        gestionarListaVacia();
    }

    private void recuperarDatosPasoAnterior() {
        Intent intent = getIntent();
        tituloApatxa = intent.getStringExtra("titulo");
        fechaInicioApatxa = intent.getLongExtra("fechaInicio", -1);
        fechaFinApatxa = intent.getLongExtra("fechaFin", -1);
        soloUnDiaApatxa = intent.getBooleanExtra("soloUnDia", false);
        boteInicialApatxa = intent.getDoubleExtra("boteInicial", 0);
        descontarBoteInicial = intent.getBooleanExtra("descontarBoteInicial", false);
        personasApatxa = (ArrayList<PersonaListado>) intent.getSerializableExtra("personas");
    }

    private void anadirCabeceraListaGastos() {
        tituloGastosApatxaListViewHeader = (TextView) findViewById(R.id.listaGastosApatxaCabecera);
        tituloGastosApatxaListViewHeader.setVisibility(View.VISIBLE);
        actualizarTituloCabeceraListaGastos();
    }

    private void actualizarTituloCabeceraListaGastos() {
        int numGastos = listaGastos.size();
        String titulo = resources.getQuantityString(R.plurals.titulo_cabecera_lista_gastos, numGastos, numGastos, CalcularSumaTotalGastos.calcular(listaGastos));
        tituloGastosApatxaListViewHeader.setText(titulo);
    }


    private void anadirGastoAListaDeGastos(Intent data) {
        String conceptoGasto = data.getStringExtra("concepto");
        Double totalGasto = data.getDoubleExtra("total", 0);
        //TODO pagadoPor
        PersonaListado pagadoGasto = (PersonaListado) data.getSerializableExtra("pagadoPor");

        GastoApatxaListado gastoListado = new GastoApatxaListado(conceptoGasto, totalGasto, pagadoGasto);
        listaGastosApatxaArrayAdapter.add(gastoListado);
        actualizarTituloCabeceraListaGastos();
    }

    private void borrarGastos() {
        listaGastosApatxaArrayAdapter.eliminarGastosSeleccionados();
        actualizarTituloCabeceraListaGastos();
    }

    private void actualizarGastoListaDeGastos(Intent data) {
        String conceptoGasto = data.getStringExtra("concepto");
        Double totalGasto = data.getDoubleExtra("total", 0);
        //TODO pagadoPor
        PersonaListado pagadoGasto = (PersonaListado)data.getSerializableExtra("pagadoPor");
        Integer posicionGastoActualizar = data.getIntExtra("posicionGastoEditar", -1);

        GastoApatxaListado gastoListado = new GastoApatxaListado(conceptoGasto, totalGasto, pagadoGasto);
        listaGastosApatxaArrayAdapter.actualizarGasto(posicionGastoActualizar, gastoListado);
        actualizarTituloCabeceraListaGastos();
    }

    private void irPantallaEdicionGasto(GastoApatxaListado gasto) {
        Intent intent = new Intent(this, EditarGastoApatxaActivity.class);
        intent.putExtra("conceptoGasto", gasto.getConcepto());
        intent.putExtra("importeGasto", gasto.getTotal());
        intent.putExtra("idContactoPersonaPagadoGasto", gasto.idContactoPersonaPagadoPor);
        intent.putExtra("personas",personasApatxa);
        intent.putExtra("posicionGastoEditar", listaGastosApatxaArrayAdapter.getPosition(gasto));
        startActivityForResult(intent, EDITAR_GASTO_REQUEST_CODE);
    }

    private void asignarContextualActionBar(final ListView gastosListView) {
        gastosListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        gastosListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaGastosApatxaArrayAdapter adapter = (ListaGastosApatxaArrayAdapter) gastosListView.getAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                adapter.toggleSeleccion(position, checked);
                int numeroGastosSeleccionados = adapter.numeroGastosSeleccionados();
                mode.setTitle(resources.getQuantityString(R.plurals.seleccionados, numeroGastosSeleccionados,numeroGastosSeleccionados));
                if (numeroGastosSeleccionados == 1) {
                    findViewById(R.id.action_gasto_apatxa_cambiar).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.action_gasto_apatxa_cambiar).setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                this.mode = mode;
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu_gasto_apatxa, menu);
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
                switch (item.getItemId()) {
                    case R.id.action_gasto_apatxa_cambiar:
                        irPantallaEdicionGasto(listaGastosApatxaArrayAdapter.getGastosSeleccionados().get(0));
                        mode.finish();
                        return true;
                    case R.id.action_gasto_apatxa_borrar:
                        confimarBorradoGastos();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.resetearSeleccion();
            }

            private void confimarBorradoGastos() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(adapter.getContext());
                alertDialog.setMessage(R.string.mensaje_confirmacion_borrado_gastos);
                alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int numGastosBorrar = adapter.numeroGastosSeleccionados();
                        borrarGastos();
                        mode.finish();
                        MensajesToast.mostrarConfirmacionBorrados(adapter.getContext(), R.plurals.mensaje_confirmacion_borrado_gastos_realizado, numGastosBorrar);
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

    private void gestionarListaVacia() {
        listaGastosApatxaArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                toggleInformacionListaVacia();
            }
        });
        toggleInformacionListaVacia();
    }

    private void toggleInformacionListaVacia() {
        int visibilidad = listaGastosApatxaArrayAdapter.getCount() == 0 ? View.VISIBLE : View.GONE;
        findViewById(R.id.imagen_lista_vacia).setVisibility(visibilidad);
        ((TextView)findViewById(R.id.informacion_lista_vacia)).setText(R.string.lista_vacia_nuevo_apatxas_paso2);
        findViewById(R.id.informacion_lista_vacia).setVisibility(visibilidad);
        ((TextView)findViewById(R.id.anadir_elementos_mas_tarde)).setText(R.string.lista_vacia_anadir_mas_tarde_nuevo_apatxas_paso2);
        findViewById(R.id.anadir_elementos_mas_tarde).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarApatxa();
            }
        });
        findViewById(R.id.anadir_elementos_mas_tarde).setVisibility(visibilidad);
    }

}
