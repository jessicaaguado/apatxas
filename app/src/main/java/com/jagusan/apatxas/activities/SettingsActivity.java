package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.fragments.SettingsFragment;
import com.jagusan.apatxas.utils.SettingsUtils;

/**
 * Settings activity that contains a fragment displaying the preferences.
 */
public class SettingsActivity extends ApatxasActionBarActivity  implements SharedPreferences.OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personalizarActionBar(R.string.title_activity_settings,MostrarTituloPantalla.SETTINGS);
		// Display the preferences fragment as the content of the activity
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SettingsUtils.aplicarSettingsPropios(this);
        restartActivity();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


}
