package com.example.albert.ccu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AnnouncementListAdapter extends RecyclerView.Adapter<AnnouncementListAdapter.AnnouncementViewHolder> {
  public class AnnouncementViewHolder extends RecyclerView.ViewHolder{
    private final TextView title, content, date;
    public AnnouncementViewHolder(View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.announcement_title);
      content = itemView.findViewById(R.id.announcement_content);
      date = itemView.findViewById(R.id.announcement_date);
    }
  }

  private final LayoutInflater layoutInflater;
  private List<Announcement> allAnnouncement;
  AnnouncementListAdapter(Context context) {
    layoutInflater = LayoutInflater.from(context);
  }

  public void setAllAnnouncement(List<Announcement> allAnnouncement) {
    this.allAnnouncement = allAnnouncement;
    notifyDataSetChanged();
  }
  @NonNull
  @Override
  public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = layoutInflater.inflate(R.layout.announcement_items, parent, false);
    return new AnnouncementViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
    if (allAnnouncement != null) {
      Announcement current = allAnnouncement.get(position);
      holder.title.setText(current.getTitle());
      holder.date.setText(current.getDate());
      holder.content.setText(current.getContent());
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          v.findViewById(R.id.announcement_content).setVisibility(View.VISIBLE);
          v.findViewById(R.id.expand_icon).setVisibility(View.GONE);
        }
      });
    }

  }



  @Override
  public int getItemCount() {
    return allAnnouncement == null ? 0 : allAnnouncement.size();
  }


}
