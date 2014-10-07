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
import com.jagusan.apatxas.sqlite.daos.ApatxaDAO;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;


public class ListaApatxasActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_apatxas);

		List<ApatxaListado> gastos = recuperarGastos();
		ListView listaGastosListView = (ListView) findViewById(R.id.lista_gastos);
		ListaGastosArrayAdapter adapter = new ListaGastosArrayAdapter(this, R.layout.lista_apatxas_row, gastos);
		listaGastosListView.setAdapter(adapter);

		// OnVerDetalleProductoClickListener listener = new
		// OnVerDetalleProductoClickListener();
		// productosListView.setOnItemClickListener(listener);
	}

	private List<ApatxaListado> recuperarGastos() {
		ApatxaDAO apatxaDAO = new ApatxaDAO(this);
		apatxaDAO.open();
		
		apatxaDAO.nuevoApatxa("prueba 1", 3, 4.1);

	    List<ApatxaListado> apatxas = apatxaDAO.getTodosApatxas();
//		List<ApatxaListado> gastos = new ArrayList<ApatxaListado>();
//		ApatxaListado gasto1 = new ApatxaListado();
//		gasto1.setNombre("Comida en la sociedad de Ibon");
//		gasto1.setFecha(new Date(2014, 9, 6));
//		gasto1.setGastoTotal(new Double("500"));
//		gasto1.setPagado(new Double("350"));
//		gastos.add(gasto1);
//		ApatxaListado gasto2 = new ApatxaListado();
//		gasto2.setNombre("Despedida de Juanjo");
//		gasto2.setFecha(new Date(2014, 9, 1));
//		gasto2.setGastoTotal(new Double("300"));
//		gasto2.setPagado(new Double("350"));
//		gastos.add(gasto2);
//		ApatxaListado gasto3 = new ApatxaListado();
//		gasto3.setNombre("Regalo Mari Trini");
//		gasto3.setFecha(new Date(2014, 8, 6));
//		gasto3.setGastoTotal(new Double("100"));
//		gasto3.setPagado(new Double("100"));
//		gastos.add(gasto3);
		return apatxas;
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
		Intent intent = new Intent(this,InformacionApatxaActivity.class);		
		startActivity(intent);		
	}
}
