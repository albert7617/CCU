package com.example.albert.ccu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {
  public class CourseViewHolder extends RecyclerView.ViewHolder {
    private final TextView course_name, course_time, course_professor, course_location;

    public CourseViewHolder(View itemView) {
      super(itemView);
      course_name = itemView.findViewById(R.id.course_name);
      course_time = itemView.findViewById(R.id.course_time);
      course_professor = itemView.findViewById(R.id.course_professor);
      course_location = itemView.findViewById(R.id.course_location);
    }
  }

  private final LayoutInflater layoutInflater;
  private List<Course> allCourse;
  private Context context;
  CourseListAdapter(Context context) {
    layoutInflater = LayoutInflater.from(context);
    this.context = context;
  }

  @NonNull
  @Override
  public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = layoutInflater.inflate(R.layout.main_recyclerview_item, parent, false);
    return new CourseViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull final CourseViewHolder holder, final int position) {
    if (allCourse != null) {
      Course current = allCourse.get(position);
      holder.course_name.setText(current.getCourse_name());
      holder.course_time.setText(current.getCourse_time());
      holder.course_professor.setText(current.getCourse_professor());
      holder.course_location.setText(current.getCourse_location());
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(context instanceof MainActivity) {
            ((MainActivity)context).setDetailFragment(allCourse.get(holder.getAdapterPosition()).getCourse_id(),
                    allCourse.get(holder.getAdapterPosition()).getCourse_name());
          }
        }
      });
    }


  }

  @Override
  public int getItemCount() {
    return allCourse == null ? 0 : allCourse.size();
  }

  public void setCourse(List<Course> courses) {
    this.allCourse = courses;
    notifyDataSetChanged();
  }
}
