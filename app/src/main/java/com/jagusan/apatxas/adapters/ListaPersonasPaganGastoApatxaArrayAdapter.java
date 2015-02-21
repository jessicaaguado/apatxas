package com.jagusan.apatxas.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.CrearAvatarConLetra;

import java.util.List;

public class ListaPersonasPaganGastoApatxaArrayAdapter extends ArrayAdapter<PersonaListado> {

    Context context;
    int rowLayoutId;
    List<PersonaListado> personas;

    public ListaPersonasPaganGastoApatxaArrayAdapter(Context context, int rowLayoutId, List<PersonaListado> personas) {

        super(context, rowLayoutId, personas);

        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.personas = personas;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(rowLayoutId, parent, false);
        }

        PersonaListado persona = personas.get(position);
        // nombre
        TextView nombrePersonaTextView = (TextView) convertView.findViewById(android.R.id.text1);
        nombrePersonaTextView.setText(persona.nombre);

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
        return convertView;
    }


}
