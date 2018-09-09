package com.example.albert.ccu;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getFragmentManager().beginTransaction().replace(android.R.id.content,
            new SettingsFragment()).commit();
    setupActionBar();
    PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
  }



  public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onResume() {
      super.onResume();
      getPreferenceScreen().getSharedPreferences()
              .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
      super.onPause();
      getPreferenceScreen().getSharedPreferences()
              .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_general);
      final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
      findPreference("account").setSummary(sharedPreferences.getString(getString(R.string.user_name_key), getString(R.string.not_login)));
      SharedPreferences.OnSharedPreferenceChangeListener changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
          findPreference("account").setSummary(sharedPreferences.getString(getString(R.string.user_name_key), getString(R.string.not_login)));
        }
      };
      findPreference("log_out").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.remove(getString(R.string.user_name_key));
          editor.remove(getString(R.string.password_key));
          editor.putBoolean(getString(R.string.is_login_key), false);
          editor.apply();
          return false;
        }
      });
      findPreference("appVersion").setOnPreferenceClickListener(new ClickListener());
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
      switch (key) {
        case "0":
          findPreference("account").setSummary(sharedPreferences.getString(getString(R.string.user_name_key), getString(R.string.not_login)));
      }
    }

    private class ClickListener implements Preference.OnPreferenceClickListener{
      private int count = -5;
      Toast msg;

      ClickListener() {
        msg = Toast.makeText(getContext(), "安安", Toast.LENGTH_SHORT);
      }
      @Override
      public boolean onPreferenceClick(Preference preference) {
        count++;
        if (count >= 0) {
          if (count < 10) {
            msg.setText(getResources().getStringArray(R.array.tap_count)[count]);
            msg.show();
          } else {
            msg.setText(getResources().getStringArray(R.array.tap_count)[10]);
            msg.show();
          }
        }
        return false;
      }
    }
  }
  private void setupActionBar() {
    ViewGroup rootView = (ViewGroup)findViewById(R.id.action_bar_root); //id from appcompat

    if (rootView != null) {
      View view = getLayoutInflater().inflate(R.layout.activity_setting, rootView, false);
      rootView.addView(view, 0);

      Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
    }

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      // Show the Up button in the action bar.
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}