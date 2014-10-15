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
import com.jagusan.apatxas.sqlite.modelView.Persona;

public class ListaPersonasApatxaArrayAdapter extends ArrayAdapter<Persona> {

	Context context;
	int rowLayoutId;
	List<Persona> personas;

	public ListaPersonasApatxaArrayAdapter(Context context, int rowLayoutId, List<Persona> personas) {

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

		Persona persona = personas.get(position);		
		//nombre
		TextView nombrePersonaTextView = (TextView) convertView.findViewById(R.id.nombre);
		nombrePersonaTextView.setText(persona.getNombre());
		
		return convertView;
	}
	
}
