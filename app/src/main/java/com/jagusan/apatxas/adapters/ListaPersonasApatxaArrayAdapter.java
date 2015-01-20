package com.jagusan.apatxas.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.sqlite.modelView.PersonaListado;

public class ListaPersonasApatxaArrayAdapter extends ArrayAdapter<PersonaListado> {

    Context context;
    int rowLayoutId;
    List<PersonaListado> personas;

    private List<PersonaListado> personasSeleccionadas;

    public ListaPersonasApatxaArrayAdapter(Context context, int rowLayoutId, List<PersonaListado> personas) {

        super(context, rowLayoutId, personas);

        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.personas = personas;
        personasSeleccionadas = new ArrayList<PersonaListado>();
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

        marcarSeleccion(convertView, persona);

        return convertView;
    }

    private void marcarSeleccion(View convertView, PersonaListado persona) {
        int colorFondo = (personasSeleccionadas.contains(persona)) ? context.getResources().getColor(R.color.apatxascolors_gris_claro) : Color.TRANSPARENT;
        convertView.setBackgroundColor(colorFondo);
    }

    public void toggleSeleccion(Integer position, boolean checked) {
        if (checked) {
            personasSeleccionadas.add(personas.get(position));
        } else {
            personasSeleccionadas.remove(personas.get(position));
        }
        notifyDataSetChanged();
    }

    public void resetearSeleccion() {
        personasSeleccionadas = new ArrayList<PersonaListado>();
        notifyDataSetChanged();
    }

    public List<PersonaListado> getPersonasSeleccionadas() {
        return personasSeleccionadas;
    }

    public int numeroPersonasSeleccionadas(){
        return personasSeleccionadas.size();
    }

    public void eliminarPersonasSeleccionadas() {
        for (PersonaListado personaEliminar : personasSeleccionadas) {
            personas.remove(personaEliminar);
        }
        resetearSeleccion();
    }
}
