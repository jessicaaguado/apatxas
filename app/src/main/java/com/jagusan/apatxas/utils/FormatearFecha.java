package com.jagusan.apatxas.utils;

import android.content.res.Resources;

import com.jagusan.apatxas.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatearFecha {

    private static final Integer ID_PATRON_FECHA = R.string.patron_fecha_dia_mes_ano;

    public static String formatearHoy(Resources res) {
        return formatearFecha(res, new Date());
    }

    public static String formatearFecha(Resources res, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(res.getString(ID_PATRON_FECHA));
        return sdf.format(date);
    }

}
