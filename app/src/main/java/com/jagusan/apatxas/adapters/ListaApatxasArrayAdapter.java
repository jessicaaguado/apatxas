package com.jagusan.apatxas.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.ApatxaListado;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaApatxasArrayAdapter extends ArrayAdapter<ApatxaListado> {

    Context context;
    int rowLayoutId;
    List<ApatxaListado> apatxas;

    private List<ApatxaListado> apatxasSeleccionadas;

    public ListaApatxasArrayAdapter(Context context, int rowLayoutId, List<ApatxaListado> apatxas) {

        super(context, rowLayoutId, apatxas);

        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.apatxas = apatxas;

        apatxasSeleccionadas = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(rowLayoutId, parent, false);
        }

        ApatxaListado apatxa = apatxas.get(position);

        // nombre
        TextView nombreApatxaTextView = (TextView) convertView.findViewById(R.id.nombre);
        nombreApatxaTextView.setText(apatxa.nombre);
        // fecha
        TextView fechaApatxaTextView = (TextView) convertView.findViewById(R.id.fecha);
        Date fechaInicio = apatxa.fechaInicio;
        Date fechaFin = apatxa.fechaFin;
        boolean soloUnDia = apatxa.soloUnDia;
        String fechaDescripcion = FormatearFecha.formatearFecha(context.getResources(), fechaInicio);
        if (!soloUnDia) {
            fechaDescripcion += " - " + FormatearFecha.formatearFecha(context.getResources(), fechaFin);
        }
        fechaApatxaTextView.setText(fechaDescripcion);
        // estado
        TextView estadoApatxaTextView = (TextView) convertView.findViewById(R.id.estado);
        String descripcionEstadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaListado(context.getResources(), apatxa.getEstadoApatxa());
        estadoApatxaTextView.setText(descripcionEstadoApatxa);

        marcarSeleccion(convertView, apatxa);

        return convertView;
    }


    private void marcarSeleccion(View convertView, ApatxaListado apatxa) {
        int colorFondo = (apatxasSeleccionadas.contains(apatxa)) ? context.getResources().getColor(R.color.apatxascolors_gris_claro) : Color.TRANSPARENT;
        convertView.setBackgroundColor(colorFondo);
    }

    public void toggleSeleccion(Integer position, boolean checked) {
        if (checked) {
            apatxasSeleccionadas.add(apatxas.get(position));
        } else {
            apatxasSeleccionadas.remove(apatxas.get(position));
        }
        notifyDataSetChanged();
    }

    public void resetearSeleccion() {
        apatxasSeleccionadas = new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<ApatxaListado> getApatxasSeleccionadas() {
        return apatxasSeleccionadas;
    }

    public int numeroApatxasSeleccionadas() {
        return apatxasSeleccionadas.size();
    }

    public void eliminarApatxasSeleccionadas() {
        for (ApatxaListado apatxaEliminar : apatxasSeleccionadas) {
            apatxas.remove(apatxaEliminar);
        }
        resetearSeleccion();
    }


}
