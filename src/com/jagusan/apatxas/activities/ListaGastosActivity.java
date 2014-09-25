package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosArrayAdapter;
import com.jagusan.apatxas.sqlite.modelView.GastoListado;

public class ListaGastosActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_gastos);
		
		List<GastoListado> gastos = recuperarGastos();
		ListView listaGastosListView = (ListView) findViewById(R.id.lista_gastos);
		ListaGastosArrayAdapter adapter = new ListaGastosArrayAdapter(this, R.layout.lista_gastos_row, gastos);
		listaGastosListView.setAdapter(adapter);
		
//		OnVerDetalleProductoClickListener listener = new OnVerDetalleProductoClickListener();
//		productosListView.setOnItemClickListener(listener);
	}
	
	

	private List<GastoListado> recuperarGastos() {
		List<GastoListado> gastos = new ArrayList<GastoListado>();
		GastoListado gasto1 = new GastoListado();
		gasto1.setNombre("Comida en la sociedad de Ibon");
		gasto1.setFecha(new Date(2014,9,6));
		gasto1.setGastoTotal(new Double("500"));
		gasto1.setPagado(new Double("350"));
		gastos.add(gasto1);
		GastoListado gasto2 = new GastoListado();
		gasto2.setNombre("Despedida de Juanjo");
		gasto2.setFecha(new Date(2014,9,1));
		gasto2.setGastoTotal(new Double("300"));
		gasto2.setPagado(new Double("350"));
		gastos.add(gasto2);
		GastoListado gasto3 = new GastoListado();
		gasto3.setNombre("Regalo Mari Trini");
		gasto3.setFecha(new Date(2014,8,6));
		gasto3.setGastoTotal(new Double("100"));
		gasto3.setPagado(new Double("100"));
		gastos.add(gasto3);
		return gastos;
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
