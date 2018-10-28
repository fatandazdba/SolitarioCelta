package es.upm.miw.SolitarioCelta;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import es.upm.miw.SolitarioCelta.MainActivity;

public class SCeltaPreferenceFragment extends PreferenceFragment{
String nombre="";
    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);


    }
}
