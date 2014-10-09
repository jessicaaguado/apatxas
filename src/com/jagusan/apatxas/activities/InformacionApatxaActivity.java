package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.sqlite.daos.ApatxaDAO;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

public class InformacionApatxaActivity extends ActionBarActivity {

	private ApatxaDAO apatxaDAO;
	private Long idApatxaActualizar;

	private EditText nombreApatxaEditText;
	private EditText fechaApatxaEditText;
	private EditText boteInicialEditText;
	
	private TextView gastoTotalApatxaTextView;
	private TextView estadoApatxaTextView;
	private TextView boteApatxaTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_informacion_apatxa);
		idApatxaActualizar = (long) -1.0;

		nombreApatxaEditText = (EditText) findViewById(R.id.nombreApatxa);
		fechaApatxaEditText = (EditText) findViewById(R.id.fechaApatxa);
		boteInicialEditText = (EditText) findViewById(R.id.boteInicialApatxa);
		
		gastoTotalApatxaTextView = (TextView) findViewById(R.id.gastoTotalApatxa);
		estadoApatxaTextView = (TextView) findViewById(R.id.estadoApatxa);
		boteApatxaTextView = (TextView) findViewById(R.id.boteApatxa);

		idApatxaActualizar = getIntent().getLongExtra("id", -1);
		if (esActualizarApatxa()) {
			cargarInformacionApatxa(idApatxaActualizar);
		}

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

		if (esActualizarApatxa()) {
			apatxaDAO.actualizarApatxa(idApatxaActualizar, titulo, fecha, boteInicial);
		} else {
			apatxaDAO.nuevoApatxa(titulo, fecha, boteInicial);
		}

		apatxaDAO.close();

		irListadoApatxasPrincipal();

	}

	private void cargarInformacionApatxa(Long idApatxa) {
		apatxaDAO = new ApatxaDAO(this);
		apatxaDAO.open();

		ApatxaDetalle apatxaDetalle = apatxaDAO.getApatxaDetalle(idApatxa);
		Log.d("APATXAS", "recuperado con id " + apatxaDetalle.toString());
		// titulo
		nombreApatxaEditText.setText(apatxaDetalle.getNombre());
		// fecha
		Date fecha = apatxaDetalle.getFecha();
		if (fecha != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fechaApatxaEditText.setText(sdf.format(fecha));
		}
		// bote inicial
		boteInicialEditText.setText(apatxaDetalle.getBoteInicial().toString());
		// gasto total
		gastoTotalApatxaTextView.setText(apatxaDetalle.getGastoTotal().toString());
		// estado actual
		String estadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaDetalle(getResources(), apatxaDetalle.getGastoTotal(), apatxaDetalle.getPagado(), apatxaDetalle.getBoteInicial());
		estadoApatxaTextView.setText(estadoApatxa);
		// bote final
		Double boteApatxa = apatxaDetalle.getBote();
		boteApatxaTextView.setText(boteApatxa.toString());

		apatxaDAO.close();

	}
	

	private void irListadoApatxasPrincipal() {
		Intent intent = new Intent(this, ListaApatxasActivity.class);
		startActivity(intent);
	}

	private Boolean esActualizarApatxa() {
		return idApatxaActualizar != -1;
	}

}
