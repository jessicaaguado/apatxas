package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
import com.jagusan.apatxas.adapters.ListaPersonasApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.GastoService;
import com.jagusan.apatxas.logicaNegocio.PersonaService;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;

public class ListaPersonasApatxaActivity extends ActionBarActivity {

	private final Boolean MOSTRAR_TITULO_PANTALLA = true;

	private Long idApatxa;
	private List<PersonaListado> personasApatxa;
	private List<String> nombresPersonasAnadidas = new ArrayList<String>();
	private List<PersonaListado> personasEliminadas = new ArrayList<PersonaListado>();

	private ListView personasListView;
	private TextView tituloPersonasListViewHeader;
	private ListaPersonasApatxaArrayAdapter listaPersonasApatxaArrayAdapter;

	private Resources resources;
	private PersonaService personaService;
	private GastoService gastoService;

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
		switch (id) {
		case R.id.action_guardar_lista_personas_apatxa:
			actualizarPersonasAnadidasBorradas();
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
			return true;
		case R.id.action_anadir_persona:
			anadirPersonaParaGuardar();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void actualizarPersonasAnadidasBorradas() {
		for (PersonaListado persona: personasEliminadas){
			Long idPersona = persona.getId();
			if (idPersona == null){
				nombresPersonasAnadidas.remove(persona.getNombre());
			}else{				
				personaService.borrarPersona(persona.getId());
			}
		}
		for (String nombre : nombresPersonasAnadidas) {
			personaService.crearPersona(nombre, idApatxa);
		}
		
		
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
		case R.id.action_persona_apatxa_borrar:
			anadirPersonaParaBorrar(info.position - 1);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void inicializarServicios() {
		resources = getResources();
		personaService = new PersonaService(this);
		gastoService = new GastoService(this);
	}

	private void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setElevation(0);
		actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
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
		tituloPersonasListViewHeader = (TextView) findViewById(R.id.listaPersonasApatxaCabecera);
		actualizarTituloCabeceraListaPersonas();
	}

	private void actualizarTituloCabeceraListaPersonas() {
		int numPersonas = personasApatxa.size();
		String titulo = resources.getQuantityString(R.plurals.titulo_cabecera_lista_personas, numPersonas, numPersonas);
		tituloPersonasListViewHeader.setText(titulo);
	}

	public void anadirPersonaParaGuardar() {
		String nombre = "Apatxero " + ++numPersonasApatxaAnadidas;
		PersonaListado nuevaPersona = new PersonaListado();
		nuevaPersona.setNombre(nombre);
		personasApatxa.add(nuevaPersona);
		nombresPersonasAnadidas.add(nombre);
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaPersonas();
	}

	public void anadirPersonaParaBorrar(int posicion) {
		PersonaListado personaSeleccionada = personasApatxa.get(posicion);
		Long idPersona = personaSeleccionada.getId();
		if (gastoService.hayGastosAsociadosA(idPersona)) {
			confimarBorradoPersonaConGastosAsociados(personaSeleccionada);
		} else {
			anadirPersonaListaBorrado(personaSeleccionada);
		}
	}

	private void confimarBorradoPersonaConGastosAsociados(final PersonaListado persona) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setMessage(R.string.mensaje_aviso_borrar_persona_con_gastos);
		alertDialog.setPositiveButton(R.string.action_borrar, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				anadirPersonaListaBorrado(persona);
			}
		});
		alertDialog.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		alertDialog.create();
		alertDialog.show();
	}

	private void anadirPersonaListaBorrado(PersonaListado persona) {
		personasApatxa.remove(persona);		
		personasEliminadas.add(persona);
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaPersonas();
	}

}
