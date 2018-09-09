package com.example.albert.ccu;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

  private CourseDetailViewModel viewModel;

  private TextView mTextMessage;

  private RecyclerView announcement, material;

  private NestedScrollView score;

  private final int margin = 16;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
          = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          announcement.setVisibility(View.VISIBLE);
          score.setVisibility(View.GONE);
          material.setVisibility(View.GONE);
          return true;
        case R.id.navigation_dashboard:
          announcement.setVisibility(View.GONE);
          score.setVisibility(View.VISIBLE);
          material.setVisibility(View.GONE);
          return true;
        case R.id.navigation_notifications:
          announcement.setVisibility(View.GONE);
          score.setVisibility(View.GONE);
          material.setVisibility(View.VISIBLE);
          return true;
      }
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_detail);



    Intent intent = getIntent();
//    Toast.makeText(this, intent.getStringExtra("COURSE_ID"), Toast.LENGTH_SHORT).show();

    Toolbar toolbar = findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(intent.getStringExtra("COURSE_NAME"));

    announcement = findViewById(R.id.announcement_recyclerView);
    material = findViewById(R.id.material_recyclerView);
    score = findViewById(R.id.score_recyclerView);

    final String course = intent.getStringExtra("COURSE_ID");
    final AnnouncementListAdapter announcementListAdapter = new AnnouncementListAdapter(this);
    announcement.setAdapter(announcementListAdapter);
    announcement.setLayoutManager(new LinearLayoutManager(this));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllAnnouncements(course).observe(this, new Observer<List<Announcement>>() {
      @Override
      public void onChanged(@Nullable List<Announcement> announcements) {
        announcementListAdapter.setAllAnnouncement(announcements);
      }
    });

    final RecyclerView scoreNormal = findViewById(R.id.normal_score);
    scoreNormal.setNestedScrollingEnabled(false);
    final ScoreListAdapter normalScoreAdapter = new ScoreListAdapter(this);
    scoreNormal.setAdapter(normalScoreAdapter);
    scoreNormal.setLayoutManager(new LinearLayoutManager(this));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllScores(course, 0).observe(this, new Observer<List<Score>>() {
      @Override
      public void onChanged(@Nullable List<Score> scores) {
        normalScoreAdapter.setAllScore(scores);
      }
    });

    final RecyclerView scoreOnline = findViewById(R.id.online_score);
    scoreOnline.setNestedScrollingEnabled(false);
    final ScoreListAdapter onlineScoreAdapter = new ScoreListAdapter(this);
    scoreOnline.setAdapter(onlineScoreAdapter);
    scoreOnline.setLayoutManager(new LinearLayoutManager(this));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllScores(course, 1).observe(this, new Observer<List<Score>>() {
      @Override
      public void onChanged(@Nullable List<Score> scores) {
        onlineScoreAdapter.setAllScore(scores);
      }
    });

    final RecyclerView scoreHomework = findViewById(R.id.homework_score);
    scoreHomework.setNestedScrollingEnabled(false);
    final ScoreListAdapter homeworkScoreAdapter = new ScoreListAdapter(this);
    scoreHomework.setAdapter(homeworkScoreAdapter);
    scoreHomework.setLayoutManager(new LinearLayoutManager(this));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllScores(course, 2).observe(this, new Observer<List<Score>>() {
      @Override
      public void onChanged(@Nullable List<Score> scores) {
        homeworkScoreAdapter.setAllScore(scores);
      }
    });

    final RecyclerView scoreTotal = findViewById(R.id.total_score);
    scoreTotal.setNestedScrollingEnabled(false);
    final ScoreListAdapter totalScoreAdapter = new ScoreListAdapter(this);
    scoreTotal.setAdapter(totalScoreAdapter);
    scoreTotal.setLayoutManager(new LinearLayoutManager(this));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllScores(course, 3).observe(this, new Observer<List<Score>>() {
      @Override
      public void onChanged(@Nullable List<Score> scores) {
        totalScoreAdapter.setAllScore(scores);
      }
    });

    final MaterialListAdapter materialListAdapter= new MaterialListAdapter(this);
    material.setAdapter(materialListAdapter);
    material.setLayoutManager(new LinearLayoutManager(this));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllMaterial(course).observe(this, new Observer<List<Material>>() {
      @Override
      public void onChanged(@Nullable List<Material> materials) {
        materialListAdapter.setAllMaterial(materials);
      }
    });

    announcement.addItemDecoration(new MarginItemDecoration(margin));
    scoreHomework.addItemDecoration(new MarginItemDecoration(margin));
    scoreNormal.addItemDecoration(new MarginItemDecoration(margin));
    scoreOnline.addItemDecoration(new MarginItemDecoration(margin));
    scoreTotal.addItemDecoration(new MarginItemDecoration(margin));
    material.addItemDecoration(new MarginItemDecoration(margin));




    mTextMessage = (TextView) findViewById(R.id.message);


    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

}
