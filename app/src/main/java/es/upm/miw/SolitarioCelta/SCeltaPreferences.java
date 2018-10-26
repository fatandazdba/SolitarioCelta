package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class SCeltaPreferences extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_void);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction =
				fragmentManager.beginTransaction();
		SCeltaPreferenceFragment fragment = new SCeltaPreferenceFragment();
		fragmentTransaction.replace(android.R.id.content, fragment);
		fragmentTransaction.commit();
	}
}
