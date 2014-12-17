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
import com.jagusan.apatxas.sqlite.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.utils.FormatearNumero;

public class ListaPersonasRepartoApatxaArrayAdapter extends ArrayAdapter<PersonaListadoReparto> {

	Context context;
	int rowLayoutId;
	List<PersonaListadoReparto> personas;

	public ListaPersonasRepartoApatxaArrayAdapter(Context context, int rowLayoutId, List<PersonaListadoReparto> personas) {

		super(context, rowLayoutId, personas);

		this.context = context;
		this.rowLayoutId = rowLayoutId;
		this.personas = personas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(rowLayoutId, parent, false);
		}

		PersonaListadoReparto persona = personas.get(position);
		TextView nombrePersonaTextView = (TextView) convertView.findViewById(R.id.nombre);
		nombrePersonaTextView.setText(persona.getNombre());
		TextView gastoPersonaTextView = (TextView) convertView.findViewById(R.id.totalGastoReparto);
		gastoPersonaTextView.setText(FormatearNumero.aDinero(context.getResources(), persona.getCantidadPago()));
		return convertView;
	}

}
