package com.jagusan.apatxas.utils;

import android.content.res.Resources;

import com.jagusan.apatxas.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertirFecha {

    private static final Integer ID_PATRON_FECHA = R.string.patron_fecha_dia_mes_ano;

    public static Long getTime(Resources res, String fecha) {
        Long date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(res.getString(ID_PATRON_FECHA));
            date = sdf.parse(fecha).getTime();
        } catch (ParseException e) {
        }
        return date;
    }
}
