package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jagusan.apatxas.R;

public class NuevoApatxaPaso1Activity extends ActionBarActivity {
	
	private EditText nombreApatxaEditText;
	private EditText fechaApatxaEditText;
	private EditText boteInicialEditText;
	
	private ListView personasListView;
	private List<String> personasApatxa;
	private ArrayAdapter<String> listaPersonasApatxaArrayAdapter;
	
	private int numPersonasApatxaAnadidas = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_apatxa_paso1);
		
		personalizarActionBar();
		
		nombreApatxaEditText = (EditText) findViewById(R.id.nombreApatxa);
		fechaApatxaEditText = (EditText) findViewById(R.id.fechaApatxa);
		boteInicialEditText = (EditText) findViewById(R.id.boteInicialApatxa);
		
		personasListView = (ListView) findViewById(R.id.listaPersonasApatxa);
		personasApatxa = new ArrayList<String>();
		listaPersonasApatxaArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, personasApatxa);
		personasListView.setAdapter(listaPersonasApatxaArrayAdapter);

		//OnVerDetalleApatxaClickListener listener = new OnVerDetalleApatxaClickListener();
		//personasListView.setOnItemClickListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuevo_apatxa_paso1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_siguiente_paso) {
			continuarAnadirApatxas();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void anadirPersona(View v) {
		String nombre = "Apatxero "+ numPersonasApatxaAnadidas++;
//		Persona personaAnadida = new Persona();
//		personaAnadida.setNombre(nombre);
		personasApatxa.add(nombre);
		listaPersonasApatxaArrayAdapter.notifyDataSetChanged();
    }
	
	private void personalizarActionBar() {
		//quitamos el titulo
		getActionBar().setDisplayShowTitleEnabled(false);
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
		
		Intent intent = new Intent(this, NuevoApatxaPaso2Activity.class);
		intent.putExtra("titulo", titulo);
		intent.putExtra("fecha", fecha);
		intent.putExtra("boteInicial", boteInicial);
		intent.putStringArrayListExtra("personas", (ArrayList<String>) personasApatxa);
		
		startActivity(intent);
	}
}
