package com.jagusan.apatxas.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.utils.CrearAvatarConLetra;
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
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(rowLayoutId, parent, false);
        }

        PersonaListadoReparto persona = personas.get(position);
        TextView nombrePersonaTextView = (TextView) convertView.findViewById(R.id.nombre);
        nombrePersonaTextView.setText(persona.nombre);
        TextView gastoPersonaTextView = (TextView) convertView.findViewById(R.id.totalGastoRepartoTexto);
        gastoPersonaTextView.setText(FormatearNumero.aDescripcionRepartoDineroEurosEnFuncionEstado(context.getResources(), persona.cantidadPago, persona.repartoPagado));

        TextView gastoPersonaCantidadTextView = (TextView) convertView.findViewById(R.id.totalGastoRepartoCantidad);
        gastoPersonaCantidadTextView.setText(FormatearNumero.aDineroEuros(context.getResources(), persona.cantidadPago));

        if (persona.repartoPagado) {
            nombrePersonaTextView.setTypeface(null, Typeface.NORMAL);
            gastoPersonaTextView.setTextColor(context.getResources().getColor(R.color.apatxascolors_color_reparto_pagado));
        } else {
            nombrePersonaTextView.setTypeface(null, Typeface.BOLD);
            gastoPersonaTextView.setTextColor(context.getResources().getColor(R.color.secondary_text_default_material_light));
        }

        ImageView fotoContactoImageView = (ImageView) convertView.findViewById(R.id.fotoContacto);
        if (persona.uriFoto != null) {
            fotoContactoImageView.setImageURI(Uri.parse(persona.uriFoto));
        } else {
            final int tamanoAvatar = context.getResources().getDimensionPixelSize(R.dimen.apatxas_persona_avatar_tamano);
            final Bitmap avatar = CrearAvatarConLetra.crearAvatarConLetra(context, persona.nombre, tamanoAvatar, tamanoAvatar);
            if (avatar != null) {
                fotoContactoImageView.setImageBitmap(avatar);
            } else {
                fotoContactoImageView.setVisibility(View.INVISIBLE);
            }
        }

        marcarSeleccion(convertView, persona);

        return convertView;
    }

    private void marcarSeleccion(View convertView, PersonaListadoReparto persona) {
        int colorFondo = (personasSeleccionadas.contains(persona)) ? context.getResources().getColor(R.color.apatxascolors_gris_claro) : Color.TRANSPARENT;
        convertView.setBackgroundColor(colorFondo);
        if (personasSeleccionadas.contains(persona)) {
            ImageView fotoContactoImageView = (ImageView) convertView.findViewById(R.id.fotoContacto);
            // fotoContactoImageView.setImageResource(R.drawable.ic_check_white_48dp);
        }

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
