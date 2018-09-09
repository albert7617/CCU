package com.example.albert.ccu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {
  public class ScoreViewHolder extends RecyclerView.ViewHolder {
    private final TextView score, title, rank, proportion;
    public ScoreViewHolder(View itemView) {
      super(itemView);
      score = itemView.findViewById(R.id.score);
      title = itemView.findViewById(R.id.score_title);
      rank = itemView.findViewById(R.id.score_rank);
      proportion = itemView.findViewById(R.id.score_proportion);
    }
  }

  private final LayoutInflater layoutInflater;
  private List<Score> allScore;

  ScoreListAdapter(Context context) {
    layoutInflater = LayoutInflater.from(context);
  }
  @NonNull
  @Override
  public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = layoutInflater.inflate(R.layout.score_items, parent, false);
    return new ScoreViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
    if(allScore != null) {
      Score current = allScore.get(position);
      holder.score.setText(current.getScore_score());
      holder.title.setText(current.getScore_title());
      holder.rank.setText(current.getScore_rank());
      holder.proportion.setText(current.getScore_proportion());
    }
  }

  @Override
  public int getItemCount() {
    return allScore == null ? 0 : allScore.size();
  }

  public void setAllScore(List<Score> allScore) {
    this.allScore = allScore;
    notifyDataSetChanged();
  }
}
