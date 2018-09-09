package com.example.albert.ccu;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
  @Insert
  void insert(Score score);

  @Query("DELETE FROM Score")
  void dropScoreTable();

  @Query("SELECT * FROM Score WHERE c_id = :c_id AND s_type_cd = :s_type ORDER BY s_id ASC")
  LiveData<List<Score>> getScoreByCourseAndType(String c_id, int s_type);

}
