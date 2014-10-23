package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;

public class NuevoApatxaPaso2Activity extends ActionBarActivity {

	private ApatxaService apatxaService;

	private String tituloApatxa;
	private Long fechaApatxa;
	private Double boteInicialApatxa;
	private ArrayList<String> personasApatxa;

	private int NUEVO_GASTO_REQUEST_CODE = 1;

	private ListView gastosApatxaListView;
	private List<GastoApatxaListado> listaGastos = new ArrayList<GastoApatxaListado>();
	private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_apatxa_paso2);

		personalizarActionBar();

		gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxa);
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.lista_gastos_apatxa_header, gastosApatxaListView, false);
		ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.lista_gastos_apatxa_footer, gastosApatxaListView, false);
		gastosApatxaListView.addHeaderView(header, null, false);
		gastosApatxaListView.addFooterView(footer, null, false);
		listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_apatxa_row, listaGastos);
		gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);

		recuperarDatosPasoAnterior();

		apatxaService = new ApatxaService(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuevo_apatxa_paso2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_guardar) {
			guardarApatxa();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NUEVO_GASTO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Log.d("APATXAS", "Hemos anadido un gasto");
				String conceptoGasto = data.getStringExtra("concepto");
				Double totalGasto = data.getDoubleExtra("total", 0);
				String pagadoGasto = data.getStringExtra("pagadoPor");

				GastoApatxaListado gastoListado = new GastoApatxaListado(conceptoGasto, totalGasto, pagadoGasto);
				listaGastos.add(gastoListado);
				listaGastosApatxaArrayAdapter.notifyDataSetChanged();
			} else {
				Log.d("APATXAS", "Al final no se ha anadido ningun gasto");
			}
		}
	}

	private void personalizarActionBar() {
		// quitamos el titulo
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	private void recuperarDatosPasoAnterior() {
		Intent intent = getIntent();
		tituloApatxa = intent.getStringExtra("titulo");
		fechaApatxa = intent.getLongExtra("fecha", -1);
		boteInicialApatxa = intent.getDoubleExtra("boteInicial", 0);
		personasApatxa = intent.getStringArrayListExtra("personas");

	}

	public void anadirGasto(View v) {
		Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
		intent.putStringArrayListExtra("personas", personasApatxa);
		startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);
	}

	private void irListadoApatxasPrincipal() {
		Intent intent = new Intent(this, ListaApatxasActivity.class);
		startActivity(intent);
	}

	public void guardarApatxa() {
		apatxaService.crearApatxa(tituloApatxa, fechaApatxa, boteInicialApatxa);
		irListadoApatxasPrincipal();
	}
}
