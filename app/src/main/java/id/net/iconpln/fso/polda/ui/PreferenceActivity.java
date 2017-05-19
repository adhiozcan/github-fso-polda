package id.net.iconpln.fso.polda.ui;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import id.net.iconpln.fso.polda.AppPreference;
import id.net.iconpln.fso.polda.FsoPoldaApp;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.jobscheduler.JobName;
import id.net.iconpln.fso.polda.jobscheduler.job.ServiceManager;
import id.net.iconpln.fso.polda.utils.L;

/**
 * Created by Ozcan on 16/01/2017.
 */

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_petugas);
        setupAppToolbar();

        if (null == savedInstanceState)
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new PreferencePetugas())
                    .commit();
    }

    private void setupAppToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.appcompat_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public static class PreferencePetugas extends PreferenceFragment {

        private ListPreference     syncInterval;
        private CheckBoxPreference alwaysRemember;
        private CheckBoxPreference offlineMode;
        private CheckBoxPreference photoAlbum;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_petugas);

            /**
             * AppConfig Always Remember
             * Key : @string/pref_remember_key
             */
            String KEY_PREF_REMEMBER = getString(R.string.pref_remember_key);
            alwaysRemember = (CheckBoxPreference) getPreferenceManager().findPreference(KEY_PREF_REMEMBER);
            alwaysRemember.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    boolean checked = (boolean) o;
                    alwaysRemember.setChecked(checked);
                    AppPreference.saveAlwaysRemember(checked);
                    L.d("Value always remember --> " + checked);
                    L.d("Getting auto remember configuration value : " + AppPreference.isAlwaysRemember());
                    return checked;
                }
            });


            /**
             * Syncing Interval
             * Key : @string/pref_sync_key
             */
            String KEY_SYNC_INTERVAL = getString(R.string.pref_sync_key);
            syncInterval = (ListPreference) getPreferenceManager().findPreference(KEY_SYNC_INTERVAL);
            CharSequence[] defaultEntries = syncInterval.getEntries();
            syncInterval.setDefaultValue(defaultEntries[AppPreference.getSyncInterval()[0]]);
            syncInterval.setSummary(defaultEntries[AppPreference.getSyncInterval()[0]]);
            L.d("Default Value of FetchLaporan Interval is" +
                    "\n[Index] " + AppPreference.getSyncInterval()[0] +
                    "\n[Values] " + AppPreference.getSyncInterval()[1]);

            syncInterval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    String textValue = o.toString();

                    int            index   = syncInterval.findIndexOfValue(textValue);
                    CharSequence[] entries = syncInterval.getEntries();
                    CharSequence[] values  = syncInterval.getEntryValues();
                    syncInterval.setSummary(entries[index]);

                    int value = Integer.parseInt(String.valueOf(values[index]));
                    AppPreference.saveConfSyncInterval(index, value);

                    L.d("Get Entry  -- >" + entries[index]);
                    L.d("Get Entry Values --> " + values[index]);
                    L.d("Change Value of FetchLaporan Interval as below" +
                            "\n[Index] " + AppPreference.getSyncInterval()[0] +
                            "\n[Values] " + AppPreference.getSyncInterval()[1]);
                    String  intervalString = String.valueOf(values[index]);
                    Integer interval       = Integer.parseInt(intervalString);
                    if (interval == -1) {
                        //Stop service due to option is "Tidak Pernah [-1]"
                        ServiceManager.unregister(FsoPoldaApp.getContext(), JobName.UPDATE_LAPORAN);
                    } else {
                        //Restart service to perform information downloading in long task running
                        ServiceManager.unregister(FsoPoldaApp.getContext(), JobName.UPDATE_LAPORAN);
                        ServiceManager.register(FsoPoldaApp.getContext(), JobName.UPDATE_LAPORAN);
                    }

                    return true;
                }
            });

            /**
             * AppConfig Offline Mode
             * Key : @string/pref_offline_key
             */
            String KEY_OFFLINE_MODE = getString(R.string.pref_offline_key);
            offlineMode = (CheckBoxPreference) getPreferenceManager().findPreference(KEY_OFFLINE_MODE);
            offlineMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    boolean checked = (boolean) o;
                    offlineMode.setChecked(checked);
                    AppPreference.saveConfOfflineMode(checked);
                    L.d("Value offline mode --> " + checked);
                    L.d("Getting user information about offline mode : " + AppPreference.isOfflineModeActive());
                    return false;
                }
            });


            /**
             * AppConfig Photo Auto Album
             * Key : @string/pref_album_key
             */
            String KEY_PHOTO_ALBUM = getString(R.string.pref_album_key);
            photoAlbum = (CheckBoxPreference) getPreferenceManager().findPreference(KEY_PHOTO_ALBUM);
            photoAlbum.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    boolean checked = (boolean) o;
                    photoAlbum.setChecked(checked);
                    AppPreference.saveConfAutoPhotoAlbum(checked);
                    L.d("Value auto photo album --> " + checked);
                    L.d("Getting auto photo album configuration value : " + AppPreference.isAutoPhotoAlbum());
                    return false;
                }
            });

            setInitialValue();
        }

        private void setInitialValue() {
            alwaysRemember.setChecked(AppPreference.isAlwaysRemember());
            photoAlbum.setChecked(AppPreference.isAutoPhotoAlbum());
            syncInterval.setDefaultValue(-1);
        }
    }
}
