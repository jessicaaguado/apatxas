package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.logicaNegocio.GastoService;
import com.jagusan.apatxas.logicaNegocio.PersonaService;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;

public class NuevoApatxaPaso2Activity extends ActionBarActivity {

	private final Boolean MOSTRAR_TITULO_PANTALLA = true;

	private ApatxaService apatxaService;
	private PersonaService personaService;
	private GastoService gastoService;
	private Resources resources;

	private String tituloApatxa;
	private Long fechaApatxa;
	private Double boteInicialApatxa;
	private ArrayList<String> personasApatxa;
	private Double totalGastos = 0.0;

	private int NUEVO_GASTO_REQUEST_CODE = 1;
	private int EDITAR_GASTO_REQUEST_CODE = 2;

	private ListView gastosApatxaListView;
	private ViewGroup gastosApatxaListViewHeader;
	private TextView tituloGastosApatxaListViewHeader;
	private List<GastoApatxaListado> listaGastos = new ArrayList<GastoApatxaListado>();
	private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_apatxa_paso2);

		inicializarServicios();

		personalizarActionBar();

		cargarElementosLayout();

		recuperarDatosPasoAnterior();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.nuevo_apatxa_paso2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_guardar) {
			guardarApatxa();
			return true;
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
			GastoApatxaListado gasto = listaGastos.get(info.position - 1);			
			Intent intent = new Intent(this, EditarGastoApatxaActivity.class);
			intent.putExtra("conceptoGasto", gasto.getConcepto());
			intent.putExtra("importeGasto", gasto.getTotal());
			intent.putExtra("nombrePersonaPagadoGasto", gasto.getPagadoPor());
			intent.putStringArrayListExtra("personas", personasApatxa);
			intent.putExtra("posicionGastoEditar", info.position - 1);
			startActivityForResult(intent, EDITAR_GASTO_REQUEST_CODE);
			return true;
		case R.id.action_gasto_apatxa_borrar:
			borrarGasto(info.position);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NUEVO_GASTO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				anadirGastoAListaDeGastos(data);
			}
		}
		if (requestCode == EDITAR_GASTO_REQUEST_CODE){
			if (resultCode == RESULT_OK) {
				actualizarGastoListaDeGastos(data);
			}
		}
	}
	

	public void anadirGasto(View v) {
		Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
		intent.putStringArrayListExtra("personas", personasApatxa);
		startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);		
	}

	public void guardarApatxa() {
		Long idApatxa = apatxaService.crearApatxa(tituloApatxa, fechaApatxa, boteInicialApatxa);
		Map<String, Long> personas = new HashMap<String, Long>(personasApatxa.size());
		for (int i = 0; i < personasApatxa.size(); i++) {
			String nombrePersona = personasApatxa.get(i);
			Long idPersona = personaService.crearPersona(nombrePersona, idApatxa);
			personas.put(nombrePersona, idPersona);
		}
		for (int i = 0; i < listaGastos.size(); i++) {
			GastoApatxaListado gasto = listaGastos.get(i);
			String concepto = gasto.getConcepto();
			Double total = gasto.getTotal();
			Long idPersona = personas.get(gasto.getPagadoPor());
			gastoService.crearGasto(concepto, total, idApatxa, idPersona);
		}
		apatxaService.actualizarGastoTotalApatxa(idApatxa);
		irListadoApatxasPrincipal();
	}

	private void irListadoApatxasPrincipal() {
		Intent intent = new Intent(this, ListaApatxasActivity.class);
		//TODO finish?
		startActivity(intent);
	}

	private void inicializarServicios() {
		apatxaService = new ApatxaService(this);
		personaService = new PersonaService(this);
		gastoService = new GastoService(this);
		resources = getResources();
	}

	private void personalizarActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
	}

	private void cargarElementosLayout() {
		gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxa);
		anadirCabeceraListaGastos(getLayoutInflater());

		listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_apatxa_row, listaGastos);
		gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
		registerForContextMenu(gastosApatxaListView);
	}

	private void recuperarDatosPasoAnterior() {
		Intent intent = getIntent();
		tituloApatxa = intent.getStringExtra("titulo");
		fechaApatxa = intent.getLongExtra("fecha", -1);
		boteInicialApatxa = intent.getDoubleExtra("boteInicial", 0);
		personasApatxa = intent.getStringArrayListExtra("personas");
	}

	private void anadirCabeceraListaGastos(LayoutInflater inflater) {
		gastosApatxaListViewHeader = (ViewGroup) inflater.inflate(R.layout.lista_gastos_apatxa_header, gastosApatxaListView, false);
		gastosApatxaListView.addHeaderView(gastosApatxaListViewHeader);
		tituloGastosApatxaListViewHeader = (TextView) gastosApatxaListViewHeader.findViewById(R.id.listaGastosApatxaCabecera);
		actualizarTituloCabeceraListaGastos();
	}

	private void actualizarTituloCabeceraListaGastos() {
		String titulo = String.format(resources.getString(R.string.titulo_cabecera_lista_gastos), listaGastos.size(), totalGastos);
		tituloGastosApatxaListViewHeader.setText(titulo);
	}

	private void anadirGastoAListaDeGastos(Intent data) {
		String conceptoGasto = data.getStringExtra("concepto");
		Double totalGasto = data.getDoubleExtra("total", 0);
		String pagadoGasto = data.getStringExtra("pagadoPor");

		GastoApatxaListado gastoListado = new GastoApatxaListado(conceptoGasto, totalGasto, pagadoGasto);
		listaGastos.add(gastoListado);
		actualizarListaGastos(totalGasto, 0.0);
	}

	private void borrarGasto(int posicion) {
		Double importeGasto = listaGastos.get(posicion - 1).getTotal();
		listaGastos.remove(posicion - 1);
		actualizarListaGastos(0.0, importeGasto);
	}
	
	private void actualizarGastoListaDeGastos(Intent data) {
		String conceptoGasto = data.getStringExtra("concepto");
		Double totalGasto = data.getDoubleExtra("total", 0);
		String pagadoGasto = data.getStringExtra("pagadoPor");
		Integer posicionGastoActualizar = data.getIntExtra("posicionGastoEditar", -1);
		
		Double importeGastoAnterior = listaGastos.get(posicionGastoActualizar).getTotal();
		GastoApatxaListado gastoListado = new GastoApatxaListado(conceptoGasto, totalGasto, pagadoGasto);
		listaGastos.set(posicionGastoActualizar, gastoListado);
				
		actualizarListaGastos(totalGasto,importeGastoAnterior);		
	}

	private void actualizarListaGastos(Double importeGastoNuevo, Double importeGastoEliminar) {
		calcularTotalGastosApatxa(importeGastoNuevo, importeGastoEliminar);
		listaGastosApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaGastos();
	}
	
	private void calcularTotalGastosApatxa(Double importeGastoNuevo, Double importeGastoEliminar){
		totalGastos = totalGastos - importeGastoEliminar + importeGastoNuevo;
	}
}
