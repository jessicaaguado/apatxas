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
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.utils.FormatearNumero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaGastosApatxaArrayAdapter extends ArrayAdapter<GastoApatxaListado> {

    Context context;
    int rowLayoutId;
    List<GastoApatxaListado> gastos;

    private List<GastoApatxaListado> gastosSeleccionados;

    public ListaGastosApatxaArrayAdapter(Context context, int rowLayoutId, List<GastoApatxaListado> gastos) {

        super(context, rowLayoutId, gastos);

        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.gastos = gastos;
        gastosSeleccionados = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(rowLayoutId, parent, false);
        }

        GastoApatxaListado gasto = gastos.get(position);
        // titulo
        TextView conceptoGastoTextView = (TextView) convertView.findViewById(R.id.concepto);
        conceptoGastoTextView.setText(gasto.concepto);
        // total
        TextView totalGastoTextView = (TextView) convertView.findViewById(R.id.total);
        totalGastoTextView.setText(FormatearNumero.aDineroEuros(context.getResources(), gasto.total));
        // quien lo ha pagado
        TextView pagadorGastoTextView = (TextView) convertView.findViewById(R.id.pagadoPor);
        if (pagadorGastoTextView != null) {
            String pagador = gasto.pagadoPor != null ? gasto.pagadoPor : context.getResources().getString(R.string.sin_pagar);
            pagadorGastoTextView.setText(pagador);
        }

        marcarSeleccion(convertView, gasto);

        return convertView;
    }

    private void marcarSeleccion(View convertView, GastoApatxaListado gasto) {
        int colorFondo = (gastosSeleccionados.contains(gasto)) ? context.getResources().getColor(R.color.apatxascolors_gris_claro) : Color.TRANSPARENT;
        convertView.setBackgroundColor(colorFondo);
    }

    public void toggleSeleccion(Integer position, boolean checked) {
        if (checked) {
            gastosSeleccionados.add(gastos.get(position));
        } else {
            gastosSeleccionados.remove(gastos.get(position));
        }
        notifyDataSetChanged();
    }

    public void resetearSeleccion() {
        gastosSeleccionados = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void actualizarGasto(Integer posicionGastoActualizar, GastoApatxaListado gastoActualizado) {
        gastos.set(posicionGastoActualizar, gastoActualizado);
        notifyDataSetChanged();
    }

    public List<GastoApatxaListado> getGastosSeleccionados() {
        return gastosSeleccionados;
    }


    public int numeroGastosSeleccionados() {
        return gastosSeleccionados.size();
    }


    public void eliminarGastosSeleccionados() {
        for (GastoApatxaListado gastoEliminar : gastosSeleccionados) {
            gastos.remove(gastoEliminar);
        }
        resetearSeleccion();
    }

    public void reordenarPorNombre(){
        Collections.sort(gastos, new GastosComparadorNombre() );
        notifyDataSetChanged();
    }

    public void reordenarPorOrdenEntrada(){
        Collections.sort(gastos, new GastosComparadorOrdenEntrada());
        notifyDataSetChanged();
    }

    class GastosComparadorNombre implements Comparator<GastoApatxaListado> {
        public int compare(GastoApatxaListado gasto1, GastoApatxaListado gasto2) {
            return gasto1.concepto.compareTo(gasto2.concepto);
        }
    }
    class GastosComparadorOrdenEntrada implements Comparator<GastoApatxaListado> {
        public int compare(GastoApatxaListado gasto1, GastoApatxaListado gasto2) {
            return gasto1.id.compareTo(gasto2.id);
        }
    }


}
