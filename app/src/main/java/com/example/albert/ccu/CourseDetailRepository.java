package com.example.albert.ccu;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class CourseDetailRepository {
  private CourseDao courseDao;
  private AnnouncementDao announcementDao;
  private ScoreDao scoreDao;
  private MaterialDao materialDao;

  CourseDetailRepository(Application application) {
    CourseRoomDatabase db = CourseRoomDatabase.getDatabase(application);
    courseDao = db.courseDao();
    announcementDao = db.announcementDao();
    scoreDao = db.scoreDao();
    materialDao = db.materialDao();
  }

  LiveData<List<Announcement>> getAllAnnouncement() {
    return announcementDao.getAll();
  }

  LiveData<List<Announcement>> getAllAnnouncementByCourse(String course) {
    return announcementDao.getAnnouncementByC_id(course);
  }

  LiveData<List<Score>> getAllScoreByCourseAndType(String course, int type) {
    return scoreDao.getScoreByCourseAndType(course, type);
  }

  LiveData<List<Material>> getAllMaterialByCourse(String course) {
    return materialDao.getMaterialByCourse(course);
  }
}
