package com.example.albert.ccu;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface AnnouncementDao {
  @Insert
  void insert(Announcement announcement);

  @Query("SELECT * FROM Announcement WHERE c_id = :course_id ORDER BY a_date DESC")
  LiveData<List<Announcement>> getAnnouncementByC_id(String course_id);

  @Query("SELECT * FROM Announcement WHERE a_id = :a_id")
  Announcement getAnnouncementById(String a_id);

  @Query("SELECT * FROM Announcement ORDER BY a_date DESC")
  LiveData<List<Announcement>> getAll();
}
