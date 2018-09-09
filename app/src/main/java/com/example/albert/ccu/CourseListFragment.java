package com.example.albert.ccu;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CourseListFragment extends Fragment {
  private CourseViewModel viewModel;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_course_list, container, false);
    setHasOptionsMenu(true);
    Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
    ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.main_actionbar_title));

    RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
    final CourseListAdapter adapter = new CourseListAdapter(getActivity());
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.addItemDecoration(new MarginItemDecoration(16));

    viewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

    viewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
      @Override
      public void onChanged(@Nullable List<Course> courses) {
        adapter.setCourse(courses);
      }
    });

    final SwipeRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_course);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        viewModel.update();
        refreshLayout.setRefreshing(false);
      }
    });
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity)getActivity()).setNonSwipeable();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_main, menu);
  }

  //  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//
//    MenuInflater inflater = getMenuInflater();
//    inflater.inflate(R.menu.menu_main, menu);
//    return true;
//  }
}
