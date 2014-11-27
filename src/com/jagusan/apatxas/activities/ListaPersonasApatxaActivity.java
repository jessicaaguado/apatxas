package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

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
import com.jagusan.apatxas.adapters.ListaPersonasApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.logicaNegocio.PersonaService;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;

public class ListaPersonasApatxaActivity extends ActionBarActivity {

	private final Boolean MOSTRAR_TITULO_PANTALLA = true;

	private Long idApatxa;
	private List<PersonaListado> personasApatxa;

	private ListView personasListView;
	private ViewGroup personasListViewHeader;
	private TextView tituloPersonasListViewHeader;
	private ListaPersonasApatxaArrayAdapter listaPersonasApatxaArrayAdapter;

	private Resources resources;
	private ApatxaService apatxaService;
	private PersonaService personaService;
	
	private int numPersonasApatxaAnadidas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lista_personas_apatxa);

		inicializarServicios();

		personalizarActionBar();

		recuperarDatosPasoAnterior();

		cargarElementosLayout();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lista_personas_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_guardar_lista_personas_apatxa) {
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_persona_apatxa, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_persona_apatxa_cambiar:
			irEdicionPersona();
			return true;
		case R.id.action_persona_apatxa_borrar:
			borrarPersona(info.position -1);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void irEdicionPersona() {
		// TODO Auto-generated method stub
		
	}

	private void inicializarServicios() {
		resources = getResources();
		apatxaService = new ApatxaService(this);
		personaService = new PersonaService(this);
	}

	private void personalizarActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
	}

	private void recuperarDatosPasoAnterior() {
		Intent intent = getIntent();
		personasApatxa = (ArrayList<PersonaListado>) intent.getSerializableExtra("personas");
		idApatxa = intent.getLongExtra("idApatxa", -1);
		numPersonasApatxaAnadidas = personasApatxa.size();
	}

	private void cargarElementosLayout() {
		personasListView = (ListView) findViewById(R.id.listaPersonasApatxa);
		anadirCabeceraListaPersonas(getLayoutInflater());

		listaPersonasApatxaArrayAdapter = new ListaPersonasApatxaArrayAdapter(this, R.layout.lista_personas_apatxa_row, personasApatxa);
		personasListView.setAdapter(listaPersonasApatxaArrayAdapter);
		registerForContextMenu(personasListView);
	}	

	private void anadirCabeceraListaPersonas(LayoutInflater inflater) {
		personasListViewHeader = (ViewGroup) inflater.inflate(R.layout.lista_personas_apatxa_header, personasListView, false);
		personasListView.addHeaderView(personasListViewHeader);
		tituloPersonasListViewHeader = (TextView) personasListViewHeader.findViewById(R.id.listaPersonasApatxaCabecera);
		actualizarTituloCabeceraListaPersonas();
	}

	private void actualizarTituloCabeceraListaPersonas() {
		String titulo = String.format(resources.getString(R.string.titulo_cabecera_lista_personas), personasApatxa.size());
		tituloPersonasListViewHeader.setText(titulo);
	}
	
	public void anadirPersona(View v) {
		String nombre = "Apatxero " + ++numPersonasApatxaAnadidas;
		PersonaListado persona = new PersonaListado();
		persona.setNombre(nombre);
		personasApatxa.add(persona);
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaPersonas();
	}

	public void borrarPersona(int posicion) {		
		personaService.borrarPersona(personasApatxa.get(posicion).getId());
		personasApatxa.clear();
		personasApatxa.addAll(personaService.getTodasPersonasApatxa(idApatxa));
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaPersonas();
	}
	
	public void irEdicionPersona(int posicion){
		
	}

	// private Boolean validacionesCorrectas() {
	// return true;
	// }

}
