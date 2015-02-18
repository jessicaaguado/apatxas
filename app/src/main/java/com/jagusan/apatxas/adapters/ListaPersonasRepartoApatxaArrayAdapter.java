package com.jagusan.apatxas.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.utils.FormatearNumero;

import java.util.ArrayList;
import java.util.List;

public class ListaPersonasRepartoApatxaArrayAdapter extends ArrayAdapter<PersonaListadoReparto> {

    Context context;
    int rowLayoutId;
    List<PersonaListadoReparto> personas;

    private List<PersonaListadoReparto> personasSeleccionadas;

    public ListaPersonasRepartoApatxaArrayAdapter(Context context, int rowLayoutId, List<PersonaListadoReparto> personas) {

        super(context, rowLayoutId, personas);

        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.personas = personas;

        personasSeleccionadas = new ArrayList<>();
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
        nombrePersonaTextView.setText(persona.nombre);
        TextView gastoPersonaTextView = (TextView) convertView.findViewById(R.id.totalGastoReparto);
        gastoPersonaTextView.setText(FormatearNumero.aDescripcionRepartoDineroEuros(context.getResources(), persona.getCantidadPago()));
        ImageView indicadorRepartoPagadoImageView = (ImageView) convertView.findViewById(R.id.indicadorRepartoPagado);
        if (persona.getRepartoPagado()) {
            indicadorRepartoPagadoImageView.setImageResource(R.drawable.ic_apatxas_estado_persona_reparto_pagado);
        } else {
            indicadorRepartoPagadoImageView.setImageResource(R.drawable.ic_apatxas_estado_persona_reparto_pendiente);
        }

        ImageView fotoContactoImageView = (ImageView) convertView.findViewById(R.id.fotoContacto);
        if (persona.uriFoto != null) {
            fotoContactoImageView.setImageURI(Uri.parse(persona.uriFoto));
        } else {
            fotoContactoImageView.setImageResource(R.drawable.ic_apatxas_contacto_sin_foto);
        }

        marcarSeleccion(convertView, persona);

        return convertView;
    }

    private void marcarSeleccion(View convertView, PersonaListadoReparto persona) {
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
        personasSeleccionadas = new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<PersonaListadoReparto> getPersonasSeleccionadas() {
        return personasSeleccionadas;
    }

    public int numeroPersonasSeleccionadas() {
        return personasSeleccionadas.size();
    }


}
