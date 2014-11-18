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
import android.view.Menu;
import android.view.MenuItem;
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
	private Long idApatxaDetalle;

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
	private ApatxaDetalle apatxaDetalle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inicializarServicios();
		setContentView(R.layout.activity_detalle_apatxa);

		personalizarActionBar();
		
		idApatxaDetalle = getIntent().getLongExtra("id", -1);

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
		getMenuInflater().inflate(R.menu.detalle_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_repartir_apatxa:
			apatxaService.realizarRepartoSiNecesario(apatxaDetalle);
			verReparto();
			return true;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void verReparto() {		
		Intent intent = new Intent(this, RepartoApatxaActivity.class);
		intent.putExtra("id", idApatxaDetalle);
		startActivity(intent);
	}

	private void cargarInformacionApatxa() {

		apatxaDetalle = apatxaService.getApatxaDetalle(idApatxaDetalle);
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
		String titulo = resources.getQuantityString(R.plurals.numero_personas_apatxa, numPersonas, numPersonas);
		numeroPersonasTextView.setText(titulo);

		// estado actual
		Double totalGastosApatxa = apatxaDetalle.getGastoTotal();
		String estadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaDetalle(getResources(), totalGastosApatxa, apatxaDetalle.getPagado(), apatxaDetalle.getBoteInicial());
		estadoApatxaTextView.setText(estadoApatxa);
		// gastos
		gastosApatxa = apatxaDetalle.getGastos();
		listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_detalle_apatxa_row, gastosApatxa);
		gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
		actualizarTituloCabeceraListaGastos(gastosApatxa.size(), totalGastosApatxa);

	}


	private void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		// quitamos el titulo
		actionBar.setDisplayShowTitleEnabled(false);
		// boton para ir a la actividad anterior
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	private void anadirCabeceraListaGastos() {
		gastosApatxaListViewHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.lista_gastos_detalle_apatxa_header, gastosApatxaListView, false);
		gastosApatxaListView.addHeaderView(gastosApatxaListViewHeader);
		tituloGastosApatxaListViewHeader = (TextView) gastosApatxaListViewHeader.findViewById(R.id.listaGastosDetalleApatxaCabecera);
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
