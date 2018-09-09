package com.example.albert.ccu;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

  private CourseRepository courseRepository;
  private LiveData<List<Course>> allCourses;

  public CourseViewModel(@NonNull Application application) {
    super(application);
    courseRepository = new CourseRepository(application);
    allCourses = courseRepository.getAllCourse();
  }

  public LiveData<List<Course>> getAllCourses() {
    return allCourses;
  }

  public void update() {
    courseRepository.update();
  }
}
