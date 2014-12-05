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
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
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

	private ListView personasListView;
	private ViewGroup personasListViewHeader;
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
		case R.id.action_persona_apatxa_borrar:
			borrarPersona(info.position - 1);
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
		personaService.crearPersona(nombre, idApatxa);
		personasApatxa.clear();
		personasApatxa.addAll(personaService.getTodasPersonasApatxa(idApatxa));
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaPersonas();
	}

	public void borrarPersona(int posicion) {
		Long idPersona = personasApatxa.get(posicion).getId();
		if (gastoService.hayGastosAsociadosA(idPersona)) {
			confimarBorradoPersonaConGastosAsociados(idPersona);
		} else {
			borrarPersonaYRecargar(idPersona);
		}
	}

	private void confimarBorradoPersonaConGastosAsociados(final Long idPersona) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);		
		alertDialog.setMessage(R.string.mensaje_aviso_borrar_persona_con_gastos);
		alertDialog.setPositiveButton(R.string.action_borrar, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				borrarPersonaYRecargar(idPersona);
			}
		});
		alertDialog.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		alertDialog.create();
		alertDialog.show();
	}

	private void borrarPersonaYRecargar(Long idPersona) {
		personaService.borrarPersona(idPersona);
		personasApatxa.clear();
		personasApatxa.addAll(personaService.getTodasPersonasApatxa(idApatxa));
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaPersonas();
	}

}
