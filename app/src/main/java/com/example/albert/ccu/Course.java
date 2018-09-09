package com.example.albert.ccu;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Course")
public class Course {

  @PrimaryKey
  @NonNull
  private String course_id;

  public String getCourse_id() {
    return course_id;
  }

  public void setCourse_id(String course_id) {
    this.course_id = course_id;
  }

  @ColumnInfo
  private String course_name;

  public String getCourse_name() {
    return course_name;
  }

  public void setCourse_name(String course_name) {
    this.course_name = course_name;
  }

  @ColumnInfo
  private String course_time;

  public String getCourse_time() {
    return course_time;
  }

  public void setCourse_time(String course_time) {
    this.course_time = course_time;
  }

  @ColumnInfo
  private String course_location;

  public String getCourse_location() {
    return course_location;
  }

  public void setCourse_location(String course_location) {
    this.course_location = course_location;
  }

  @ColumnInfo
  private String course_professor;

  public String getCourse_professor() {
    return course_professor;
  }

  public void setCourse_professor(String course_professor) {
    this.course_professor = course_professor;
  }
}
