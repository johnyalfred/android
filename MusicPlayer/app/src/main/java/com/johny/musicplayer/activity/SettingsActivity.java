package com.johny.musicplayer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.johny.musicplayer.R;
import com.johny.musicplayer.fragments.EqualizerFragment;
import com.johny.musicplayer.utils.Prefs;
import com.johny.musicplayer.utils.Themes;
import com.johny.musicplayer.utils.Util;

public class SettingsActivity extends BaseActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar()
                    .setElevation(getResources().getDimension(R.dimen.header_elevation));
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.prefFrame, new PrefFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!getFragmentManager().popBackStackImmediate()) {
                    super.onOptionsItemSelected(item);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!getFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    public static class PrefFragment extends PreferenceFragment
            implements AdapterView.OnItemLongClickListener {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            if (view != null) {
                ((ListView) view.findViewById(android.R.id.list)).setOnItemLongClickListener(this);
            }
            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle(R.string.header_settings);
            }
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                             Preference preference) {
            if ("com.johny.musicplayer.fragments.EqualizerFragment"
                    .equals(preference.getFragment())) {
                Intent eqIntent = Util.getSystemEqIntent(getActivity());

                if (eqIntent != null
                        && !Prefs.getPrefs(getActivity()).getBoolean(Prefs.EQ_ENABLED, false)) {
                    // If the system has an equalizer implementation already in place, use it
                    // to avoid weird problems and conflicts that can cause unexpected behavior

                    // for example, on Motorola devices, attaching an Equalizer can cause the
                    // MediaPlayer's volume to briefly become very loud -- even when the phone
                    // is muted
                    startActivity(eqIntent);
                } else if (Util.hasEqualizer()) {
                    // If there isn't a global equalizer or the user has already enabled our
                    // equalizer, navigate to the built-in implementation
                    // TODO animate this
                    getFragmentManager().beginTransaction()
                            .replace(R.id.prefFrame, new EqualizerFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getActivity(), R.string.equalizerUnsupported, Toast.LENGTH_LONG)
                            .show();
                }
                return true;
            } else if (preference.getKey().equals(Prefs.ADD_SHORTCUT)) {
                AlertDialog prompt = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.add_shortcut)
                        .setMessage(R.string.add_shortcut_description)
                        .setPositiveButton(R.string.action_add,
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Themes.updateLauncherIcon(getActivity());
                            }
                        })
                        .setNegativeButton(R.string.action_cancel, null)
                        .show();

                Themes.themeAlertDialog(prompt);
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            // A long press on the Equalizer preference will always open the built-in equalizer
            Object item = ((ListView) parent).getAdapter().getItem(position);
            if (item instanceof Preference) {
                Preference pref = (Preference) item;
                if ("com.johny.musicplayer.fragments.EqualizerFragment".equals(pref.getFragment())) {
                    if (Util.hasEqualizer()) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.prefFrame, new EqualizerFragment())
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast
                                .makeText(
                                        getActivity(),
                                        R.string.equalizerUnsupported,
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                }
                return true;
            }
            return false;
        }
    }
}
