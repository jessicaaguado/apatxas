package com.jagusan.apatxas.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.utils.FormatearNumero;

public class EditarGastoApatxaActivity extends ActionBarActivity {
	
	private final Boolean MOSTRAR_TITULO_PANTALLA = true;
	
	private EditText conceptoGastoEditText;
	private EditText totalGastoEditText;
	
	private Integer posicionGastoEditar;
	private Long idGasto;
	private String conceptoGasto;
	private Double importeGasto;
	private String nombrePersonaPagadoGasto;
	private Long idPersonaPagadoGasto;
	private Long idApatxa;
	private List<String> personasApatxa;
	private Spinner personasSpinner;	
	private ArrayAdapter<String> listaPersonasApatxaArrayAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_nuevo_gasto_apatxa);
		
		personalizarActionBar();
		
		recuperarDatosPasoAnterior();
		
		cargarElementosLayout();
		
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
			Log.d("APATXAS","TOTAL introducido "+totalGasto+" -- #"+totalGastoEditText.getText().toString()+"#");
			returnIntent.putExtra("pagadoPor", personasApatxa.get(elementoSeleccionado));
			returnIntent.putExtra("posicionGastoEditar", posicionGastoEditar);
			
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

	private void cargarElementosLayout() {
		conceptoGastoEditText = (EditText) findViewById(R.id.concepto);
		totalGastoEditText = (EditText) findViewById(R.id.totalGasto);
		
		personasSpinner = (Spinner) findViewById(R.id.listaPersonasApatxa);		
		listaPersonasApatxaArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, personasApatxa);
		personasSpinner.setAdapter(listaPersonasApatxaArrayAdapter);
		
	}	

	private void cargarInformacionGasto() {
		conceptoGastoEditText.setText(conceptoGasto);
		totalGastoEditText.setText(importeGasto.toString());
		personasSpinner.setSelection(personasApatxa.indexOf(nombrePersonaPagadoGasto));
		
	}

}
