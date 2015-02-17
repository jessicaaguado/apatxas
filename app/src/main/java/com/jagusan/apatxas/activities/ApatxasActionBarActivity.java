package com.jagusan.apatxas.activities;

import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.jagusan.apatxas.utils.SettingsUtils;

public class ApatxasActionBarActivity extends ActionBarActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SettingsUtils.aplicarSettingsPropios(this);
    }

    protected void personalizarActionBar(int idTituloString, boolean mostrar){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(mostrar);
        actionBar.setTitle(this.getResources().getString(idTituloString));
    }
}
