package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;

public class DetalleApatxaSinRepartoActivity extends DetalleApatxaActivity {

	private ListView gastosApatxaListView;	
	private TextView tituloGastosApatxaListViewHeader;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detalle_apatxa_sin_reparto, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_repartir_apatxa:
			apatxaService.realizarRepartoSiNecesario(apatxa);
			verReparto();
			return true;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_detalle_apatxa_sin_reparto);

	}

	private void verReparto() {
		Intent intent = new Intent(this, DetalleApatxaConRepartoActivity.class);
		intent.putExtra("id", idApatxa);
		startActivity(intent);
	}

	@Override
	protected void cargarElementosLayout() {
		super.cargarElementosLayout();
		gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxaDetalle);
		ViewGroup gastosApatxaListViewHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.lista_gastos_detalle_apatxa_header, gastosApatxaListView, false);
		gastosApatxaListView.addHeaderView(gastosApatxaListViewHeader);
		tituloGastosApatxaListViewHeader = (TextView) gastosApatxaListViewHeader.findViewById(R.id.listaGastosDetalleApatxaCabecera);
	}

	@Override
	protected void cargarInformacionApatxa() {
		super.cargarInformacionApatxa();
		cargarInformacionGastos();
	}

	private void cargarInformacionGastos() {		
		ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_detalle_apatxa_row, apatxa.getGastos());
		gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
		actualizarTituloCabeceraListaGastos(apatxa.getGastos().size(), apatxa.getGastoTotal());
	}

	private void actualizarTituloCabeceraListaGastos(Integer numeroGastos, Double totalGastosApatxa) {
		String titulo = String.format(resources.getString(R.string.titulo_cabecera_lista_gastos_detalle_apatxa), totalGastosApatxa);
		tituloGastosApatxaListViewHeader.setText(titulo);
	}

}
