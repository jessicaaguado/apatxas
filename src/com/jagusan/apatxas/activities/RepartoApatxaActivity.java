package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.adapters.ListaPersonasRepartoApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.logicaNegocio.PersonaService;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.sqlite.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.utils.FormatearNumero;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

public class RepartoApatxaActivity extends ActionBarActivity {

	private ApatxaService apatxaService;
	private PersonaService personaService;
	Resources resources;
	private Long idApatxaReparto;

	private TextView nombreApatxaTextView;
	private TextView fechaApatxaTextView;
	private TextView boteInicialEditText;
	private TextView numeroPersonasTextView;
	private TextView estadoApatxaTextView;
	private TextView resumenGastosApatxaTextView;

	private ListView personasRepartoListView;
	private List<PersonaListadoReparto> listaPersonasReparto;
	private ListaPersonasRepartoApatxaArrayAdapter listaPersonasRepartoApatxaArrayAdapter;

	private ApatxaDetalle apatxaDetalle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inicializarServicios();
		setContentView(R.layout.activity_reparto_apatxa);

		personalizarActionBar();

		idApatxaReparto = getIntent().getLongExtra("id", -1);

		nombreApatxaTextView = (TextView) findViewById(R.id.nombreApatxaDetalle);
		fechaApatxaTextView = (TextView) findViewById(R.id.fechaApatxaDetalle);
		boteInicialEditText = (TextView) findViewById(R.id.boteInicialApatxaDetalle);
		numeroPersonasTextView = (TextView) findViewById(R.id.numeroPersonasApatxaDetalle);
		estadoApatxaTextView = (TextView) findViewById(R.id.estadoApatxaDetalle);

		resumenGastosApatxaTextView = (TextView) findViewById(R.id.resumenGastosApatxaDetalle);
		personasRepartoListView = (ListView) findViewById(R.id.listaDesgloseRepartoApatxa);

		cargarInformacionApatxa();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reparto_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void cargarInformacionApatxa() {
		apatxaDetalle = apatxaService.getApatxaDetalle(idApatxaReparto);
		cargarInformacionTitulo();
		cargarInformacionFecha();
		cargarInformacionBoteInicial();
		cargarInformacionPersonas();
		cargarInformacionEstado();
		cargarInformacionGastos();
		cargarInformacionReparto();
	}

	private void cargarInformacionTitulo() {
		nombreApatxaTextView.setText(apatxaDetalle.getNombre());
	}

	private void cargarInformacionFecha() {
		Date fecha = apatxaDetalle.getFecha();
		if (fecha != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fechaApatxaTextView.setText(sdf.format(fecha));
		}
	}

	private void cargarInformacionBoteInicial() {
		Double boteInicial = apatxaDetalle.getBoteInicial();
		boteInicialEditText.setText(FormatearNumero.aDineroEuros(resources, boteInicial));
	}

	private void cargarInformacionPersonas() {
		int numPersonas = apatxaDetalle.getPersonas().size();
		String titulo = resources.getQuantityString(R.plurals.numero_personas_apatxa, numPersonas, numPersonas);
		numeroPersonasTextView.setText(titulo);
	}

	private void cargarInformacionEstado() {
		String estadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaDetalle(getResources(), apatxaDetalle.getGastoTotal(), apatxaDetalle.getPagado(), apatxaDetalle.getBoteInicial());
		estadoApatxaTextView.setText(estadoApatxa);
	}

	private void cargarInformacionGastos() {
		resumenGastosApatxaTextView.setText(apatxaDetalle.getGastos().size() + " gastos. Total: " + FormatearNumero.aDineroEuros(resources, apatxaDetalle.getGastoTotal()));
	}

	private void cargarInformacionReparto() {
		listaPersonasReparto = apatxaService.getResultadoReparto(idApatxaReparto);
		listaPersonasRepartoApatxaArrayAdapter = new ListaPersonasRepartoApatxaArrayAdapter(this, R.layout.lista_personas_resultado_reparto_row, listaPersonasReparto);
		personasRepartoListView.setAdapter(listaPersonasRepartoApatxaArrayAdapter);
	}

	private void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		// quitamos el titulo
		actionBar.setDisplayShowTitleEnabled(false);
		// boton para ir a la actividad anterior
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	private void inicializarServicios() {
		apatxaService = new ApatxaService(this);
		personaService = new PersonaService(this);
		resources = getResources();
	}
}
