package com.jagusan.apatxas.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.utils.FormatearNumero;

public class ListaGastosApatxaArrayAdapter extends ArrayAdapter<GastoApatxaListado> {

	Context context;
	int rowLayoutId;
	List<GastoApatxaListado> gastos;

    private List<GastoApatxaListado> gastosSeleccionados;

	public ListaGastosApatxaArrayAdapter(Context context, int rowLayoutId, List<GastoApatxaListado> gastos) {

		super(context, rowLayoutId, gastos);

		this.context = context;
		this.rowLayoutId = rowLayoutId;
		this.gastos = gastos;
        gastosSeleccionados = new ArrayList<GastoApatxaListado>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(rowLayoutId, parent, false);
		}

		GastoApatxaListado gasto = gastos.get(position);
		// titulo
		TextView conceptoGastoTextView = (TextView) convertView.findViewById(R.id.concepto);
		conceptoGastoTextView.setText(gasto.getConcepto());
		// total
		TextView totalGastoTextView = (TextView) convertView.findViewById(R.id.total);
		totalGastoTextView.setText(FormatearNumero.aDinero(context.getResources(), gasto.getTotal()));
		// quien lo ha pagado
		TextView pagadorGastoTextView = (TextView) convertView.findViewById(R.id.pagadoPor);
		if (pagadorGastoTextView != null) {
			String pagador = gasto.getPagadoPor() != null ? gasto.getPagadoPor() : context.getResources().getString(R.string.sin_pagar);
			pagadorGastoTextView.setText(pagador);
		}

        marcarSeleccion(convertView, gasto);

		return convertView;
	}

    private void marcarSeleccion(View convertView, GastoApatxaListado gasto) {
        int colorFondo = (gastosSeleccionados.contains(gasto)) ? context.getResources().getColor(R.color.apatxascolors_gris_claro) : Color.TRANSPARENT;
        convertView.setBackgroundColor(colorFondo);
    }

    public void toggleSeleccion(Integer position, boolean checked) {
        if (checked) {
            gastosSeleccionados.add(gastos.get(position));
        } else {
            gastosSeleccionados.remove(gastos.get(position));
        }
        notifyDataSetChanged();
    }

    public void resetearSeleccion() {
        gastosSeleccionados = new ArrayList<GastoApatxaListado>();
        notifyDataSetChanged();
    }

    public void actualizarGasto(Integer posicionGastoActualizar, GastoApatxaListado gastoActualizado) {
        gastos.set(posicionGastoActualizar, gastoActualizado);
        notifyDataSetChanged();
    }

    public List<GastoApatxaListado> getGastosSeleccionados() {
        return gastosSeleccionados;
    }


    public int numeroGastosSeleccionados(){
        return gastosSeleccionados.size();
    }


    public void eliminarGastosSeleccionados() {
        for (GastoApatxaListado gastoEliminar : gastosSeleccionados) {
            gastos.remove(gastoEliminar);
        }
        resetearSeleccion();
    }

    
}
