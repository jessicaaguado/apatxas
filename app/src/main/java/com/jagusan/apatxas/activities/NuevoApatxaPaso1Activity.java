package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.ValidacionActivity;

public class NuevoApatxaPaso1Activity extends ActionBarActivity {

	private final Boolean MOSTRAR_TITULO_PANTALLA = true;

	private EditText nombreApatxaEditText;
	private EditText fechaApatxaEditText;
	private EditText boteInicialEditText;
	private CheckBox descontarBoteInicialCheckBox;

	private ListView personasListView;
	private TextView tituloPersonasListViewHeader;
	private List<String> personasApatxa = new ArrayList<String>();
	private ArrayAdapter<String> listaPersonasApatxaArrayAdapter;

	private Resources resources;

	private int numPersonasApatxaAnadidas = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_apatxa_paso1);

		inicializarServicios();

		personalizarActionBar();

		cargarElementosLayout();
		inicializarElementosLayout();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.nuevo_apatxa_paso1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_siguiente_paso:
			continuarAnadirApatxas();
			return true;
		case R.id.action_anadir_persona:
			anadirPersona();
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
		int posicionSeleccionada = info.position;
		switch (item.getItemId()) {		
		case R.id.action_persona_apatxa_borrar:
			borrarPersona(posicionSeleccionada);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
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

	public void anadirPersona() {
		String nombre = "Apatxero " + ++numPersonasApatxaAnadidas;
		personasApatxa.add(nombre);
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaPersonas();
	}

	public void borrarPersona(int posicion) {
		personasApatxa.remove(posicion);
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
		actualizarTituloCabeceraListaPersonas();
	}

	private void continuarAnadirApatxas() {
		String titulo = nombreApatxaEditText.getText().toString();
		Long fecha = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fecha = sdf.parse(fechaApatxaEditText.getText().toString()).getTime();
		} catch (Exception e) {
			// sin fecha
		}
		Double boteInicial = 0.0;
		try {
			boteInicial = Double.parseDouble(boteInicialEditText.getText().toString());
		} catch (Exception e) {
			// mantenemos bote inicial a 0
		}
		Boolean descontarBoteInicial = descontarBoteInicialCheckBox.isChecked();
		if (validacionesCorrectas()) {
			Intent intent = new Intent(this, NuevoApatxaPaso2Activity.class);
			intent.putExtra("titulo", titulo);
			intent.putExtra("fecha", fecha);
			intent.putExtra("boteInicial", boteInicial);
			intent.putExtra("descontarBoteInicial", descontarBoteInicial);
			intent.putStringArrayListExtra("personas", (ArrayList<String>) personasApatxa);

			startActivity(intent);
		}
	}

	private Boolean validacionesCorrectas() {
		Boolean tituloOk = ValidacionActivity.validarTituloObligatorio(nombreApatxaEditText, resources);
		Boolean fechaOk = ValidacionActivity.validarFechaObligatoria(fechaApatxaEditText, resources);
		return tituloOk && fechaOk;
	}

	private void inicializarServicios() {
		resources = getResources();
	}

	private void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setElevation(0);
		actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
	}

	private void cargarElementosLayout() {
		nombreApatxaEditText = (EditText) findViewById(R.id.nombreApatxa);
		fechaApatxaEditText = (EditText) findViewById(R.id.fechaApatxa);
		boteInicialEditText = (EditText) findViewById(R.id.boteInicialApatxa);
		descontarBoteInicialCheckBox = (CheckBox) findViewById(R.id.descontarBoteInicial);

		personasListView = (ListView) findViewById(R.id.listaPersonasApatxa);
		anadirCabeceraListaPersonas(getLayoutInflater());

		listaPersonasApatxaArrayAdapter = new ArrayAdapter<String>(this, R.layout.lista_personas_apatxa_row, personasApatxa);
		personasListView.setAdapter(listaPersonasApatxaArrayAdapter);
		registerForContextMenu(personasListView);
	}

	private void inicializarElementosLayout() {
		fechaApatxaEditText.setText(FormatearFecha.formatearHoy(resources));
	}

}