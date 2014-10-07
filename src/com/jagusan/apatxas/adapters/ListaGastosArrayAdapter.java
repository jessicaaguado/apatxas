package com.jagusan.apatxas.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;

public class ListaGastosArrayAdapter extends ArrayAdapter<ApatxaListado> {

	Context context;
	int rowLayoutId;
	List<ApatxaListado> apatxas;

	public ListaGastosArrayAdapter(Context context, int rowLayoutId, List<ApatxaListado> apatxas) {

		super(context, rowLayoutId, apatxas);

		this.context = context;
		this.rowLayoutId = rowLayoutId;
		this.apatxas = apatxas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(rowLayoutId, parent, false);
		}

		ApatxaListado apatxa = apatxas.get(position);
		Log.d("APATXAS"," ADAPTER: apatxa recuperada"+apatxa);
		TextView nombreGastoTextView = (TextView) convertView.findViewById(R.id.nombre);
		nombreGastoTextView.setText(apatxa.getNombre());
		TextView fechaGastoTextView = (TextView) convertView.findViewById(R.id.fecha);
		fechaGastoTextView.setText(apatxa.getFecha().toString());
		TextView estadoGastoTextView = (TextView) convertView.findViewById(R.id.estado);
		
		Double estadoGasto = apatxa.getGastoTotal() - apatxa.getPagado();
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
