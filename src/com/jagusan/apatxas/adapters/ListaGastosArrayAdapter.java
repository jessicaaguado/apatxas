package com.jagusan.apatxas.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.sqlite.modelView.GastoListado;

public class ListaGastosArrayAdapter extends ArrayAdapter<GastoListado> {

	Context context;
	int rowLayoutId;
	List<GastoListado> gastos;

	public ListaGastosArrayAdapter(Context context, int rowLayoutId, List<GastoListado> gastos) {

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

		GastoListado gasto = gastos.get(position);
		TextView nombreGastoTextView = (TextView) convertView.findViewById(R.id.nombre);
		nombreGastoTextView.setText(gasto.getNombre());
		TextView fechaGastoTextView = (TextView) convertView.findViewById(R.id.fecha);
		fechaGastoTextView.setText(gasto.getFecha().toString());
		TextView estadoGastoTextView = (TextView) convertView.findViewById(R.id.estado);
		
		Double estadoGasto = gasto.getGastoTotal() - gasto.getPagado();
		Resources res = context.getResources();
		String descripcionEstadoGasto = res.getString(R.string.estado_gasto_pagado);		
		if (estadoGasto < 0) {
			descripcionEstadoGasto = String.format(res.getString(R.string.estado_gasto_sobra),estadoGasto*-1);
		} else if (estadoGasto > 0) {
			descripcionEstadoGasto = String.format(res.getString(R.string.estado_gasto_falta_pagar),estadoGasto);
		}

		estadoGastoTextView.setText(descripcionEstadoGasto);

		return convertView;
	}

}
