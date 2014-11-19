package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.utils.FormatearNumero;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

public abstract class DetalleApatxaActivity extends ActionBarActivity {

	private final Boolean MOSTRAR_TITULO_PANTALLA = true;
	
	protected ApatxaService apatxaService;
	protected Resources resources;
	protected Long idApatxa;

	private TextView nombreApatxaTextView;
	private TextView fechaApatxaTextView;
	private TextView boteInicialEditText;
	private TextView numeroPersonasTextView;
	private TextView estadoApatxaTextView;

	protected ApatxaDetalle apatxa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		idApatxa = getIntent().getLongExtra("id", -1);

		setContentView();

		inicializarServicios();

		personalizarActionBar();

		cargarElementosLayout();

		cargarInformacionApatxa();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	abstract protected void setContentView();

	protected void inicializarServicios() {
		apatxaService = new ApatxaService(this);
		resources = getResources();
	}

	protected void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
		// boton para ir a la actividad anterior
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	protected void cargarElementosLayout() {
		nombreApatxaTextView = (TextView) findViewById(R.id.nombreApatxaDetalle);
		fechaApatxaTextView = (TextView) findViewById(R.id.fechaApatxaDetalle);
		boteInicialEditText = (TextView) findViewById(R.id.boteInicialApatxaDetalle);
		numeroPersonasTextView = (TextView) findViewById(R.id.numeroPersonasApatxaDetalle);
		estadoApatxaTextView = (TextView) findViewById(R.id.estadoApatxaDetalle);
	}

	protected void cargarInformacionApatxa() {
		apatxa = apatxaService.getApatxaDetalle(idApatxa);
		cargarInformacionTitulo();
		cargarInformacionFecha();
		cargarInformacionBoteInicial();
		cargarInformacionPersonas();
		cargarInformacionEstado();
	}

	private void cargarInformacionTitulo() {
		nombreApatxaTextView.setText(apatxa.getNombre());
	}

	private void cargarInformacionFecha() {
		Date fecha = apatxa.getFecha();
		if (fecha != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fechaApatxaTextView.setText(sdf.format(fecha));
		}
	}

	private void cargarInformacionBoteInicial() {
		Double boteInicial = apatxa.getBoteInicial();
		boteInicialEditText.setText(FormatearNumero.aDineroEuros(resources, boteInicial));
	}

	private void cargarInformacionPersonas() {
		int numPersonas = apatxa.getPersonas().size();
		String titulo = resources.getQuantityString(R.plurals.numero_personas_apatxa, numPersonas, numPersonas);
		numeroPersonasTextView.setText(titulo);
	}

	private void cargarInformacionEstado() {
		String estadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaDetalle(getResources(), apatxa.getGastoTotal(), apatxa.getPagado(), apatxa.getBoteInicial());
		estadoApatxaTextView.setText(estadoApatxa);
	}

}
