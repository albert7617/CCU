package com.example.albert.ccu;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Score",
        foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "c_id", onDelete = CASCADE),
        indices = {@Index("c_id")})
public class Score {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "s_id")
  private int score_id;
  public int getScore_id() {
    return score_id;
  }
  public void setScore_id(int score_id) {
    this.score_id = score_id;
  }

  @ColumnInfo(name = "c_id")
  private String course_id;
  public void setCourse_id(String course_id) {
    this.course_id = course_id;
  }
  public String getCourse_id() {
    return course_id;
  }

  @ColumnInfo(name = "s_title")
  private String score_title;
  public void setScore_title(String score_title) {
    this.score_title = score_title;
  }
  public String getScore_title() {
    return score_title;
  }

  @ColumnInfo(name = "s_type_cd")
  private int score_type;
  public int getScore_type() {
    return score_type;
  }
  public void setScore_type(int score_type) {
    this.score_type = score_type;
  }

  @ColumnInfo(name = "s_score")
  private String score_score;
  public String getScore_score() {
    return score_score;
  }
  public void setScore_score(String score_score) {
    this.score_score = score_score;
  }

  @ColumnInfo(name = "s_proportion")
  private String score_proportion;
  public String getScore_proportion() {
    return score_proportion;
  }
  public void setScore_proportion(String score_proportion) {
    this.score_proportion = score_proportion;
  }

  @ColumnInfo(name = "s_rank")
  private String score_rank;
  public String getScore_rank() {
    return score_rank;
  }
  public void setScore_rank(String score_rank) {
    this.score_rank = score_rank;
  }

}
