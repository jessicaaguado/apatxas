package com.jagusan.apatxas.activities;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.utils.SettingsUtils;

public class ApatxasActionBarActivity extends ActionBarActivity {


    private int stringIdAnadirElementosPosterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsUtils.aplicarSettingsPropios(this);

    }

    void personalizarActionBar(int idTituloString, boolean mostrar) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(mostrar);
        actionBar.setTitle(this.getResources().getString(idTituloString));
    }

    protected void gestionarListaVacia(final ArrayAdapter adapter, final boolean mostrarAnadirElementosPosteriori, Integer idStringListaVacia, Integer idStringAnadirElementosPosteriori) {
        final int idStringTextoListaVacia = idStringListaVacia != null ? idStringListaVacia : R.string.lista_vacia;
        final int idStringTextoAnadirElementosPosteriori = idStringAnadirElementosPosteriori != null ? idStringAnadirElementosPosteriori : R.string.anadir_elementos_mas_tarde;
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                toggleInformacionListaVacia(adapter, mostrarAnadirElementosPosteriori, idStringTextoListaVacia, idStringTextoAnadirElementosPosteriori);
            }
        });
        toggleInformacionListaVacia(adapter, mostrarAnadirElementosPosteriori, idStringTextoListaVacia, idStringTextoAnadirElementosPosteriori);
    }

    private void toggleInformacionListaVacia(ArrayAdapter adapter, boolean mostrarAnadirElementosPosteriori, int idStringTextoListaVacia, int idStringTextoAnadirElementosPosteriori) {
        int visibilidad = adapter.getCount() == 0 ? View.VISIBLE : View.GONE;
        findViewById(R.id.imagen_lista_vacia).setVisibility(visibilidad);
        findViewById(R.id.informacion_lista_vacia).setVisibility(visibilidad);
        int visibilidadAnadirElementosPosteriori = mostrarAnadirElementosPosteriori ? View.VISIBLE : View.GONE;
        findViewById(R.id.anadir_elementos_mas_tarde).setVisibility(visibilidad == View.GONE ? View.GONE : visibilidadAnadirElementosPosteriori);
        if (visibilidad == View.VISIBLE) {
            ((TextView) findViewById(R.id.informacion_lista_vacia)).setText(idStringTextoListaVacia);
        }
        if (visibilidadAnadirElementosPosteriori == View.VISIBLE) {
            ((TextView) findViewById(R.id.anadir_elementos_mas_tarde)).setText(idStringTextoAnadirElementosPosteriori);
            findViewById(R.id.anadir_elementos_mas_tarde).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    continuarSinAnadirElementos();
                }
            });
        }
    }

    protected void continuarSinAnadirElementos() {
    }

    ;
}
