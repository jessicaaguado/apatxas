package com.jagusan.apatxas.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosArrayAdapter;
import com.jagusan.apatxas.listeners.OnVerDetalleApatxaClickListener;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;

public class ListaApatxasActivity extends ActionBarActivity {

	private ApatxaService apatxaService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lista_apatxas);
		personalizarActionBar();
		apatxaService = new ApatxaService(this);

		List<ApatxaListado> gastos = recuperarApatxas();
		ListView listaGastosListView = (ListView) findViewById(R.id.lista_gastos);
		ListaGastosArrayAdapter adapter = new ListaGastosArrayAdapter(this, R.layout.lista_apatxas_row, gastos);
		listaGastosListView.setAdapter(adapter);

		OnVerDetalleApatxaClickListener listener = new OnVerDetalleApatxaClickListener();
		listaGastosListView.setOnItemClickListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lista_apatxas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_anadir:
			irAnadirApatxa();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setElevation(0);
	}

	private void irAnadirApatxa() {
		Intent intent = new Intent(this, NuevoApatxaPaso1Activity.class);
		startActivity(intent);
	}

	private List<ApatxaListado> recuperarApatxas() {
		return apatxaService.getTodosApatxasListado();
	}
}
