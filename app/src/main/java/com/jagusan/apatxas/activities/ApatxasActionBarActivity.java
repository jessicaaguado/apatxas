package com.jagusan.apatxas.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.jagusan.apatxas.utils.SettingsUtils;

public class ApatxasActionBarActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsUtils.aplicarSettingsPropios(this);

    }

    void personalizarActionBar(int idTituloString, boolean mostrar) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(mostrar);
        actionBar.setTitle(this.getResources().getString(idTituloString));
    }
}
