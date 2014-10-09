package com.jagusan.apatxas.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

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
		
		//nombre
		TextView nombreApatxaTextView = (TextView) convertView.findViewById(R.id.nombre);
		nombreApatxaTextView.setText(apatxa.getNombre());
		//fecha
		TextView fechaApatxaTextView = (TextView) convertView.findViewById(R.id.fecha);
		Date fecha = apatxa.getFecha();
		if (fecha != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fechaApatxaTextView.setText(sdf.format(fecha));	
		}
		//estado
		TextView estadoApatxaTextView = (TextView) convertView.findViewById(R.id.estado);		
		String descripcionEstadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaListado(context.getResources(), apatxa.getGastoTotal(), apatxa.getPagado());
		estadoApatxaTextView.setText(descripcionEstadoApatxa);

		return convertView;
	}
	
}
