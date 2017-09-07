package com.vagrant.android.vagrant.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.activity.UpdatePwdActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment {
    private Preference mAccountPreference;
    private Preference mContactPreference;
    private Preference mDeveloperPreference;
    private Preference mEmailPreference;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mAccountPreference = (Preference)findPreference("preference_account");
        mContactPreference = (Preference)findPreference("preference_contact");
        mDeveloperPreference = (Preference)findPreference("preference_developer");
        mEmailPreference = (Preference)findPreference("preference_email");
        mAccountPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), UpdatePwdActivity.class);
                startActivity(intent);
                return false;
            }
        });
        mContactPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String content = mContactPreference.getSummary().toString();
                ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("contact",content);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(),"已复制到粘贴板",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mDeveloperPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String content = mDeveloperPreference.getSummary().toString();
                ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("developer",content);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(),"已复制到粘贴板",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mEmailPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String content = mEmailPreference.getSummary().toString();
                ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("email",content);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(),"已复制到粘贴板",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
}
