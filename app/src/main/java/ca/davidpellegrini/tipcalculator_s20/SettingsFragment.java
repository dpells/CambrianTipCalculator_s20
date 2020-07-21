package ca.davidpellegrini.tipcalculator_s20;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
