package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jagusan.apatxas.R;

public class EditarGastoApatxaActivity extends GastoApatxaActivity {

	private Integer posicionGastoEditar;
	private Long idGasto;
	private String conceptoGasto;
	private Double importeGasto;
	private String nombrePersonaPagadoGasto;
	private Long idPersonaPagadoGasto;
	private Long idApatxa;

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
		int id = item.getItemId();
		if (id == R.id.action_actualizar_gasto_apatxa) {
			if (validacionesCorrectas()) {
				String concepto = getConceptoIntroducido();
				Double totalGasto = getImporteIntroducido();
				String pagador = getPagadorSeleccionado();

				Intent returnIntent = new Intent();
				returnIntent.putExtra("concepto", concepto);
				returnIntent.putExtra("total", totalGasto);				
				returnIntent.putExtra("pagadoPor", pagador);
				returnIntent.putExtra("posicionGastoEditar", posicionGastoEditar);
				setResult(RESULT_OK, returnIntent);
				
				finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

		@Override
	protected void recuperarDatosPasoAnterior() {
		super.recuperarDatosPasoAnterior();
		Intent intent = getIntent();
		conceptoGasto = intent.getStringExtra("conceptoGasto");
		importeGasto = intent.getDoubleExtra("importeGasto", 0.0);
		nombrePersonaPagadoGasto = intent.getStringExtra("nombrePersonaPagadoGasto");
		personasApatxa = intent.getStringArrayListExtra("personas");
		posicionGastoEditar = intent.getIntExtra("posicionGastoEditar", -1);
		idApatxa = intent.getLongExtra("idApatxa", -1);
		idGasto = intent.getLongExtra("idGasto", -1);
		idPersonaPagadoGasto = intent.getLongExtra("idPersonaPagadoGasto", -1);
	}

	private void cargarInformacionGasto() {
		conceptoGastoEditText.setText(conceptoGasto);
		totalGastoEditText.setText(importeGasto.toString());
		personasSpinner.setSelection(getPosicionPersonaSeleccionada());

	}

	private int getPosicionPersonaSeleccionada() {
		//anadimos 1 porque hemos forzado un nuevo elemento en la lista por delante #sin pagar#
		return personasApatxa.indexOf(nombrePersonaPagadoGasto)+1;
	}

}
