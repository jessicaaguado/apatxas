package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.R.id;
import com.jagusan.apatxas.R.layout;
import com.jagusan.apatxas.R.menu;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.ApatxaService;
import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;
import com.jagusan.apatxas.utils.FormatearNumero;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class RepartoApatxaActivity extends ActionBarActivity {

	private ApatxaService apatxaService;
	Resources resources;
	private Long idApatxaReparto;

	private TextView nombreApatxaTextView;
	private TextView fechaApatxaTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inicializarServicios();
		setContentView(R.layout.activity_reparto_apatxa);

		personalizarActionBar();
		
		idApatxaReparto = getIntent().getLongExtra("id", -1);

		nombreApatxaTextView = (TextView) findViewById(R.id.nombreApatxaReparto);
		fechaApatxaTextView = (TextView) findViewById(R.id.fechaApatxaReparto);

		cargarInformacionApatxa();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reparto_apatxa, menu);
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

	private void cargarInformacionApatxa() {

		ApatxaDetalle apatxaDetalle = apatxaService.getApatxaDetalle(idApatxaReparto);
		// titulo
		nombreApatxaTextView.setText(apatxaDetalle.getNombre());
		// fecha
		Date fecha = apatxaDetalle.getFecha();
		if (fecha != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fechaApatxaTextView.setText(sdf.format(fecha));
		}

	}

	private void personalizarActionBar() {
		ActionBar actionBar = getSupportActionBar();
		// quitamos el titulo
		actionBar.setDisplayShowTitleEnabled(false);
		// boton para ir a la actividad anterior
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	private void inicializarServicios() {
		apatxaService = new ApatxaService(this);
		resources = getResources();
	}
}
