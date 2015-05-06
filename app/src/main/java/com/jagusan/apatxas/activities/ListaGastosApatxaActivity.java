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
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.logicaNegocio.servicios.GastoService;
import com.jagusan.apatxas.modelView.ApatxaDetalle;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.CalcularSumaTotalGastos;

import java.util.ArrayList;
import java.util.List;

public class ListaGastosApatxaActivity extends ApatxasActionBarActivity {

    private int NUEVO_GASTO_REQUEST_CODE = 1;
    private int EDITAR_GASTO_REQUEST_CODE = 2;

    private Long idApatxa;
    private ApatxaDetalle apatxa;
    private List<PersonaListado> personasApatxa;

    private TextView tituloGastosApatxaListViewHeader;
    private List<GastoApatxaListado> listaGastos = new ArrayList<>();
    private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;

    private ApatxaService apatxaService;
    private GastoService gastoService;
    private Resources resources;

    private ActionMode actionMode;
    private boolean hayModificacionesEnGastos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_apatxa_paso3);

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_lista_gastos, MostrarTituloPantalla.LISTA_GASTOS);

        recuperarDatosPasoAnterior();

        cargarElementosLayout();

        hayModificacionesEnGastos = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_gastos_apatxa, menu);
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
            case R.id.action_anadir_gasto:
                continuarMostrandoAvisoSiNecesario(R.id.action_anadir_gasto, null);
                return true;
            case R.id.action_ordenar_por_nombre:
                listaGastosApatxaArrayAdapter.reordenarPorNombre();
                return true;
            case R.id.action_ordenar_por_orden_entrada:
                listaGastosApatxaArrayAdapter.reordenarPorOrdenEntrada();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void inicializarServicios() {
        apatxaService = new ApatxaService(this);
        gastoService = new GastoService(this);
        resources = getResources();
    }


    private void cargarElementosLayout() {
        ListView gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxa);
        anadirCabeceraListaGastos();

        listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_apatxa_row, listaGastos);
        gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
        gastosApatxaListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        gastosApatxaListView.setMultiChoiceModeListener(new ModeCallback());

        gestionarListaVacia(listaGastosApatxaArrayAdapter, false, R.string.lista_vacia_gastos, null);
    }

    private void recuperarDatosPasoAnterior() {
        Intent intent = getIntent();
        personasApatxa = (List<PersonaListado>) intent.getSerializableExtra("personas");
        idApatxa = intent.getLongExtra("idApatxa", -1);
        listaGastos = (List<GastoApatxaListado>) intent.getSerializableExtra("gastos");
        apatxa = apatxaService.getApatxaDetalle(idApatxa);

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


    private void continuarMostrandoAvisoSiNecesario(final int accionSobreGastos, final List<GastoApatxaListado> gastos) {
        if (!hayModificacionesEnGastos && apatxa.personasPendientesPagarCobrar != apatxa.personas.size()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(R.string.mensaje_confirmacion_resetear_pagos_cobros_del_reparto_gastos);
            alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    continuarAccionSobreGastos(accionSobreGastos, gastos);
                }
            });
            alertDialog.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog.create();
            alertDialog.show();
        } else {
            continuarAccionSobreGastos(accionSobreGastos, gastos);
        }
    }


    private void continuarAccionSobreGastos(int accionSobreGastos, List<GastoApatxaListado> gastos) {
        switch (accionSobreGastos) {
            case R.id.action_anadir_gasto:
                anadirGasto();
                break;
            case R.id.action_gasto_apatxa_cambiar:
                irPantallaEdicionGasto(gastos);
                break;
            case R.id.action_gasto_apatxa_borrar:
                confirmarBorradoGastos(gastos);
                break;
            default:
                break;
        }
    }

    private void confirmarBorradoGastos(final List<GastoApatxaListado> gastos) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(R.string.mensaje_confirmacion_borrado_gastos);
        alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                borrarGastos(gastos);
                actionMode.finish();
                MensajesToast.mostrarConfirmacionBorrados(getApplicationContext(), R.plurals.mensaje_confirmacion_borrado_gastos_realizado, gastos.size());
            }
        });
        alertDialog.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    public void anadirGasto() {
        Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
        intent.putExtra("personas", (ArrayList<PersonaListado>) personasApatxa);
        startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NUEVO_GASTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                crearGastoNuevo(data);
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_gasto_anadido);
            }
        }
        if (requestCode == EDITAR_GASTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                actualizarGastoListaDeGastos(data);
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_gasto_modificado);
            }
        }
    }


    private void borrarGastos(List<GastoApatxaListado> gastos) {
        gastoService.borrarGastos(gastos);
        hayModificacionesEnGastos = true;
        listaGastosApatxaArrayAdapter.eliminarGastosSeleccionados();
        actualizarTituloCabeceraListaGastos();
    }

    private void crearGastoNuevo(Intent data) {
        String conceptoGasto = data.getStringExtra("concepto");
        Double totalGasto = data.getDoubleExtra("total", 0);
        PersonaListado pagadoGasto = (PersonaListado) data.getSerializableExtra("pagadoPor");
        Long idPagador = (pagadoGasto != null) ? pagadoGasto.id : null;
        Long idGasto = gastoService.crearGasto(conceptoGasto, totalGasto, idApatxa, idPagador);
        hayModificacionesEnGastos = true;
        GastoApatxaListado gastoListado = new GastoApatxaListado(conceptoGasto, totalGasto, pagadoGasto);
        gastoListado.id = idGasto;
        listaGastosApatxaArrayAdapter.add(gastoListado);
        actualizarTituloCabeceraListaGastos();
    }

    private void actualizarGastoListaDeGastos(Intent data) {
        String conceptoGasto = data.getStringExtra("concepto");
        Double totalGasto = data.getDoubleExtra("total", 0);
        PersonaListado personaPagado = (PersonaListado) data.getSerializableExtra("pagadoPor");
        Integer posicionGastoActualizar = data.getIntExtra("posicionGastoEditar", -1);

        GastoApatxaListado gastoActualizado = listaGastosApatxaArrayAdapter.getItem(posicionGastoActualizar);
        gastoActualizado.concepto = conceptoGasto;
        gastoActualizado.total = totalGasto;
        gastoActualizado.pagadoPor = personaPagado != null ? personaPagado.nombre : null;
        Long idPersona = personaPagado != null ? personaPagado.id : null;
        gastoActualizado.idPagadoPor = idPersona;

        gastoService.actualizarGasto(gastoActualizado.id, conceptoGasto, totalGasto, idPersona);
        hayModificacionesEnGastos = true;
        listaGastosApatxaArrayAdapter.actualizarGasto(posicionGastoActualizar, gastoActualizado);
        actualizarTituloCabeceraListaGastos();
    }


    private void irPantallaEdicionGasto(List<GastoApatxaListado> gastos) {
        GastoApatxaListado gastoSeleccionadoEdicion = gastos.get(0);
        Intent intent = new Intent(this, EditarGastoApatxaActivity.class);
        intent.putExtra("conceptoGasto", gastoSeleccionadoEdicion.concepto);
        intent.putExtra("importeGasto", gastoSeleccionadoEdicion.total);
        intent.putExtra("idContactoPersonaPagadoGasto", gastoSeleccionadoEdicion.idContactoPersonaPagadoPor);
        intent.putExtra("personas", (ArrayList<PersonaListado>) personasApatxa);
        intent.putExtra("posicionGastoEditar", listaGastosApatxaArrayAdapter.getPosition(gastoSeleccionadoEdicion));
        startActivityForResult(intent, EDITAR_GASTO_REQUEST_CODE);
    }

    private void volverPantallaAnterior() {
        Intent returnIntent = new Intent();
        if (hayModificacionesEnGastos) {
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(RESULT_CANCELED, returnIntent);
        }
        finish();
    }

    private final class ModeCallback implements AbsListView.MultiChoiceModeListener {

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            listaGastosApatxaArrayAdapter.toggleSeleccion(position, checked);
            int numeroGastosSeleccionados = listaGastosApatxaArrayAdapter.numeroGastosSeleccionados();
            mode.setTitle(resources.getQuantityString(R.plurals.seleccionados, numeroGastosSeleccionados, numeroGastosSeleccionados));
            if (numeroGastosSeleccionados == 1) {
                findViewById(R.id.action_gasto_apatxa_cambiar).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.action_gasto_apatxa_cambiar).setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu_gasto_apatxa, menu);
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
                case R.id.action_gasto_apatxa_cambiar:
                    continuarMostrandoAvisoSiNecesario(R.id.action_gasto_apatxa_cambiar, listaGastosApatxaArrayAdapter.getGastosSeleccionados());
                    mode.finish();
                    return true;
                case R.id.action_gasto_apatxa_borrar:
                    continuarMostrandoAvisoSiNecesario(R.id.action_gasto_apatxa_borrar, listaGastosApatxaArrayAdapter.getGastosSeleccionados());
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listaGastosApatxaArrayAdapter.resetearSeleccion();
        }


    }
}
