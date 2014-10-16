package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jagusan.apatxas.R;

public class NuevoGastoApatxaActivity extends ActionBarActivity {
	
	private EditText tituloGastoEditText;
	private EditText totalGastoEditText;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_gasto_apatxa);
		
		tituloGastoEditText = (EditText) findViewById(R.id.titulo);
		totalGastoEditText = (EditText) findViewById(R.id.totalGasto);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuevo_gasto_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_asociar_gasto_apatxa) {
			Intent returnIntent = new Intent();
			
			String titulo = tituloGastoEditText.getText().toString();			
			Double totalGasto = 0.0;
			try {
				totalGasto = Double.parseDouble(totalGastoEditText.getText().toString());
			} catch (Exception e) {
				// mantenemos total a 0
			}			
						
			returnIntent.putExtra("titulo",titulo);
			returnIntent.putExtra("total",totalGasto);
			
			setResult(RESULT_OK,returnIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
