package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jagusan.apatxas.R;

public class NuevoGastoApatxaActivity extends GastoApatxaActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.nuevo_gasto_apatxa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_asociar_gasto_apatxa) {
			Intent returnIntent = new Intent();
			
			String concepto = conceptoGastoEditText.getText().toString();			
			Double totalGasto = 0.0;
			try {
				totalGasto = Double.parseDouble(totalGastoEditText.getText().toString());
			} catch (Exception e) {
				// mantenemos total a 0
			}			
			Integer elementoSeleccionado = personasSpinner.getSelectedItemPosition() <= 0 ? null : personasSpinner.getSelectedItemPosition();
			
			returnIntent.putExtra("concepto",concepto);
			returnIntent.putExtra("total",totalGasto);
			if (elementoSeleccionado != null){				
				returnIntent.putExtra("pagadoPor", personasApatxa.get(elementoSeleccionado));
			}
			
			setResult(RESULT_OK,returnIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
	
}
