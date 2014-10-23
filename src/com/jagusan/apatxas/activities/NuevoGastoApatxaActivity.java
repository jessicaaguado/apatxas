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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuevo_gasto_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
		// quitamos el titulo
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}
	
	private void recuperarDatosPasoAnterior() {				
		personasApatxa = getIntent().getStringArrayListExtra("personas");		
	}
}
