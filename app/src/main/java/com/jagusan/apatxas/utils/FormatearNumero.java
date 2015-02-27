package com.jagusan.apatxas.utils;

import android.content.res.Resources;

import com.jagusan.apatxas.R;

public class FormatearNumero {

    public static String aDinero(Resources res, Double cantidad) {
        return res.getString(R.string.cantidad_dinero, cantidad);
    }

    public static String aDineroEuros(Resources res, Double cantidad) {
        return res.getString(R.string.cantidad_dinero_euros, cantidad);
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
