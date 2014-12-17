package com.jagusan.apatxas.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;
import com.jagusan.apatxas.utils.FormatearNumero;

public class ListaGastosApatxaArrayAdapter extends ArrayAdapter<GastoApatxaListado> {

	Context context;
	int rowLayoutId;
	List<GastoApatxaListado> gastos;

	public ListaGastosApatxaArrayAdapter(Context context, int rowLayoutId, List<GastoApatxaListado> gastos) {

		super(context, rowLayoutId, gastos);

		this.context = context;
		this.rowLayoutId = rowLayoutId;
		this.gastos = gastos;
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

		return convertView;
	}

}
