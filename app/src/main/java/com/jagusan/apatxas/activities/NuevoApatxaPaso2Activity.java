package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.logicaNegocio.GastoService;
import com.jagusan.apatxas.logicaNegocio.PersonaService;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.CalcularSumaTotalGastos;

public class NuevoApatxaPaso2Activity extends ActionBarActivity {

    private final Boolean MOSTRAR_TITULO_PANTALLA = true;

    private ApatxaService apatxaService;
    private PersonaService personaService;
    private GastoService gastoService;
    private Resources resources;

    private String tituloApatxa;
    private Long fechaApatxa;
    private Double boteInicialApatxa;
    private ArrayList<PersonaListado> personasApatxa;
    private Boolean descontarBoteInicial;

    private int NUEVO_GASTO_REQUEST_CODE = 1;
    private int EDITAR_GASTO_REQUEST_CODE = 2;

    private ListView gastosApatxaListView;
    private TextView tituloGastosApatxaListViewHeader;
    private List<GastoApatxaListado> listaGastos = new ArrayList<GastoApatxaListado>();
    private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_apatxa_paso2);

        inicializarServicios();

        personalizarActionBar();

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
        Long idApatxa = apatxaService.crearApatxa(tituloApatxa, fechaApatxa, boteInicialApatxa, descontarBoteInicial);
        personaService.crearPersonas(personasApatxa, idApatxa);
        gastoService.crearGastos(listaGastos, idApatxa);
        irListadoApatxasPrincipal();
    }

    private void irListadoApatxasPrincipal() {
        Intent intent = new Intent(this, ListaApatxasActivity.class);
        finish();
        startActivity(intent);
    }

    private void inicializarServicios() {
        apatxaService = new ApatxaService(this);
        personaService = new PersonaService(this);
        gastoService = new GastoService(this);
        resources = getResources();
    }

    private void personalizarActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
    }

    private void cargarElementosLayout() {
        gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxa);
        anadirCabeceraListaGastos(getLayoutInflater());

        listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_apatxa_row, listaGastos);
        gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
        asignarContextualActionBar(gastosApatxaListView);
    }

    private void recuperarDatosPasoAnterior() {
        Intent intent = getIntent();
        tituloApatxa = intent.getStringExtra("titulo");
        fechaApatxa = intent.getLongExtra("fecha", -1);
        boteInicialApatxa = intent.getDoubleExtra("boteInicial", 0);
        descontarBoteInicial = intent.getBooleanExtra("descontarBoteInicial", false);
        personasApatxa = (ArrayList<PersonaListado>) intent.getSerializableExtra("personas");
    }

    private void anadirCabeceraListaGastos(LayoutInflater inflater) {
        tituloGastosApatxaListViewHeader = (TextView) findViewById(R.id.listaGastosApatxaCabecera);
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
        String pagadoGasto = data.getStringExtra("pagadoPor");

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
        String pagadoGasto = data.getStringExtra("pagadoPor");
        Integer posicionGastoActualizar = data.getIntExtra("posicionGastoEditar", -1);

        GastoApatxaListado gastoListado = new GastoApatxaListado(conceptoGasto, totalGasto, pagadoGasto);
        listaGastosApatxaArrayAdapter.actualizarGasto(posicionGastoActualizar, gastoListado);
        actualizarTituloCabeceraListaGastos();
    }

    private void irPantallaEdicionGasto(GastoApatxaListado gasto) {
        Intent intent = new Intent(this, EditarGastoApatxaActivity.class);
        intent.putExtra("conceptoGasto", gasto.getConcepto());
        intent.putExtra("importeGasto", gasto.getTotal());
        intent.putExtra("nombrePersonaPagadoGasto", gasto.getPagadoPor());
        intent.putExtra("personas", (ArrayList<PersonaListado>) personasApatxa);
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
                mode.setTitle("" + numeroGastosSeleccionados);
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
                        borrarGastos();
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
