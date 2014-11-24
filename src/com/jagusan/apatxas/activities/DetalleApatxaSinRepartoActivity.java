package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.GastoService;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;

public class DetalleApatxaSinRepartoActivity extends DetalleApatxaActivity {

	private ListView gastosApatxaListView;	
	private TextView tituloGastosApatxaListViewHeader;
	private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;
	private List<GastoApatxaListado> gastosApatxa;
	
	private final int NUEVO_GASTO_REQUEST_CODE = 20;
	private final int EDITAR_GASTO_REQUEST_CODE = 21;
	
	private GastoService gastoService;

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
	
	@Override
	protected void inicializarServicios() {
		super.inicializarServicios();
		gastoService = new GastoService(this);
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
		gastosApatxa = apatxa.getGastos();
		listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_detalle_apatxa_row, gastosApatxa);
		gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
		actualizarTituloCabeceraListaGastos(apatxa.getGastos().size(), apatxa.getGastoTotal());
	}

	private void actualizarTituloCabeceraListaGastos(Integer numeroGastos, Double totalGastosApatxa) {
		String titulo = String.format(resources.getString(R.string.titulo_cabecera_lista_gastos_detalle_apatxa), totalGastosApatxa);
		tituloGastosApatxaListViewHeader.setText(titulo);
	}
	
	public void anadirGastoDetalleApatxa(View view){
		ArrayList<String> personas = new ArrayList<String>();
		List<PersonaListado> personasApatxa = apatxa.getPersonas();
		for (int i=0; i<personasApatxa.size(); i++){
			personas.add(personasApatxa.get(i).getNombre());
		}
		Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
		intent.putStringArrayListExtra("personas", personas);
		startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == NUEVO_GASTO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				guardarNuevoGastoApatxa(data);
			}
		}
		if (requestCode == EDITAR_GASTO_REQUEST_CODE){
			if (resultCode == RESULT_OK) {
		//		actualizarGastoListaDeGastos(data);
			}
		}
	}
	
	private void guardarNuevoGastoApatxa(Intent data) {
		String conceptoGasto = data.getStringExtra("concepto");
		Double totalGasto = data.getDoubleExtra("total", 0);
		String nombrePersonaPagadoGasto = data.getStringExtra("pagadoPor");
		Long idPersona = getIdPersonaPagadoGasto(apatxa.getPersonas(), nombrePersonaPagadoGasto);
		gastoService.crearGasto(conceptoGasto, totalGasto, idApatxa, idPersona);
		apatxaService.actualizarGastoTotalApatxa(idApatxa);		
		recargarInformacionGastos();
	}

	private void recargarInformacionGastos() {
		ApatxaDetalle apatxaActualizada = apatxaService.getApatxaDetalle(idApatxa);
		gastosApatxa.clear();
		gastosApatxa.addAll(apatxaActualizada.getGastos());		
		listaGastosApatxaArrayAdapter.notifyDataSetChanged();	
		actualizarTituloCabeceraListaGastos(apatxaActualizada.getGastos().size(), apatxaActualizada.getGastoTotal());
	}

	private Long getIdPersonaPagadoGasto(List<PersonaListado> personas, String nombrePersonaPagadoGasto) {
		Long idPersona = null;
		int i=0;
		while (nombrePersonaPagadoGasto != null && !nombrePersonaPagadoGasto.equals(personas.get(i).getNombre()) && i < personas.size()){
			i++;
		}
		if (nombrePersonaPagadoGasto != null && nombrePersonaPagadoGasto.equals(personas.get(i).getNombre())){
			idPersona = personas.get(i).getId();
		}
		return idPersona;
	}

}
