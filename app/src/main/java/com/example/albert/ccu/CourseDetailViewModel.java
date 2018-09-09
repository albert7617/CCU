package com.example.albert.ccu;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class CourseDetailViewModel extends AndroidViewModel {
  private CourseDetailRepository repository;
//  private LiveData<List<Announcement>> allAnnouncements;


  public CourseDetailViewModel(@NonNull Application application) {
    super(application);
    repository = new CourseDetailRepository(application);
//    allAnnouncements = repository.getAllAnnouncement();
  }

  LiveData<List<Announcement>> getAllAnnouncements(String course) {
    return repository.getAllAnnouncementByCourse(course);
  }

  LiveData<List<Score>> getAllScores(String course, int type) {
    return repository.getAllScoreByCourseAndType(course, type);
  }

  LiveData<List<Material>> getAllMaterial(String course) {
    return repository.getAllMaterialByCourse(course);
  }
}
