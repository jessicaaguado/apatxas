package com.jagusan.apatxas.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.ContactoListado;

import java.util.ArrayList;
import java.util.List;

public class ListaContactosArrayAdapter extends ArrayAdapter<ContactoListado> {

    Context context;
    int rowLayoutId;
    List<ContactoListado> contactos;

    List<ContactoListado> contactosSeleccionados;

    public ListaContactosArrayAdapter(Context context, int rowLayoutId, List<ContactoListado> contactos) {

        super(context, rowLayoutId, contactos);

        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.contactos = contactos;
        this.contactosSeleccionados = new ArrayList<>();
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
            fotoContactoImageView.setImageResource(R.drawable.ic_apatxas_contacto_sin_foto);
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

}
