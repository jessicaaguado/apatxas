package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasRepartoApatxaArrayAdapter;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;
import com.jagusan.apatxas.sqlite.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.utils.FormatearNumero;

public class DetalleApatxaConRepartoActivity extends DetalleApatxaActivity {

	private TextView resumenGastosApatxaTextView;
	private ListView personasRepartoListView;
	
	private int EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE = 20;	

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
		case R.id.action_actualizar_reparto_apatxa:
			apatxaService.realizarReparto(apatxa);
			cargarInformacionApatxa();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

	public void irEditarListaGastosApatxa(View view) {
		Intent intent = new Intent(this, ListaGastosApatxaActivity.class);
		intent.putExtra("personas", new ArrayList<PersonaListado>(apatxa.getPersonas()));
		intent.putExtra("idApatxa", apatxa.getId());
		intent.putExtra("gastos", new ArrayList<GastoApatxaListado>(apatxa.getGastos()));
		startActivityForResult(intent, EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				actualizarReparto();				
			}
		}
		if (requestCode == EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				actualizarReparto();				
			}
		}
	}

	private void actualizarReparto() {
		apatxa = apatxaService.getApatxaDetalle(idApatxa);
		apatxaService.realizarReparto(apatxa);
		cargarInformacionApatxa();
		
	}

}
