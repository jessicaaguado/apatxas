package com.jagusan.apatxas.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.jagusan.apatxas.R;

public class NuevoGastoApatxaActivity extends ActionBarActivity {
	
	private final Boolean MOSTRAR_TITULO_PANTALLA = true;
	
	private EditText conceptoGastoEditText;
	private EditText totalGastoEditText;
	
	private Spinner personasSpinner;
	private List<String> personasApatxa;
	private ArrayAdapter<String> listaPersonasApatxaArrayAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_gasto_apatxa);
		
		personalizarActionBar();
		
		recuperarDatosPasoAnterior();
		
		conceptoGastoEditText = (EditText) findViewById(R.id.concepto);
		totalGastoEditText = (EditText) findViewById(R.id.totalGasto);
		
		personasSpinner = (Spinner) findViewById(R.id.listaPersonasApatxa);		
		listaPersonasApatxaArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, personasApatxa);
		personasSpinner.setAdapter(listaPersonasApatxaArrayAdapter);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.nuevo_gasto_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_asociar_gasto_apatxa) {
			Intent returnIntent = new Intent();
			
			String concepto = conceptoGastoEditText.getText().toString();			
			Double totalGasto = 0.0;
			try {
				totalGasto = Double.parseDouble(totalGastoEditText.getText().toString());
			} catch (Exception e) {
				// mantenemos total a 0
			}			
			int elementoSeleccionado = personasSpinner.getSelectedItemPosition();
			
			returnIntent.putExtra("concepto",concepto);
			returnIntent.putExtra("total",totalGasto);
			returnIntent.putExtra("pagadoPor", personasApatxa.get(elementoSeleccionado));
			
			setResult(RESULT_OK,returnIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void personalizarActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
	}
	
	private void recuperarDatosPasoAnterior() {				
		personasApatxa = getIntent().getStringArrayListExtra("personas");		
	}
}
