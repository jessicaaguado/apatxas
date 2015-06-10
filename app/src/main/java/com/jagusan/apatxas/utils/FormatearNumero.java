package com.jagusan.apatxas.utils;

import android.content.Context;
import android.content.res.Resources;

import com.jagusan.apatxas.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import static java.lang.Math.abs;

public class FormatearNumero {

    public static String aDinero(Context context, Double cantidad) {
        NumberFormat dineroFormat = NumberFormat.getNumberInstance(SettingsUtils.getLocaleParaMoneda(context));
        dineroFormat.setMaximumFractionDigits(2);
        dineroFormat.setMinimumFractionDigits(2);
        return dineroFormat.format(abs(cantidad));
    }

    public static Double dineroADouble(Context context, String dinero) throws ParseException {
        NumberFormat dineroFormat = NumberFormat.getNumberInstance(SettingsUtils.getLocaleParaMoneda(context));
        dineroFormat.setMaximumFractionDigits(2);
        dineroFormat.setMinimumFractionDigits(2);
        return dineroFormat.parse(dinero).doubleValue();
    }

    public static String aDineroConMoneda(Context context, Double cantidad) {
        String cantidadDinero = aDinero(context, cantidad);
        System.out.println("CANTIDAD DINERO -- " + cantidad + " >> " + cantidadDinero + " " + SettingsUtils.getMoneda(context));
        Resources res = context.getResources();
        return res.getString(R.string.cantidad_dinero_con_moneda, cantidadDinero, SettingsUtils.getMoneda(context));
    }

    public static String aDescripcionRepartoDineroEurosEnFuncionEstado(Resources res, Double cantidad, boolean pagado) {
        String texto = "";
        if (cantidad > 0) {
            if (pagado) {
                texto = res.getString(R.string.pagado);
            } else {
                texto = res.getString(R.string.debe);
            }
        } else if (cantidad < 0) {
            if (pagado) {
                texto = res.getString(R.string.recibido);
            } else {
                texto = res.getString(R.string.recibe);
            }
        }
        return texto;
    }
}
