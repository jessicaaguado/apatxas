package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import com.jagusan.apatxas.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public abstract class GastoApatxaActivity extends ActionBarActivity{
	
	private final Boolean MOSTRAR_TITULO_PANTALLA = true;
	
	protected EditText conceptoGastoEditText;
	protected EditText totalGastoEditText;
	
	protected Spinner personasSpinner;
	protected List<String> personasApatxa;
	protected ArrayAdapter<String> listaPersonasApatxaArrayAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_nuevo_gasto_apatxa);
		
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
	
	protected void personalizarActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
	}
	
	protected void recuperarDatosPasoAnterior() {				
		personasApatxa = getIntent().getStringArrayListExtra("personas");		
	}
	
	protected void cargarElementosLayout() {
		conceptoGastoEditText = (EditText) findViewById(R.id.concepto);
		totalGastoEditText = (EditText) findViewById(R.id.totalGasto);
		
		personasSpinner = (Spinner) findViewById(R.id.listaPersonasApatxa);
		if (!personasApatxa.isEmpty()){
			List<String> personasApatxaConOpcionSinPagar = new ArrayList<String>();
			personasApatxaConOpcionSinPagar.add("Sin pagar");
			personasApatxaConOpcionSinPagar.addAll(personasApatxa);
			listaPersonasApatxaArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, personasApatxaConOpcionSinPagar);
			personasSpinner.setAdapter(listaPersonasApatxaArrayAdapter);	
		}else{
			//TODO eliminar label y spinner
		}
		
		
	}

}
