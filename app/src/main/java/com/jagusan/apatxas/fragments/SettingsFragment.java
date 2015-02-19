package com.jagusan.apatxas.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jagusan.apatxas.R;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
