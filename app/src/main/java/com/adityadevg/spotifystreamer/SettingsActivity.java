package com.adityadevg.spotifystreamer;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        ListPreference listPreferenceCategory = (ListPreference) findPreference(getString(R.string.country_code_pref_key));
        if (null != listPreferenceCategory) {
            Map<String, String> countryDetails = getCountryCode();
            CharSequence entries[] = new String[countryDetails.size()];
            CharSequence entryValues[] = new String[countryDetails.size()];
            int i = 0;

            // Iterate all keys
            for (String key : countryDetails.keySet())
                entryValues[i++] = key;

            i = 0;
            // Iterate all values
            for (String value : countryDetails.values())
                entries[i++] = value;

            listPreferenceCategory.setEntries(entries);
            listPreferenceCategory.setEntryValues(entryValues);
        }
        bindPreferenceSummaryToValue(findPreference(getString(R.string.country_code_pref_key)));
    }

    public Map<String, String> getCountryCode() {
        String[] LOCALES = Locale.getISOCountries();
        List<String> countryCodeList = new ArrayList<String>();
        List<String> countryNameList = new ArrayList<String>();
        Map<String, String> countryDetails = new HashMap<String, String>();

        for (String countryCode : LOCALES) {
            Locale localeObj = new Locale("", countryCode);
            countryDetails.put(localeObj.getCountry(), localeObj.getDisplayCountry());
        }
        return countryDetails;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }
}
