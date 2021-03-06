package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.PersonaListado;

import java.util.ArrayList;
import java.util.List;

public class EditarGastoApatxaActivity extends GastoApatxaActivity {

    private Integer posicionGastoEditar;
    private Long idGasto;
    private String conceptoGasto;
    private Double importeGasto;
    private Long idContactoPersonaPagadoPor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cargarInformacionGasto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editar_gasto_apatxa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_actualizar_gasto_apatxa:
                if (validacionesCorrectas()) {
                    String concepto = getConceptoIntroducido();
                    Double totalGasto = getImporteIntroducido();
                    PersonaListado pagador = getPagadorSeleccionado();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("concepto", concepto);
                    returnIntent.putExtra("total", totalGasto);
                    returnIntent.putExtra("pagadoPor", pagador);
                    if (posicionGastoEditar != -1) {
                        returnIntent.putExtra("posicionGastoEditar", posicionGastoEditar);
                    }
                    if (idGasto != -1) {
                        returnIntent.putExtra("idGasto", idGasto);
                    }
                    setResult(RESULT_OK, returnIntent);

                    finish();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void personalizarActionBar() {
        super.personalizarActionBar(R.string.title_activity_editar_gasto, MostrarTituloPantalla.EDITAR_GASTO);
    }

    @Override
    protected void recuperarDatosPasoAnterior() {
        super.recuperarDatosPasoAnterior();
        Intent intent = getIntent();
        conceptoGasto = intent.getStringExtra("conceptoGasto");
        importeGasto = intent.getDoubleExtra("importeGasto", 0.0);
        idContactoPersonaPagadoPor = intent.getLongExtra("idContactoPersonaPagadoGasto", -1);
        personasApatxa = (ArrayList<PersonaListado>) intent.getSerializableExtra("personas");
        posicionGastoEditar = intent.getIntExtra("posicionGastoEditar", -1);
        idGasto = intent.getLongExtra("idGasto", -1);
    }

    private void cargarInformacionGasto() {
        conceptoGastoAutoComplete.setText(conceptoGasto);
        totalGastoEditText.setText(importeGasto.toString());
        personasSpinner.setSelection(getPosicionPersonaSeleccionada());

    }

    private int getPosicionPersonaSeleccionada() {
        // anadimos 1 porque hemos forzado un nuevo elemento en la lista por
        // delante #sin pagar#
        List<Long> idContactoPersonas = new ArrayList<>();
        for (PersonaListado persona : personasApatxa) {
            idContactoPersonas.add(persona.idContacto);
        }
        if (idContactoPersonaPagadoPor == -1) {
            return 0;
        } else {
            return idContactoPersonas.indexOf(idContactoPersonaPagadoPor) + 1;
        }
    }

}
