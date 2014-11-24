package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jagusan.apatxas.R;

public class EditarInformacionBasicaApatxaActivity extends ActionBarActivity {
	
	private final Boolean MOSTRAR_TITULO_PANTALLA = true;

		protected Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_editar_informacion_basica_apatxa);

		inicializarServicios();

		personalizarActionBar();

		recuperarDatosPasoAnterior();

		cargarElementosLayout();
		
		cargarInformacionApatxa();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.editar_informacion_basica_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_guardar_informacion_basica) {
			Intent returnIntent = new Intent();				
			setResult(RESULT_OK,returnIntent);
			finish();			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void inicializarServicios() {
		resources = getResources();
	}

	private void personalizarActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
	}

	private void recuperarDatosPasoAnterior() {		
	}

	private void cargarElementosLayout() {
		
	}
	
	private void cargarInformacionApatxa(){
		
	}

//	private Boolean validacionesCorrectas() {
//		return true;
//	}
	
	

}
