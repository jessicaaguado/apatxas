package com.jagusan.apatxas.activities;

import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
		getMenuInflater().inflate(R.menu.reparto_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		super.cargarInformacionApatxa();
		cargarInformacionGastos();
		cargarInformacionReparto();
	}

	private void cargarInformacionGastos() {
		resumenGastosApatxaTextView.setText(apatxa.getGastos().size() + " gastos. Total: " + FormatearNumero.aDineroEuros(resources, apatxa.getGastoTotal()));
	}

	private void cargarInformacionReparto() {
		List<PersonaListadoReparto> listaPersonasReparto = apatxaService.getResultadoReparto(idApatxa);
		ListaPersonasRepartoApatxaArrayAdapter listaPersonasRepartoApatxaArrayAdapter = new ListaPersonasRepartoApatxaArrayAdapter(this, R.layout.lista_personas_resultado_reparto_row,
				listaPersonasReparto);
		personasRepartoListView.setAdapter(listaPersonasRepartoApatxaArrayAdapter);
	}

}
