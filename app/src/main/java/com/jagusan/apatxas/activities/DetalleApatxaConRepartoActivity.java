package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasRepartoApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.PersonaService;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.utils.FormatearNumero;

import java.util.ArrayList;
import java.util.List;

public class DetalleApatxaConRepartoActivity extends DetalleApatxaActivity {

    private TextView resumenGastosApatxaTextView;
    private ListView personasRepartoListView;

    private int EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE = 20;

    private PersonaService personaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalle_apatxa_con_reparto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_actualizar_reparto_apatxa:
                apatxaService.realizarReparto(apatxa);
                cargarInformacionApatxa();
                return true;
            case R.id.action_acceso_gastos:
                irEditarListaGastosApatxa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_detalle_apatxa_con_reparto);
    }

    @Override
    protected void inicializarServicios() {
        super.inicializarServicios();
        personaService = new PersonaService(this);
    }

    @Override
    protected void cargarElementosLayout() {
        super.cargarElementosLayout();
        resumenGastosApatxaTextView = (TextView) findViewById(R.id.resumenGastosApatxaDetalle);
        personasRepartoListView = (ListView) findViewById(R.id.listaDesgloseRepartoApatxa);
    }

    @Override
    protected void cargarInformacionApatxa() {
        super.cargarInformacionApatxa();
        cargarInformacionGastos();
        cargarInformacionReparto();
    }

    private void cargarInformacionGastos() {
        resumenGastosApatxaTextView.setText(apatxa.getGastos().size() + " gastos. Total: " + FormatearNumero.aDineroEuros(resources, apatxa.getGastoTotal()));
    }

    private void cargarInformacionReparto() {
        List<PersonaListadoReparto> listaPersonasReparto = apatxaService.getResultadoReparto(idApatxa);
        ListaPersonasRepartoApatxaArrayAdapter listaPersonasRepartoApatxaArrayAdapter = new ListaPersonasRepartoApatxaArrayAdapter(this, R.layout.lista_personas_resultado_reparto_row,
                listaPersonasReparto);
        personasRepartoListView.setAdapter(listaPersonasRepartoApatxaArrayAdapter);
        asignarContextualActionBar(personasRepartoListView);
    }


    public void irEditarListaGastosApatxa() {
        Intent intent = new Intent(this, ListaGastosApatxaActivity.class);
        intent.putExtra("personas", new ArrayList<PersonaListado>(apatxa.getPersonas()));
        intent.putExtra("idApatxa", apatxa.getId());
        intent.putExtra("gastos", new ArrayList<GastoApatxaListado>(apatxa.getGastos()));
        startActivityForResult(intent, EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITAR_INFORMACION_BASICA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                actualizarReparto();
            }
        }
        if (requestCode == EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                actualizarReparto();
            }
        }
        if (requestCode == EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                actualizarReparto();
            }
        }
    }

    private void actualizarReparto() {
        apatxa = apatxaService.getApatxaDetalle(idApatxa);
        apatxaService.realizarReparto(apatxa);
        cargarInformacionApatxa();

    }


    private void asignarContextualActionBar(final ListView personasRepartoListView) {
        personasRepartoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        personasRepartoListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaPersonasRepartoApatxaArrayAdapter adapter = (ListaPersonasRepartoApatxaArrayAdapter) personasRepartoListView.getAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                adapter.toggleSeleccion(position, checked);
                mode.setTitle("" + adapter.numeroPersonasSeleccionadas());
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                this.mode = mode;
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu_reparto_apatxa, menu);
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
                    case R.id.action_marcar_hecho:
                        personaService.marcarPersonasEstadoReparto(adapter.getPersonasSeleccionadas(), true);
                        cargarInformacionApatxa();
                        mode.finish();
                        return true;
                    case R.id.action_marcar_pendiente:
                        personaService.marcarPersonasEstadoReparto(adapter.getPersonasSeleccionadas(), false);
                        cargarInformacionApatxa();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.resetearSeleccion();
            }


        });
    }

}
