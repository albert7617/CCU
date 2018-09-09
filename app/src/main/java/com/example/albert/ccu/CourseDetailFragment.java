package com.example.albert.ccu;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CourseDetailFragment extends Fragment {

  private CourseDetailViewModel viewModel;

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

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_course_detail, container, false);

    String course_id = "0", course_name = "Error";

    Bundle bundle = this.getArguments();
    if(bundle != null) {
      course_name = bundle.getString("COURSE_NAME");
      course_id = bundle.getString("COURSE_ID");
    }

    Toolbar toolbar = rootView.findViewById(R.id.detail_toolbar);
    ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(course_name);

    announcement = rootView.findViewById(R.id.announcement_recyclerView);
    material = rootView.findViewById(R.id.material_recyclerView);
    score = rootView.findViewById(R.id.score_recyclerView);

    final String course = course_id;
    final AnnouncementListAdapter announcementListAdapter = new AnnouncementListAdapter(getActivity());
    announcement.setAdapter(announcementListAdapter);
    announcement.setLayoutManager(new LinearLayoutManager(getActivity()));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllAnnouncements(course).observe(this, new Observer<List<Announcement>>() {
      @Override
      public void onChanged(@Nullable List<Announcement> announcements) {
        announcementListAdapter.setAllAnnouncement(announcements);
      }
    });

    final RecyclerView scoreNormal = rootView.findViewById(R.id.normal_score);
    scoreNormal.setNestedScrollingEnabled(false);
    final ScoreListAdapter normalScoreAdapter = new ScoreListAdapter(getActivity());
    scoreNormal.setAdapter(normalScoreAdapter);
    scoreNormal.setLayoutManager(new LinearLayoutManager(getActivity()));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllScores(course, 0).observe(this, new Observer<List<Score>>() {
      @Override
      public void onChanged(@Nullable List<Score> scores) {
        normalScoreAdapter.setAllScore(scores);
      }
    });

    final RecyclerView scoreOnline = rootView.findViewById(R.id.online_score);
    scoreOnline.setNestedScrollingEnabled(false);
    final ScoreListAdapter onlineScoreAdapter = new ScoreListAdapter(getActivity());
    scoreOnline.setAdapter(onlineScoreAdapter);
    scoreOnline.setLayoutManager(new LinearLayoutManager(getActivity()));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllScores(course, 1).observe(this, new Observer<List<Score>>() {
      @Override
      public void onChanged(@Nullable List<Score> scores) {
        onlineScoreAdapter.setAllScore(scores);
      }
    });

    final RecyclerView scoreHomework = rootView.findViewById(R.id.homework_score);
    scoreHomework.setNestedScrollingEnabled(false);
    final ScoreListAdapter homeworkScoreAdapter = new ScoreListAdapter(getActivity());
    scoreHomework.setAdapter(homeworkScoreAdapter);
    scoreHomework.setLayoutManager(new LinearLayoutManager(getActivity()));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllScores(course, 2).observe(this, new Observer<List<Score>>() {
      @Override
      public void onChanged(@Nullable List<Score> scores) {
        homeworkScoreAdapter.setAllScore(scores);
      }
    });

    final RecyclerView scoreTotal = rootView.findViewById(R.id.total_score);
    scoreTotal.setNestedScrollingEnabled(false);
    final ScoreListAdapter totalScoreAdapter = new ScoreListAdapter(getActivity());
    scoreTotal.setAdapter(totalScoreAdapter);
    scoreTotal.setLayoutManager(new LinearLayoutManager(getActivity()));

    viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);
    viewModel.getAllScores(course, 3).observe(this, new Observer<List<Score>>() {
      @Override
      public void onChanged(@Nullable List<Score> scores) {
        totalScoreAdapter.setAllScore(scores);
      }
    });

    final MaterialListAdapter materialListAdapter= new MaterialListAdapter(getActivity());
    material.setAdapter(materialListAdapter);
    material.setLayoutManager(new LinearLayoutManager(getActivity()));

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


    BottomNavigationView navigation = (BottomNavigationView) rootView.findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    return rootView;
  }
}
