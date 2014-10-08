package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.sqlite.daos.ApatxaDAO;

public class InformacionApatxaActivity extends ActionBarActivity {
	
	private ApatxaDAO apatxaDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informacion_apatxa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.informacion_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void guardarApatxa(View buttonView) {
		apatxaDAO = new ApatxaDAO(this);
		apatxaDAO.open();
		
		View informacionApatxaView = (View) buttonView.getParent();
		EditText tituloApatxaTextView = (EditText) informacionApatxaView.findViewById(R.id.tituloApatxa);
		String titulo = tituloApatxaTextView.getText().toString();
		
		EditText fechaApatxaTextView = (EditText) informacionApatxaView.findViewById(R.id.fechaApatxa);
		Long fecha = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
		    fecha = sdf.parse(fechaApatxaTextView.getText().toString()).getTime();	
		} catch (Exception e){
			//sin fecha
		}		
		
		
		EditText boteInicialTextView = (EditText) informacionApatxaView.findViewById(R.id.boteInicialApatxa);
		Double boteInicial = 0.0;
		try {
			boteInicial = Double.parseDouble(boteInicialTextView.getText().toString()); 
		} catch (Exception e){
			//mantenemos bote inicial a 0
		}
		
		apatxaDAO.nuevoApatxa(titulo, fecha, boteInicial);
		apatxaDAO.close();
		
		irListadoApatxasPrincipal();
		
	}
	
	private void irListadoApatxasPrincipal() {
		Intent intent = new Intent(this,ListaApatxasActivity.class);		
		startActivity(intent);		
	}
}

