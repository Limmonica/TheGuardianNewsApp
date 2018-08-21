package com.example.limmonica.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class ArticlePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            // Get the "Number of Articles Shown" Preference object according to its key
            Preference numberArticles = findPreference(getString(R.string.settings_number_articles_key));
            // Get the "Order by" Preference object according to its key
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            // Bind the value that's in SharedPreferences to what will show up
            // in the preference summary
            bindPreferenceSummaryToValue(numberArticles);
            bindPreferenceSummaryToValue(orderBy);
        }

        /**
         * Binds the value stored in SharedPreferences to the value displayed in the Preference
         * Summary
         *
         * @param preference is the stored preference
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the current ArticlePreferenceFragment instance to listen for changes to the
            // preference we pass
            preference.setOnPreferenceChangeListener(this);
            // Read the current value of the preference stored in the SharedPreferences on the device
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            // Display the current value of the preference in the preference summary
            onPreferenceChange(preference, preferenceString);
        }

        /**
         * Updates the displayed preference summary after it has been changed.
         *
         * @param preference is the preference stored on the device
         * @param value      is the value that the user provided
         * @return boolean
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // Store the value provided by the user into a String
            String stringValue = value.toString();
            // If the preference is changed in the list type
            if (preference instanceof ListPreference) {
                // Update the list preference summary
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                // Otherwise update the editText type of preference
                preference.setSummary(stringValue);
            }
            return true;
        }
    }
}
