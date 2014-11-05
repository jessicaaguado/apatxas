package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;
import com.jagusan.apatxas.utils.FormatearNumero;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

public class DetalleApatxaActivity extends ActionBarActivity {

	private ApatxaService apatxaService;
	private Long idApatxaActualizar;

	private TextView nombreApatxaTextView;
	private TextView fechaApatxaTextView;
	private TextView boteInicialEditText;
	private TextView numeroPersonasTextView;
	private TextView estadoApatxaTextView;

	private ListView gastosApatxaListView;
	private ViewGroup gastosApatxaListViewHeader;
	private TextView tituloGastosApatxaListViewHeader;
	private List<GastoApatxaListado> gastosApatxa = new ArrayList<GastoApatxaListado>();
	private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;

	Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inicializarServicios();
		setContentView(R.layout.activity_detalle_apatxa);

		personalizarActionBar();

		nombreApatxaTextView = (TextView) findViewById(R.id.nombreApatxaDetalle);
		fechaApatxaTextView = (TextView) findViewById(R.id.fechaApatxaDetalle);
		boteInicialEditText = (TextView) findViewById(R.id.boteInicialApatxaDetalle);
		numeroPersonasTextView = (TextView) findViewById(R.id.numeroPersonasApatxaDetalle);
		estadoApatxaTextView = (TextView) findViewById(R.id.estadoApatxaDetalle);

		gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxaDetalle);
		anadirCabeceraListaGastos();

		cargarInformacionApatxa();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.informacion_apatxa, menu);
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

	public void guardarApatxa(View buttonView) {

		String titulo = nombreApatxaTextView.getText().toString();

		Long fecha = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fecha = sdf.parse(fechaApatxaTextView.getText().toString()).getTime();
		} catch (Exception e) {
			// sin fecha
		}

		Double boteInicial = 0.0;
		try {
			boteInicial = Double.parseDouble(boteInicialEditText.getText().toString());
		} catch (Exception e) {
			// mantenemos bote inicial a 0
		}

		if (esActualizarApatxa()) {
			apatxaService.actualizarApatxa(idApatxaActualizar, titulo, fecha, boteInicial);
		} else {
			apatxaService.crearApatxa(titulo, fecha, boteInicial);
		}

		irListadoApatxasPrincipal();

	}

	private void cargarInformacionApatxa() {
		idApatxaActualizar = getIntent().getLongExtra("id", -1);

		ApatxaDetalle apatxaDetalle = apatxaService.getApatxaDetalle(idApatxaActualizar);		
		// titulo
		nombreApatxaTextView.setText(apatxaDetalle.getNombre());
		// fecha
		Date fecha = apatxaDetalle.getFecha();
		if (fecha != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fechaApatxaTextView.setText(sdf.format(fecha));
		}
		// bote inicial
		Double boteInicial = apatxaDetalle.getBoteInicial();
		boteInicialEditText.setText(FormatearNumero.aDineroEuros(resources, boteInicial));
		// personas
		int numPersonas = apatxaDetalle.getPersonas().size();
		String titulo =	resources.getQuantityString(R.plurals.numero_personas_apatxa, numPersonas, numPersonas);
		numeroPersonasTextView.setText(titulo);

		// estado actual
		Double totalGastosApatxa = apatxaDetalle.getGastoTotal();
		String estadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaDetalle(getResources(), totalGastosApatxa, apatxaDetalle.getPagado(), apatxaDetalle.getBoteInicial());
		estadoApatxaTextView.setText(estadoApatxa);
		// gastos
		gastosApatxa = apatxaDetalle.getGastos();
		listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_apatxa_row, gastosApatxa);
		gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
		actualizarTituloCabeceraListaGastos(gastosApatxa.size(), totalGastosApatxa);

	}

	private void irListadoApatxasPrincipal() {
		Intent intent = new Intent(this, ListaApatxasActivity.class);
		startActivity(intent);
	}

	private Boolean esActualizarApatxa() {
		return idApatxaActualizar != -1;
	}

	private void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		// quitamos el titulo
		actionBar.setDisplayShowTitleEnabled(false);
		// boton para ir a la actividad anterior
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	private void anadirCabeceraListaGastos() {
		gastosApatxaListViewHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.lista_gastos_apatxa_header, gastosApatxaListView, false);
		gastosApatxaListView.addHeaderView(gastosApatxaListViewHeader);
		tituloGastosApatxaListViewHeader = (TextView) gastosApatxaListViewHeader.findViewById(R.id.listaGastosApatxaCabecera);
	}

	private void actualizarTituloCabeceraListaGastos(Integer numeroGastos, Double totalGastosApatxa) {
		String titulo = String.format(resources.getString(R.string.titulo_cabecera_lista_gastos_detalle_apatxa), totalGastosApatxa);
		tituloGastosApatxaListViewHeader.setText(titulo);
	}

	private void inicializarServicios() {
		apatxaService = new ApatxaService(this);
		resources = getResources();
	}
}
