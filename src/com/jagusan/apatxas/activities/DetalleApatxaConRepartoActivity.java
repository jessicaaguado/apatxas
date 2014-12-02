package com.jagusan.apatxas.activities;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasRepartoApatxaArrayAdapter;
import com.jagusan.apatxas.sqlite.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.utils.FormatearNumero;

public class DetalleApatxaConRepartoActivity extends DetalleApatxaActivity {

	private TextView resumenGastosApatxaTextView;
	private ListView personasRepartoListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detalle_apatxa_con_reparto, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_repartir_apatxa:
			apatxaService.realizarReparto(apatxa);
			recargar();
			return true;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void recargar() {
		finish();
		startActivity(getIntent());
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_detalle_apatxa_con_reparto);
	}

	@Override
	protected void cargarElementosLayout() {
		super.cargarElementosLayout();
		resumenGastosApatxaTextView = (TextView) findViewById(R.id.resumenGastosApatxaDetalle);
		personasRepartoListView = (ListView) findViewById(R.id.listaDesgloseRepartoApatxa);
	}

	@Override
	protected void cargarInformacionApatxa() {
		Log.d("APATXAS","Cargar informacion apatxa");
		super.cargarInformacionApatxa();
		cargarInformacionGastos();
		cargarInformacionReparto();
	}

	private void cargarInformacionGastos() {
		resumenGastosApatxaTextView.setText(apatxa.getGastos().size() + " gastos. Total: " + FormatearNumero.aDineroEuros(resources, apatxa.getGastoTotal()));
	}

	private void cargarInformacionReparto() {
		Log.d("APATXAS","Cargar informacion reparto");
		ViewGroup personasRepartoListViewHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.lista_personas_resultado_reparto_header, personasRepartoListView, false);
		personasRepartoListView.addHeaderView(personasRepartoListViewHeader);
		
		List<PersonaListadoReparto> listaPersonasReparto = apatxaService.getResultadoReparto(idApatxa);		
		ListaPersonasRepartoApatxaArrayAdapter listaPersonasRepartoApatxaArrayAdapter = new ListaPersonasRepartoApatxaArrayAdapter(this, R.layout.lista_personas_resultado_reparto_row,
				listaPersonasReparto);
		personasRepartoListView.setAdapter(listaPersonasRepartoApatxaArrayAdapter);
		
	}

}
