package edu.tsinghua.location.research;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import edu.tsinghua.location.research.module.R;

/**
 * Created by mariotaku on 16/2/1.
 */
@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
