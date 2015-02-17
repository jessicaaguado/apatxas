package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasPaganGastoApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.GastoService;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.ValidacionActivity;

public abstract class GastoApatxaActivity extends ApatxasActionBarActivity {

	protected AutoCompleteTextView conceptoGastoAutoComplete;
	protected EditText totalGastoEditText;
	private TextView labelPagadoPorTextView;

	protected Spinner personasSpinner;
	protected List<PersonaListado> personasApatxa;
	protected ListaPersonasPaganGastoApatxaArrayAdapter listaPersonasApatxaArrayAdapter;

	protected Resources resources;
    private GastoService gastoService;

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
        gastoService = new GastoService(this);
	}

	abstract void personalizarActionBar();

	protected void recuperarDatosPasoAnterior() {
        personasApatxa = getIntent().getSerializableExtra("personas") != null ? (ArrayList<PersonaListado>) getIntent().getSerializableExtra("personas") : new ArrayList<PersonaListado>();
	}

	protected void cargarElementosLayout() {
		conceptoGastoAutoComplete = (AutoCompleteTextView) findViewById(R.id.concepto);
		totalGastoEditText = (EditText) findViewById(R.id.totalGasto);
		labelPagadoPorTextView = (TextView) findViewById(R.id.labelGastoPagadoPor);

		personasSpinner = (Spinner) findViewById(R.id.listaPersonasApatxaPagadoGasto);
		if (!personasApatxa.isEmpty()) {
			List<PersonaListado> personasApatxaConOpcionSinPagar = new ArrayList<PersonaListado>();

			//personasApatxaConOpcionSinPagar.add(resources.getString(R.string.sin_pagar));
            //for (PersonaListado persona:personasApatxa){
            //    personasApatxaConOpcionSinPagar.add(persona.nombre);
            // }
			//listaPersonasApatxaArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, personasApatxaConOpcionSinPagar);
            PersonaListado personaSinPagar = new PersonaListado();
            personaSinPagar.nombre = resources.getString(R.string.sin_pagar);
            personasApatxaConOpcionSinPagar.add(personaSinPagar);
            personasApatxaConOpcionSinPagar.addAll(personasApatxa);
            listaPersonasApatxaArrayAdapter = new ListaPersonasPaganGastoApatxaArrayAdapter(this, R.layout.lista_personas_apatxa_row, personasApatxaConOpcionSinPagar);
			personasSpinner.setAdapter(listaPersonasApatxaArrayAdapter);
		} else {
			labelPagadoPorTextView.setVisibility(View.GONE);
			personasSpinner.setVisibility(View.GONE);
		}

        List<String> todosConceptos = gastoService.recuperarTodosConceptos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todosConceptos);
        conceptoGastoAutoComplete.setAdapter(adapter);
        conceptoGastoAutoComplete.setThreshold(3);

	}

	protected Boolean validacionesCorrectas() {
		Boolean tituloOk = ValidacionActivity.validarTituloObligatorio(conceptoGastoAutoComplete, resources);
		Boolean fechaOk = ValidacionActivity.validarCantidadObligatoria(totalGastoEditText, resources);
		return tituloOk && fechaOk;
	}

	protected PersonaListado getPagadorSeleccionado() {
		/* Integer personaSeleccionada = personasSpinner.getSelectedItemPosition() <= 0 ? null : personasSpinner.getSelectedItemPosition() - 1;

        PersonaListado pagador = null;
		if (personaSeleccionada != null) {
			pagador = personasApatxa.get(personasSpinner.getSelectedItemPosition());
		}
        Log.d("APATXAS", "Posicion seleccionada " + personaSeleccionada+" "+(pagador == null ? "null" : pagador.nombre)+" "+(pagador == null ? "null" : pagador.idContacto));*/
        PersonaListado pagador = (PersonaListado) personasSpinner.getSelectedItem();
        Log.d("APATXAS", "Posicion seleccionada " + personasSpinner.getSelectedItemPosition()+" "+(pagador == null ? "null" : pagador.nombre)+" "+(pagador == null ? "null" : pagador.idContacto));
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
		return conceptoGastoAutoComplete.getText().toString();
	}

}
