package com.example.albert.ccu;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {

  private int margin;
  MarginItemDecoration(int margin) {
    this.margin = margin;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    if(parent.getChildAdapterPosition(view) == 0) {
      outRect.top = this.margin;
    }
    outRect.bottom = this.margin;
    outRect.left = this.margin;
    outRect.right = this.margin;
  }
}
