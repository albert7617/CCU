package com.example.albert.ccu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static int NUM_PAGES = 1;
//  private ViewPager viewPager;
  private NonSwipeableViewPager viewPager;

  private CoursePagerAdapter pagerAdapter;

  private CourseListFragment listFragment;
  private CourseDetailFragment detailFragment;

  private CourseViewModel viewModel;
  private final int REQUEST_EXTERNAL_STORAGE = 1;
  private String[] PERMISSIONS_STORAGE = {
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE
  };
  public void verifyStoragePermissions(Activity activity) {
    // Check if we have write permission
    int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if (permission != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_swipe);





    viewPager = findViewById(R.id.viewPager);
    listFragment = new CourseListFragment();
    detailFragment = new CourseDetailFragment();
    pagerAdapter = new CoursePagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(pagerAdapter);
    //viewPager.setPageTransformer(true, new IosPageTransformer());
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if(position == 0) {
          viewPager.setSwipable(false);
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {


      }
    });
  }



  @Override
  protected void onResume() {
    super.onResume();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    boolean is_login = sharedPreferences.getBoolean(getString(R.string.is_login_key), false);
    if(!is_login) {
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(intent);
    }
    verifyStoragePermissions(this);
    Log.d("TAG", "onResume: " + String.valueOf(viewPager.getCurrentItem() != 0));
    viewPager.setSwipable(viewPager.getCurrentItem() != 0);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.setting:
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }




  public void setDetailFragment(String id, String name) {
    NUM_PAGES = 2;
    FragmentManager fm = getSupportFragmentManager();

    fm.beginTransaction().remove(detailFragment).commit();
    detailFragment = new CourseDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putString("COURSE_ID", id);
    bundle.putString("COURSE_NAME", name);
    detailFragment.setArguments(bundle);
    pagerAdapter.notifyDataSetChanged();
    viewPager.setCurrentItem(1);
    viewPager.setSwipable(true);
  }

  public void setNonSwipeable() {
    viewPager.setSwipable(false);
  }

  @Override
  public void onBackPressed() {
    if (viewPager.getCurrentItem() == 0) {
      // If the user is currently looking at the first step, allow the system to handle the
      // Back button. This calls finish() on this activity and pops the back stack.
      super.onBackPressed();
    } else {
      // Otherwise, select the previous step.
      viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
  }

  private class CoursePagerAdapter extends FragmentStatePagerAdapter {

    public CoursePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch(position) {
        case 0:
          return listFragment;
        case 1:
          return detailFragment;
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return NUM_PAGES;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
      if(object instanceof CourseListFragment) {
        return POSITION_UNCHANGED;
      } else {
        Log.d("ItemPosition", "getItemPosition: POSITION_NONE");
        return POSITION_NONE;
      }

    }
  }
}
