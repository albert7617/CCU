package com.example.albert.ccu;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MaterialDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insert(Material material);

  @Query("SELECT * FROM Material WHERE c_id = :c_id ORDER BY m_date DESC")
  LiveData<List<Material>> getMaterialByCourse(String c_id);
}
