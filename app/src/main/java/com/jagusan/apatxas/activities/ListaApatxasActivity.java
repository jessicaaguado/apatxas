package com.jagusan.apatxas.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaApatxasArrayAdapter;
import com.jagusan.apatxas.listeners.OnVerDetalleApatxaClickListener;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.modelView.ApatxaListado;

import java.util.List;

public class ListaApatxasActivity extends ActionBarActivity {

    private ApatxaService apatxaService;
    private ListaApatxasArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_apatxas);
        personalizarActionBar();
        apatxaService = new ApatxaService(this);

        List<ApatxaListado> gastos = recuperarApatxas();
        ListView listaGastosListView = (ListView) findViewById(R.id.lista_gastos);
        adapter = new ListaApatxasArrayAdapter(this, R.layout.lista_apatxas_row, gastos);
        listaGastosListView.setAdapter(adapter);
        asignarContextualActionBar(listaGastosListView);

        OnVerDetalleApatxaClickListener listener = new OnVerDetalleApatxaClickListener();
        listaGastosListView.setOnItemClickListener(listener);

        anadirAdMob();
    }

    private void anadirAdMob() {
        AdView adView = (AdView) this.findViewById(R.id.adViewListaApatxasActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        //addTestDevice("B97B1584C555ECA1C4B537F4A54D3E14")
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_apatxas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_anadir:
                irAnadirApatxa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void personalizarActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }

    private void irAnadirApatxa() {
        Intent intent = new Intent(this, NuevoApatxaPaso1Activity.class);
        startActivity(intent);
    }

    private void borrarApatxas() {
        apatxaService.borrarApatxas(adapter.getApatxasSeleccionadas());
        adapter.eliminarApatxasSeleccionadas();
    }

    private List<ApatxaListado> recuperarApatxas() {
        return apatxaService.getTodosApatxasListado();
    }

    private void asignarContextualActionBar(final ListView apatxasListView) {
        apatxasListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        apatxasListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaApatxasArrayAdapter adapter = (ListaApatxasArrayAdapter) apatxasListView.getAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                adapter.toggleSeleccion(position, checked);
                mode.setTitle("" + adapter.numeroApatxasSeleccionadas());
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                this.mode = mode;
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu_apatxas, menu);
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
                    case R.id.action_apatxa_borrar:
                        confirmarBorrarApatxas();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.resetearSeleccion();
            }

            private void confirmarBorrarApatxas() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(adapter.getContext());
                alertDialog.setMessage(R.string.mensaje_confirmacion_borrado_apatxas);
                alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int numeroApatxasBorrar = adapter.numeroApatxasSeleccionadas();
                        borrarApatxas();
                        mode.finish();
                        MensajesToast.mostrarConfirmacionBorrados(adapter.getContext(), R.plurals.mensaje_confirmacion_borrado_apatxas_realizado, numeroApatxasBorrar);
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
