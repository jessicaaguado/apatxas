package com.jagusan.apatxas.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.sqlite.modelView.PersonaListado;

public class ListaPersonasApatxaArrayAdapter extends ArrayAdapter<PersonaListado> {

	Context context;
	int rowLayoutId;
	List<PersonaListado> personas;

	public ListaPersonasApatxaArrayAdapter(Context context, int rowLayoutId, List<PersonaListado> personas) {

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

		PersonaListado persona = personas.get(position);
		// nombre
		TextView nombrePersonaTextView = (TextView) convertView.findViewById(android.R.id.text1);
		nombrePersonaTextView.setText(persona.getNombre());

		return convertView;
	}

}
