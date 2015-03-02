package com.jagusan.apatxas.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.ContactoListado;
import com.jagusan.apatxas.utils.CrearAvatarConLetra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ListaContactosArrayAdapter extends ArrayAdapter<ContactoListado> implements SectionIndexer {

    Context context;
    int rowLayoutId;
    List<ContactoListado> contactos;

    List<ContactoListado> contactosSeleccionados;

    HashMap<String, Integer> alphaIndexer;
    String[] sections;

    public ListaContactosArrayAdapter(Context context, int rowLayoutId, List<ContactoListado> contactos) {

        super(context, rowLayoutId, contactos);

        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.contactos = contactos;
        this.contactosSeleccionados = new ArrayList<>();


        String indiceYo = this.getContext().getResources().getString(R.string.yo_mayusculas);
        int posicionYo = 0;
        alphaIndexer = new HashMap<>();
        for (ContactoListado contacto : contactos) {
            if (contacto.id != Long.MIN_VALUE) {
                String inicial = contacto.nombre.substring(0, 1).toUpperCase();
                int posicionActual = contactos.indexOf(contacto);
                int posicionAnterior = alphaIndexer.get(inicial) != null ? alphaIndexer.get(inicial) : Integer.MAX_VALUE;
                if (posicionActual < posicionAnterior) {
                    alphaIndexer.put(inicial, posicionActual);
                }
            } else {
                posicionYo = contactos.indexOf(contacto);
            }
        }

        ArrayList<String> sectionList = new ArrayList<>(alphaIndexer.keySet());
        Collections.sort(sectionList);
        sectionList.add(0, indiceYo);
        alphaIndexer.put(indiceYo, posicionYo);

        sections = new String[sectionList.size()];
        sections = sectionList.toArray(sections);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(rowLayoutId, parent, false);
        }

        ContactoListado contacto = contactos.get(position);
        // nombre
        TextView nombreContactoTextView = (TextView) convertView.findViewById(R.id.nombreContacto);
        nombreContactoTextView.setText(contacto.nombre);

        ImageView fotoContactoImageView = (ImageView) convertView.findViewById(R.id.fotoContacto);

        if (contacto.fotoURI != null) {
            fotoContactoImageView.setImageURI(Uri.parse(contacto.fotoURI));
        } else {
            final int tamanoAvatar = context.getResources().getDimensionPixelSize(R.dimen.apatxas_persona_avatar_tamano);
            final Bitmap avatar = CrearAvatarConLetra.crearAvatarConLetra(context, contacto.nombre, tamanoAvatar, tamanoAvatar);
            if (avatar != null) {
                fotoContactoImageView.setImageBitmap(avatar);
            } else {
                fotoContactoImageView.setVisibility(View.INVISIBLE);
            }
        }

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBoxContacto);
        cb.setChecked(contactosSeleccionados.contains(contacto));
        cb.setTag(contacto);
        cb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ContactoListado contactoCheckBox = (ContactoListado) cb.getTag();
                if (cb.isChecked()) {
                    contactosSeleccionados.add(contactoCheckBox);
                } else {
                    contactosSeleccionados.remove(contactoCheckBox);
                }

            }
        });

        return convertView;
    }

    public List<ContactoListado> getContactosSeleccionados() {
        return contactosSeleccionados;
    }


    public void toggleCheckBox(int position) {
        if (contactosSeleccionados.contains(contactos.get(position))) {
            contactosSeleccionados.remove(contactos.get(position));
        } else {
            contactosSeleccionados.add(contactos.get(position));
        }
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return alphaIndexer.get(sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
