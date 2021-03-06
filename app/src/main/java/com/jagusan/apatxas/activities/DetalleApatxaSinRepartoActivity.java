package com.jagusan.apatxas.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.GastoService;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.CalcularSumaTotalGastos;

import java.util.ArrayList;
import java.util.List;

public class DetalleApatxaSinRepartoActivity extends DetalleApatxaActivity {

    private final int NUEVO_GASTO_REQUEST_CODE = 20;
    private final int EDITAR_GASTO_REQUEST_CODE = 21;
    private ListView gastosApatxaListView;
    private TextView tituloGastosApatxaListViewHeader;
    private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;
    private List<GastoApatxaListado> gastosApatxa;
    private GastoService gastoService;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalle_apatxa_sin_reparto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_repartir_apatxa:
                apatxaService.realizarRepartoSiNecesario(apatxa);
                verReparto();
                return true;
            case R.id.action_anadir_gasto:
                anadirGastoDetalleApatxa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void irPantallaEdicionGasto(GastoApatxaListado gasto) {
        Intent intent = new Intent(this, EditarGastoApatxaActivity.class);
        intent.putExtra("conceptoGasto", gasto.concepto);
        intent.putExtra("importeGasto", gasto.total);
        intent.putExtra("idContactoPersonaPagadoGasto", gasto.idContactoPersonaPagadoPor);
        intent.putExtra("personas", (ArrayList<PersonaListado>) apatxa.personas);
        intent.putExtra("idGasto", gasto.id);
        startActivityForResult(intent, EDITAR_GASTO_REQUEST_CODE);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_detalle_apatxa_sin_reparto);
    }

    @Override
    protected void inicializarServicios() {
        super.inicializarServicios();
        gastoService = new GastoService(this);
    }

    private void verReparto() {
        Intent intent = new Intent(this, DetalleApatxaConRepartoActivity.class);
        intent.putExtra("id", idApatxa);
        startActivity(intent);
    }

    @Override
    protected void cargarElementosLayout() {
        super.cargarElementosLayout();
        gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxa);
        gastosApatxaListView.addHeaderView(headerInformacionDetalle, null, false);

        ViewGroup cabeceraTituloGastos = (ViewGroup) getLayoutInflater().inflate(R.layout.detalle_apatxa_lista_gastos_header, null);
        gastosApatxaListView.addHeaderView(cabeceraTituloGastos, null, false);
        tituloGastosApatxaListViewHeader = (TextView) cabeceraTituloGastos.findViewById(R.id.listaGastosApatxaCabecera);

        findViewById(R.id.listaVaciaInfoSubactivity).setVisibility(View.GONE);
        gastosApatxaListView.addHeaderView(getLayoutInflater().inflate(R.layout.subactivity_lista_vacia, null), null, false);
    }

    @Override
    protected void cargarInformacionApatxa() {
        super.cargarInformacionApatxa();
        cargarInformacionGastos();
    }

    private void cargarInformacionGastos() {
        gastosApatxa = apatxa.gastos;
        listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_apatxa_row, gastosApatxa);
        gestionarListaVacia(listaGastosApatxaArrayAdapter, false, R.string.lista_vacia_gastos, null);
        gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
        asignarContextualActionBar(gastosApatxaListView);
        actualizarTituloCabeceraListaGastos();
    }

    private void actualizarTituloCabeceraListaGastos() {
        String titulo = resources.getQuantityString(R.plurals.titulo_cabecera_lista_gastos_detalle_apatxa, gastosApatxa.size(), gastosApatxa.size(), CalcularSumaTotalGastos.calcular(gastosApatxa));
        tituloGastosApatxaListViewHeader.setText(titulo);
    }


    void anadirGastoDetalleApatxa() {
        Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
        intent.putExtra("personas", (ArrayList<PersonaListado>) apatxa.personas);
        startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITAR_INFORMACION_BASICA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                cargarInformacionApatxa();
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_apatxa_guardada);
            }
        }
        if (requestCode == NUEVO_GASTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                guardarNuevoGastoApatxa(data);
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_gasto_anadido);
            }
        }
        if (requestCode == EDITAR_GASTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                actualizarGastoApatxa(data);
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_gasto_modificado);
            }
        }
        if (requestCode == EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                cargarInformacionApatxa();
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_personas_actualizadas);
            }
        }
    }

    private void guardarNuevoGastoApatxa(Intent data) {
        String conceptoGasto = data.getStringExtra("concepto");
        Double totalGasto = data.getDoubleExtra("total", 0);

        PersonaListado personaPagado = ((PersonaListado) data.getSerializableExtra("pagadoPor"));
        Long idPersona = personaPagado != null ? personaPagado.id : null;
        gastoService.crearGasto(conceptoGasto, totalGasto, idApatxa, idPersona);
        recargarInformacionGastos();
    }

    private void borrarGastos() {
        gastoService.borrarGastos(listaGastosApatxaArrayAdapter.getGastosSeleccionados());
        recargarInformacionGastos();
    }

    private void actualizarGastoApatxa(Intent data) {
        String conceptoGasto = data.getStringExtra("concepto");
        Double totalGasto = data.getDoubleExtra("total", 0);

        PersonaListado personaPagado = ((PersonaListado) data.getSerializableExtra("pagadoPor"));
        Long idPersona = personaPagado != null ? personaPagado.id : null;
        Long idGasto = data.getLongExtra("idGasto", -1);

        gastoService.actualizarGasto(idGasto, conceptoGasto, totalGasto, idPersona);
        recargarInformacionGastos();
    }

    private void recargarInformacionGastos() {
        apatxa = apatxaService.getApatxaDetalle(idApatxa);
        listaGastosApatxaArrayAdapter.clear();
        listaGastosApatxaArrayAdapter.addAll(apatxa.gastos);
        actualizarTituloCabeceraListaGastos();
    }

    private void asignarContextualActionBar(final ListView gastosListView) {
        gastosListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        gastosListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaGastosApatxaArrayAdapter adapter = (ListaGastosApatxaArrayAdapter) ((HeaderViewListAdapter) gastosListView.getAdapter()).getWrappedAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                //ponemos -numHeaders porque tenemos header
                int numHeaders = ((HeaderViewListAdapter) gastosListView.getAdapter()).getHeadersCount();
                adapter.toggleSeleccion(position - numHeaders, checked);
                int numeroGastosSeleccionados = adapter.numeroGastosSeleccionados();
                mode.setTitle(resources.getQuantityString(R.plurals.seleccionados, numeroGastosSeleccionados, numeroGastosSeleccionados));
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
                if (this.mode == null) {
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

}
