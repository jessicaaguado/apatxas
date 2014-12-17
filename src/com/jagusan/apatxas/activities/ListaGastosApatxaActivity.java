package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.logicaNegocio.GastoService;
import com.jagusan.apatxas.logicaNegocio.PersonaService;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;

public class ListaGastosApatxaActivity extends ActionBarActivity {

	private final Boolean MOSTRAR_TITULO_PANTALLA = true;

	private int NUEVO_GASTO_REQUEST_CODE = 1;
	private int EDITAR_GASTO_REQUEST_CODE = 2;

	private Long idApatxa;	
	private ArrayList<String> nombresPersonasApatxa = new ArrayList<String>();
	private List<GastoApatxaListado> gastosAnadidos = new ArrayList<GastoApatxaListado>();
	private List<GastoApatxaListado> gastosEliminados = new ArrayList<GastoApatxaListado>();
	private List<GastoApatxaListado> gastosModificados = new ArrayList<GastoApatxaListado>();
	private Double totalGastos = 0.0;

	private ListView gastosApatxaListView;	
	private TextView tituloGastosApatxaListViewHeader;
	private List<GastoApatxaListado> listaGastos = new ArrayList<GastoApatxaListado>();
	private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;

	private ApatxaService apatxaService;
	private PersonaService personaService;
	private GastoService gastoService;
	private Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_apatxa_paso2);

		inicializarServicios();

		personalizarActionBar();

		recuperarDatosPasoAnterior();

		cargarElementosLayout();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lista_gastos_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		int id = item.getItemId();
		switch (id) {
		case R.id.action_guardar:
			actualizarGastosAnadidosBorradosActualizados();
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
			return true;
		case R.id.action_anadir_gasto:
			anadirGasto();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void actualizarGastosAnadidosBorradosActualizados() {
		Log.d("APATXAS"," 1.- BORRAR "+gastosEliminados.size());
		for (GastoApatxaListado gasto: gastosEliminados){
			Long idGasto = gasto.getId();
			if (idGasto == null){
				gastosAnadidos.remove(gasto);
			}else{	
				gastoService.borrarGasto(idGasto);				
			}
		}
		Log.d("APATXAS"," 2.- AÑADIR "+gastosAnadidos.size());
		for (GastoApatxaListado gasto : gastosAnadidos) {
			Long idPersonaPagado = personaService.recuperarIdPersonaConNombre(gasto.getPagadoPor(), idApatxa);
			Log.d("APATXAS", "id persona "+idPersonaPagado);
			gastoService.crearGasto(gasto.getConcepto(), gasto.getTotal(), idApatxa, idPersonaPagado);
		}
		Log.d("APATXAS"," 3.- ACTUALIZAR "+gastosModificados.size());
		for (GastoApatxaListado gasto: gastosModificados){
			Long idPersonaPagado = personaService.recuperarIdPersonaConNombre(gasto.getPagadoPor(), idApatxa);
			gastoService.actualizarGasto(gasto.getId(), gasto.getConcepto(), gasto.getTotal(), idPersonaPagado);
		}
		
		apatxaService.actualizarGastoTotalApatxa(idApatxa);		
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
		int posicionSeleccionada = info.position;
		switch (item.getItemId()) {
		case R.id.action_gasto_apatxa_cambiar:
			GastoApatxaListado gasto = listaGastos.get(posicionSeleccionada);			
			Intent intent = new Intent(this, EditarGastoApatxaActivity.class);
			intent.putExtra("conceptoGasto", gasto.getConcepto());
			intent.putExtra("importeGasto", gasto.getTotal());
			intent.putExtra("nombrePersonaPagadoGasto", gasto.getPagadoPor());
			intent.putStringArrayListExtra("personas", nombresPersonasApatxa);
			intent.putExtra("posicionGastoEditar", posicionSeleccionada);
			startActivityForResult(intent, EDITAR_GASTO_REQUEST_CODE);
			return true;
		case R.id.action_gasto_apatxa_borrar:
			borrarGasto(posicionSeleccionada);
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
	

	public void anadirGasto() {
		Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
		intent.putStringArrayListExtra("personas", nombresPersonasApatxa);
		startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);		
	}

//	public void guardarApatxa() {
//		Long idApatxa = apatxaService.crearApatxa(tituloApatxa, fechaApatxa, boteInicialApatxa);
//		Map<String, Long> personas = new HashMap<String, Long>(personasApatxa.size());
//		for (int i = 0; i < personasApatxa.size(); i++) {
//			String nombrePersona = personasApatxa.get(i);
//			Long idPersona = personaService.crearPersona(nombrePersona, idApatxa);
//			personas.put(nombrePersona, idPersona);
//		}
//		for (int i = 0; i < listaGastos.size(); i++) {
//			GastoApatxaListado gasto = listaGastos.get(i);
//			String concepto = gasto.getConcepto();
//			Double total = gasto.getTotal();
//			Long idPersona = personas.get(gasto.getPagadoPor());
//			gastoService.crearGasto(concepto, total, idApatxa, idPersona);
//		}
//		apatxaService.actualizarGastoTotalApatxa(idApatxa);
//		irListadoApatxasPrincipal();
//	}

//	private void irListadoApatxasPrincipal() {
//		Intent intent = new Intent(this, ListaApatxasActivity.class);
//		finish();
//		startActivity(intent);
//	}

	private void inicializarServicios() {
		apatxaService = new ApatxaService(this);
		personaService = new PersonaService(this);
		gastoService = new GastoService(this);
		resources = getResources();
	}

	private void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setElevation(0);
		actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
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
		List<PersonaListado> personasApatxa = (List<PersonaListado>) intent.getSerializableExtra("personas");
		for (PersonaListado persona:personasApatxa){
			nombresPersonasApatxa.add(persona.getNombre());
		}
		idApatxa = intent.getLongExtra("idApatxa", -1);
		listaGastos = (List<GastoApatxaListado>) intent.getSerializableExtra("gastos");
		for (GastoApatxaListado gasto: listaGastos){
			totalGastos += gasto.getTotal();
		}
	}

	private void anadirCabeceraListaGastos(LayoutInflater inflater) {
		tituloGastosApatxaListViewHeader = (TextView) findViewById(R.id.listaGastosApatxaCabecera);
		actualizarTituloCabeceraListaGastos();
	}

	private void actualizarTituloCabeceraListaGastos() {		
		int numGastos = listaGastos.size();
		String titulo = resources.getQuantityString(R.plurals.titulo_cabecera_lista_gastos, numGastos, numGastos, totalGastos);
		tituloGastosApatxaListViewHeader.setText(titulo);
	}

	private void anadirGastoAListaDeGastos(Intent data) {
		String conceptoGasto = data.getStringExtra("concepto");
		Double totalGasto = data.getDoubleExtra("total", 0);
		String pagadoGasto = data.getStringExtra("pagadoPor");

		GastoApatxaListado gastoListado = new GastoApatxaListado(conceptoGasto, totalGasto, pagadoGasto);
		gastosAnadidos.add(gastoListado);
		listaGastos.add(gastoListado);
		actualizarListaGastos(totalGasto, 0.0);
	}

	private void borrarGasto(int posicion) {
		GastoApatxaListado gastoBorrado = listaGastos.get(posicion);
		Log.d("APATXAS","Borrar gasto en posicion "+posicion+" "+gastoBorrado.getConcepto());
		Double importeGasto = gastoBorrado.getTotal();
		gastosEliminados.add(gastoBorrado);
		listaGastos.remove(posicion);
		actualizarListaGastos(0.0, importeGasto);
	}
	
	private void actualizarGastoListaDeGastos(Intent data) {
		String conceptoGasto = data.getStringExtra("concepto");
		Double totalGasto = data.getDoubleExtra("total", 0);
		String pagadoGasto = data.getStringExtra("pagadoPor");
		Integer posicionGastoActualizar = data.getIntExtra("posicionGastoEditar", -1);
		
		GastoApatxaListado gastoActualizado = listaGastos.get(posicionGastoActualizar);
		Double importeGastoAnterior = gastoActualizado.getTotal();
		gastoActualizado.setConcepto(conceptoGasto);
		gastoActualizado.setTotal(totalGasto);
		gastoActualizado.setPagadoPor(pagadoGasto);
		listaGastos.set(posicionGastoActualizar, gastoActualizado);
		gastosModificados.add(gastoActualizado);
		
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
