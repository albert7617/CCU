package com.example.albert.ccu;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Course.class, Announcement.class, Score.class, Material.class}, version = 9, exportSchema = false)
public abstract class CourseRoomDatabase extends RoomDatabase {
  public abstract CourseDao courseDao();
  public abstract AnnouncementDao announcementDao();
  public abstract ScoreDao scoreDao();
  public abstract MaterialDao materialDao();

  private static CourseRoomDatabase INSTANCE;


  static CourseRoomDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (CourseRoomDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                  CourseRoomDatabase.class, "course_database")
                  .fallbackToDestructiveMigration()
                  .build();

        }
      }
    }
    return INSTANCE;
  }
}
