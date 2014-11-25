package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_gasto_apatxa, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_gasto_apatxa_cambiar:
			GastoApatxaListado gasto = gastosApatxa.get(info.position - 1);			
			irPantallaEdicionGasto(gasto);
			return true;
		case R.id.action_gasto_apatxa_borrar:
			borrarGastoApatxa(info.position -1);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void irPantallaEdicionGasto(GastoApatxaListado gasto) {
		Intent intent = new Intent(this, EditarGastoApatxaActivity.class);
		intent.putExtra("conceptoGasto", gasto.getConcepto());
		intent.putExtra("importeGasto", gasto.getTotal());
		intent.putExtra("nombrePersonaPagadoGasto", gasto.getPagadoPor());
		intent.putStringArrayListExtra("personas", this.obtenerListaNombresPersonasApatxa());
		intent.putExtra("idGasto", gasto.getId());
		startActivityForResult(intent, EDITAR_GASTO_REQUEST_CODE);
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
		registerForContextMenu(gastosApatxaListView);
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
		ArrayList<String> nombresPersonas = obtenerListaNombresPersonasApatxa();
		Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
		intent.putStringArrayListExtra("personas", nombresPersonas);
		startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);
	}

	private ArrayList<String> obtenerListaNombresPersonasApatxa() {
		ArrayList<String> nombresPersonas = new ArrayList<String>();
		List<PersonaListado> personasApatxa = apatxa.getPersonas();
		for (int i=0; i<personasApatxa.size(); i++){
			nombresPersonas.add(personasApatxa.get(i).getNombre());
		}
		return nombresPersonas;
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
				actualizarGastoApatxa(data);
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
	
	private void borrarGastoApatxa(int posicion) {		
		gastoService.borrarGasto(gastosApatxa.get(posicion).getId());
		apatxaService.actualizarGastoTotalApatxa(idApatxa);		
		recargarInformacionGastos();
	}
	
	private void actualizarGastoApatxa(Intent data) {
		String conceptoGasto = data.getStringExtra("concepto");
		Double totalGasto = data.getDoubleExtra("total", 0);
		String nombrePersonaPagadoGasto = data.getStringExtra("pagadoPor");
		Long idPersona = getIdPersonaPagadoGasto(apatxa.getPersonas(), nombrePersonaPagadoGasto);
		Long idGasto = data.getLongExtra("idGasto", -1);
		
		gastoService.actualizarGasto(idGasto, conceptoGasto, totalGasto, idPersona);
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
