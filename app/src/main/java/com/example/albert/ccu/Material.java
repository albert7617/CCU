package com.example.albert.ccu;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Material",
        foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "c_id", onDelete = CASCADE),
        indices = {@Index("c_id")})
public class Material {
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "m_link")
  private String material_link;
  public String getMaterial_link() {
    return material_link;
  }
  public void setMaterial_link(String material_link) {
    this.material_link = material_link;
  }

  @ColumnInfo(name = "c_id")
  private String course_id;
  public String getCourse_id() {
    return course_id;
  }
  public void setCourse_id(String course_id) {
    this.course_id = course_id;
  }

  @ColumnInfo(name = "m_decs")
  private String material_desc;
  public String getMaterial_desc() {
    return material_desc;
  }
  public void setMaterial_desc(String material_desc) {
    this.material_desc = material_desc;
  }

  @ColumnInfo(name = "m_date")
  private String material_date;
  public String getMaterial_date() {
    return material_date;
  }
  public void setMaterial_date(String material_date) {
    this.material_date = material_date;
  }

  @ColumnInfo(name = "m_size")
  private String material_size;
  public String getMaterial_size() {
    return material_size;
  }
  public void setMaterial_size(String material_size) {
    this.material_size = material_size;
  }
}
