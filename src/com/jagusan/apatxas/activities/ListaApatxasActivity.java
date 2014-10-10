package com.jagusan.apatxas.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_gastos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_buscar:
			Log.d("APATXAS", "buscar");
			return true;
		case R.id.action_anadir:
			Log.d("APATXAS", "anadir");
			irAnadirApatxa();
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void irAnadirApatxa() {
		Intent intent = new Intent(this, InformacionApatxaActivity.class);
		startActivity(intent);
	}

	private List<ApatxaListado> recuperarApatxas() {
		return apatxaService.getTodosApatxasListado();
	}
}
