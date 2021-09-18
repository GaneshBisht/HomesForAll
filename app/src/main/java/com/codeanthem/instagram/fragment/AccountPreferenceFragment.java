package com.codeanthem.instagram.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.codeanthem.instagram.R;

public class AccountPreferenceFragment extends PreferenceFragmentCompat {

    public AccountPreferenceFragment(){

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.account_preference);
    }
}
