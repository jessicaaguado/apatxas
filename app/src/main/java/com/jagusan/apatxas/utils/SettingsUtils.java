package com.jagusan.apatxas.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.jagusan.apatxas.R;

import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

public class SettingsUtils {

    public static final String SETTINGS_APATXAS_IDIOMA = "settings_apatxas_idioma";
    public static final String SETTINGS_APATXAS_MONEDA = "settings_apatxas_monedas";

    public static void aplicarSettingsPropios(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String idiomaDefecto = context.getResources().getString(R.string.settings_apatxas_valor_idioma_defecto);
        String idioma = preferences.getString(SETTINGS_APATXAS_IDIOMA, idiomaDefecto);

        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    public static String getMoneda(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String monedaDefecto = context.getResources().getString(R.string.settings_apatxas_valor_moneda_defecto);
        String monedaCode = preferences.getString(SETTINGS_APATXAS_MONEDA, monedaDefecto);

        Locale locale = getLocaleParaMoneda(context);
        return Currency.getInstance(monedaCode).getSymbol(locale);
    }

    public static Locale getLocale(Context context){
        return context.getResources().getConfiguration().locale;
    }

    public static Locale getLocaleParaMoneda(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String monedaDefecto = context.getResources().getString(R.string.settings_apatxas_valor_moneda_defecto);
        String monedaCode = preferences.getString(SETTINGS_APATXAS_MONEDA, monedaDefecto);

        String idiomaDefecto = context.getResources().getString(R.string.settings_apatxas_valor_idioma_defecto);
        Locale locale = new Locale(idiomaDefecto);

        //String[] codigosMonedaIngles = new String[]{"USD","GBP"};
        //if (Arrays.asList(codigosMonedaIngles).contains(monedaCode)){
        //    locale = new Locale("en");
        //}

        if ("USD".equals(monedaCode)){
            locale = new Locale("en","US");
        }
        if ("GBP".equals(monedaCode)){
            locale = new Locale("en","EN");
        }

        return locale;
    }
}
