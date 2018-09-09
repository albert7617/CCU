package com.example.albert.ccu;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CourseDao {
  @Insert
  void insert(Course course);

  @Query("SELECT * FROM Course ORDER BY course_id ASC")
  LiveData<List<Course>> getAll();

  @Query("SELECT * FROM Course WHERE course_name = :course_name")
  List<Course> getWithCourseName(String course_name);

  @Query("SELECT course_id FROM Course")
  List<String> getCourseIds();

  @Query("DELETE FROM Course WHERE course_id = :c_id")
  void deleteCourse(String c_id);
}
