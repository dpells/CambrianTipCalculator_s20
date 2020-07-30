package ca.davidpellegrini.tipcalculator_s20;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

import android.app.Fragment;

import android.preference.PreferenceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment
implements OnSharedPreferenceChangeListener{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    public void onSharedPreferenceChanged(SharedPreferences savedValues, String key){
        Fragment tipFragment = getFragmentManager()
                .findFragmentById(R.id.main_fragment);

        if(tipFragment != null){
            tipFragment.onResume();
        }
    }
}
