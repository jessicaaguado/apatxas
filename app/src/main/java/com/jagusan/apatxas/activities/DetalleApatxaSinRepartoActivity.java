package com.jagusan.apatxas.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaGastosApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.GastoService;
import com.jagusan.apatxas.logicaNegocio.PersonaService;
import com.jagusan.apatxas.modelView.ApatxaDetalle;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.CalcularSumaTotalGastos;

public class DetalleApatxaSinRepartoActivity extends DetalleApatxaActivity {

	private ListView gastosApatxaListView;
	private TextView tituloGastosApatxaListViewHeader;
	private ListaGastosApatxaArrayAdapter listaGastosApatxaArrayAdapter;
	private List<GastoApatxaListado> gastosApatxa;

	private final int NUEVO_GASTO_REQUEST_CODE = 20;
	private final int EDITAR_GASTO_REQUEST_CODE = 21;

	private GastoService gastoService;
    private PersonaService personaService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detalle_apatxa_sin_reparto, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_repartir_apatxa:
			apatxaService.realizarRepartoSiNecesario(apatxa);
			verReparto();
			return true;
		case R.id.action_anadir_gasto:
			anadirGastoDetalleApatxa();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void irPantallaEdicionGasto(GastoApatxaListado gasto) {
		Intent intent = new Intent(this, EditarGastoApatxaActivity.class);
		intent.putExtra("conceptoGasto", gasto.getConcepto());
		intent.putExtra("importeGasto", gasto.getTotal());
		intent.putExtra("nombrePersonaPagadoGasto", gasto.getPagadoPor());
		intent.putExtra("personas",(ArrayList<PersonaListado>) apatxa.getPersonas());
		intent.putExtra("idGasto", gasto.getId());
		startActivityForResult(intent, EDITAR_GASTO_REQUEST_CODE);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_detalle_apatxa_sin_reparto);
	}

	@Override
	protected void inicializarServicios() {
		super.inicializarServicios();
		gastoService = new GastoService(this);
        personaService = new PersonaService(this);
	}

	private void verReparto() {
		Intent intent = new Intent(this, DetalleApatxaConRepartoActivity.class);
		intent.putExtra("id", idApatxa);
		startActivity(intent);
	}

	@Override
	protected void cargarElementosLayout() {
		super.cargarElementosLayout();
		gastosApatxaListView = (ListView) findViewById(R.id.listaGastosApatxa);
		tituloGastosApatxaListViewHeader = (TextView) findViewById(R.id.listaGastosApatxaCabecera);
	}

	@Override
	protected void cargarInformacionApatxa() {
		super.cargarInformacionApatxa();
		cargarInformacionGastos();
	}

	private void cargarInformacionGastos() {
		gastosApatxa = apatxa.getGastos();
		listaGastosApatxaArrayAdapter = new ListaGastosApatxaArrayAdapter(this, R.layout.lista_gastos_apatxa_row, gastosApatxa);
		gastosApatxaListView.setAdapter(listaGastosApatxaArrayAdapter);
        asignarContextualActionBar(gastosApatxaListView);
		actualizarTituloCabeceraListaGastos();
	}

	private void actualizarTituloCabeceraListaGastos() {
		String titulo = String.format(resources.getString(R.string.titulo_cabecera_lista_gastos_detalle_apatxa), CalcularSumaTotalGastos.calcular(gastosApatxa));
		tituloGastosApatxaListViewHeader.setText(titulo);
	}



	public void anadirGastoDetalleApatxa() {
		Intent intent = new Intent(this, NuevoGastoApatxaActivity.class);
		intent.putExtra("personas", (ArrayList<PersonaListado>)  apatxa.getPersonas());
		startActivityForResult(intent, NUEVO_GASTO_REQUEST_CODE);
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDITAR_INFORMACION_BASICA_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				cargarInformacionApatxa();
			}
		}		
		if (requestCode == NUEVO_GASTO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				guardarNuevoGastoApatxa(data);
			}
		}
		if (requestCode == EDITAR_GASTO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				actualizarGastoApatxa(data);
			}
		}		
		if (requestCode == EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				cargarInformacionApatxa();
			}
		}
	}

	private void guardarNuevoGastoApatxa(Intent data) {
		String conceptoGasto = data.getStringExtra("concepto");
		Double totalGasto = data.getDoubleExtra("total", 0);
		String nombrePersonaPagadoGasto = data.getStringExtra("pagadoPor");
		Long idPersona = personaService.recuperarIdPersonaConNombre(nombrePersonaPagadoGasto, idApatxa);
		gastoService.crearGasto(conceptoGasto, totalGasto, idApatxa, idPersona);
		recargarInformacionGastos();
	}

	private void borrarGastos() {
        gastoService.borrarGastos(listaGastosApatxaArrayAdapter.getGastosSeleccionados());
		recargarInformacionGastos();
	}

	private void actualizarGastoApatxa(Intent data) {
		String conceptoGasto = data.getStringExtra("concepto");
		Double totalGasto = data.getDoubleExtra("total", 0);
		String nombrePersonaPagadoGasto = data.getStringExtra("pagadoPor");
		Long idPersona =  personaService.recuperarIdPersonaConNombre(nombrePersonaPagadoGasto, idApatxa);
		Long idGasto = data.getLongExtra("idGasto", -1);

		gastoService.actualizarGasto(idGasto, conceptoGasto, totalGasto, idPersona);
		recargarInformacionGastos();
	}

	private void recargarInformacionGastos() {
		ApatxaDetalle apatxaActualizada = apatxaService.getApatxaDetalle(idApatxa);
		listaGastosApatxaArrayAdapter.clear();
        listaGastosApatxaArrayAdapter.addAll(apatxaActualizada.getGastos());
		actualizarTituloCabeceraListaGastos();
	}

    private void asignarContextualActionBar(final ListView gastosListView) {
        gastosListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        gastosListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ListaGastosApatxaArrayAdapter adapter = (ListaGastosApatxaArrayAdapter) gastosListView.getAdapter();
            ActionMode mode;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                adapter.toggleSeleccion(position, checked);
                int numeroGastosSeleccionados = adapter.numeroGastosSeleccionados();
                mode.setTitle("" + adapter.numeroGastosSeleccionados());
                if (numeroGastosSeleccionados == 1) {
                    findViewById(R.id.action_gasto_apatxa_cambiar).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.action_gasto_apatxa_cambiar).setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                this.mode = mode;
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu_gasto_apatxa, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if (this.mode == null){
                    this.mode = mode;
                }
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_gasto_apatxa_cambiar:
                        irPantallaEdicionGasto(listaGastosApatxaArrayAdapter.getGastosSeleccionados().get(0));
                        mode.finish();
                        return true;
                    case R.id.action_gasto_apatxa_borrar:
                        confimarBorradoGastos();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.resetearSeleccion();
            }

            private void confimarBorradoGastos() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(adapter.getContext());
                alertDialog.setMessage(R.string.mensaje_confirmacion_borrado_gastos);
                alertDialog.setPositiveButton(R.string.action_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        borrarGastos();
                        mode.finish();
                    }
                });
                alertDialog.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        });
    }

}
