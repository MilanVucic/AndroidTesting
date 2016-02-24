package com.example.vucic.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SettingsFragment extends Fragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();;
    private SharedPreferences preferences;
    private Switch notificationsSwitch;
    private boolean pushNotificationsEnabled;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        notificationsSwitch = (Switch) view.findViewById(R.id.enablePushNotificationsSwitch);
        notificationsSwitch.setChecked(pushNotificationsEnabled);
        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) enableNotifications();
                else disableNotifications();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "is attached to the activity.");
        preferences = context.getSharedPreferences("stuff", Context.MODE_PRIVATE);
        pushNotificationsEnabled = preferences.getBoolean("pushNotificationsEnabled", true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void enableNotifications() {
        Log.i(TAG, "Push notifications enabled.");

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("pushNotificationsEnabled", true);
        editor.apply();
    }

    private void disableNotifications() {
        Log.i(TAG, "Push notifications disabled.");

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("pushNotificationsEnabled", false);
        editor.apply();
    }
}
