package com.codeanthem.instagram.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.codeanthem.instagram.R;
import com.codeanthem.instagram.activity.LoginActivity;

public class AccountFragmentPreference extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.account_preference);

        EditTextPreference etpName = findPreference("key_name");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        etpName.setTitle(pref.getString("key_name",""));

        etpName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                etpName.setTitle(String.valueOf(newValue));

                return true;
            }
        });

        Preference preferenceLogout = findPreference("key_logout");

        preferenceLogout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                SharedPreferences preferences = getActivity().getSharedPreferences("app_info", MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();

                editor.clear();

                // clear login information
                //editor.putBoolean("isLogin", false);
                //editor.putString("username", null);

                editor.apply();

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                SharedPreferences.Editor editor1 = pref.edit();

                editor1.clear();

                editor1.apply();

                Intent iMain = new Intent(getActivity(), LoginActivity.class);
                startActivity(iMain);
                getActivity().finish();

                return true;
            }
        });

       Preference preference = findPreference("key_deactivate");

    }
}
