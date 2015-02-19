package com.jagusan.apatxas.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.jagusan.apatxas.R;

import java.util.Locale;

public class SettingsUtils {

    public static void aplicarSettingsPropios(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String idiomaDefecto = context.getResources().getString(R.string.settings_apatxas_valor_idioma_defecto);
        String idioma = preferences.getString("settings_apatxas_idioma", idiomaDefecto);

        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}
