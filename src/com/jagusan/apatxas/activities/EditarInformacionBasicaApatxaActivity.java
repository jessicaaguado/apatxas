package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.utils.FormatearNumero;
import com.jagusan.apatxas.utils.ValidacionActivity;

public class EditarInformacionBasicaApatxaActivity extends ActionBarActivity {

	private final Boolean MOSTRAR_TITULO_PANTALLA = true;

	private EditText nombreApatxaEditText;
	private EditText fechaApatxaEditText;
	private EditText boteInicialEditText;

	private String nombre;
	private Long fecha;
	private Double boteInicial;

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
			if (validacionesCorrectas()){				
				String nombre = getNombreIntroducido();
				Long fecha = getFechaIntroducida();
				Double boteInicial = getBoteIntroducido();
				
				Intent returnIntent = new Intent();
				returnIntent.putExtra("nombre", nombre);
				returnIntent.putExtra("fecha", fecha);
				returnIntent.putExtra("boteInicial", boteInicial);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
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

	private void cargarElementosLayout() {
		nombreApatxaEditText = (EditText) findViewById(R.id.nombreApatxa);
		fechaApatxaEditText = (EditText) findViewById(R.id.fechaApatxa);
		boteInicialEditText = (EditText) findViewById(R.id.boteInicialApatxa);
	}

	private void recuperarDatosPasoAnterior() {
		Intent intent = getIntent();
		nombre = intent.getStringExtra("nombre");
		fecha = intent.getLongExtra("fecha", -1);
		boteInicial = intent.getDoubleExtra("boteInicial", 0.0);
	}

	private void cargarInformacionApatxa() {
		nombreApatxaEditText.setText(nombre);
		if (fecha != -1) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fechaApatxaEditText.setText(sdf.format(new Date(fecha)));
		}
		boteInicialEditText.setText(boteInicial.toString());
	}
	
	
	protected String getNombreIntroducido() {
		return nombreApatxaEditText.getText().toString();
	}	

	protected Long getFechaIntroducida() {
		Long fecha = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fecha = sdf.parse(fechaApatxaEditText.getText().toString()).getTime();
		} catch (Exception e) {
			// sin fecha
		}
		return fecha;
	}

	protected Double getBoteIntroducido() {
		Double boteInicial = 0.0;
		try {
			boteInicial = Double.parseDouble(boteInicialEditText.getText().toString());
		} catch (Exception e) {
			// mantenemos bote inicial a 0
		}
		return boteInicial;
	}

	private Boolean validacionesCorrectas() {
		Boolean tituloOk = ValidacionActivity.validarTituloObligatorio(nombreApatxaEditText, resources);
		Boolean fechaOk = ValidacionActivity.validarFechaObligatoria(fechaApatxaEditText, resources);
		return tituloOk && fechaOk;
	}

}
