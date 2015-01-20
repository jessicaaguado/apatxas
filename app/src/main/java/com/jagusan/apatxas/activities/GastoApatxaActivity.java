package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;
import com.jagusan.apatxas.utils.ValidacionActivity;

public abstract class GastoApatxaActivity extends ActionBarActivity {

	private final Boolean MOSTRAR_TITULO_PANTALLA = true;

	protected EditText conceptoGastoEditText;
	protected EditText totalGastoEditText;
	private TextView labelPagadoPorTextView;

	protected Spinner personasSpinner;
	protected List<PersonaListado> personasApatxa;
	protected ArrayAdapter<String> listaPersonasApatxaArrayAdapter;

	protected Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_nuevo_gasto_apatxa);

		inicializarServicios();

		personalizarActionBar();

		recuperarDatosPasoAnterior();

		cargarElementosLayout();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private void inicializarServicios() {
		resources = getResources();
	}

	protected void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setElevation(0);
		actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
	}

	protected void recuperarDatosPasoAnterior() {
        personasApatxa = getIntent().getSerializableExtra("personas") != null ? (ArrayList<PersonaListado>) getIntent().getSerializableExtra("personas") : new ArrayList<PersonaListado>();
	}

	protected void cargarElementosLayout() {
		conceptoGastoEditText = (EditText) findViewById(R.id.concepto);
		totalGastoEditText = (EditText) findViewById(R.id.totalGasto);
		labelPagadoPorTextView = (TextView) findViewById(R.id.labelGastoPagadoPor);

		personasSpinner = (Spinner) findViewById(R.id.listaPersonasApatxa);
		if (!personasApatxa.isEmpty()) {
			List<String> personasApatxaConOpcionSinPagar = new ArrayList<String>();
			personasApatxaConOpcionSinPagar.add(resources.getString(R.string.sin_pagar));
            for (PersonaListado persona:personasApatxa){
                personasApatxaConOpcionSinPagar.add(persona.getNombre());
            }
			listaPersonasApatxaArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, personasApatxaConOpcionSinPagar);
			personasSpinner.setAdapter(listaPersonasApatxaArrayAdapter);
		} else {
			labelPagadoPorTextView.setVisibility(View.GONE);
			personasSpinner.setVisibility(View.GONE);
		}

	}

	protected Boolean validacionesCorrectas() {
		Boolean tituloOk = ValidacionActivity.validarTituloObligatorio(conceptoGastoEditText, resources);
		Boolean fechaOk = ValidacionActivity.validarCantidadObligatoria(totalGastoEditText, resources);
		return tituloOk && fechaOk;
	}

	protected String getPagadorSeleccionado() {
		// el 0 es #sin pagar#
		Integer personaSeleccionada = personasSpinner.getSelectedItemPosition() <= 0 ? null : personasSpinner.getSelectedItemPosition() - 1;
		String pagador = null;
		if (personaSeleccionada != null) {
			pagador = personasApatxa.get(personaSeleccionada).getNombre();
		}
		return pagador;
	}

	protected Double getImporteIntroducido() {
		Double totalGasto = 0.0;
		try {
			totalGasto = Double.parseDouble(totalGastoEditText.getText().toString());
		} catch (Exception e) {
			// mantenemos total a 0
		}
		return totalGasto;
	}

	protected String getConceptoIntroducido() {
		return conceptoGastoEditText.getText().toString();
	}

}
