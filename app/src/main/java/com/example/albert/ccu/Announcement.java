package com.example.albert.ccu;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Announcement",
        foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "c_id", onDelete = CASCADE),
        indices = {@Index("c_id")})
public class Announcement {
  @PrimaryKey
  @NonNull
  private String a_id;
  public String getA_id() {
    return a_id;
  }
  public void setA_id(String a_id) {
    this.a_id = a_id;
  }

  @ColumnInfo(name = "c_id")
  private String course_id;
  public void setCourse_id(String course_id) {
    this.course_id = course_id;
  }
  public String getCourse_id() {
    return course_id;
  }

  @ColumnInfo(name = "a_date")
  private String date;
  public void setDate(String date) {
    this.date = date;
  }
  public String getDate() {
    return this.date;
  }

  @ColumnInfo(name = "a_title")
  private String title;
  public void setTitle(String title) {
    this.title = title;
  }
  public String getTitle() {
    return this.title;
  }

  @ColumnInfo(name = "a_content")
  private String content;
  public void setContent(String content) {
    this.content = content;
  }
  public String getContent() {
    return this.content;
  }
}
