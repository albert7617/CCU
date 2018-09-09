package com.example.albert.ccu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import java.lang.reflect.Field;

public class NonSwipeableViewPager extends ViewPager {

  private boolean swipeable = false;

  public NonSwipeableViewPager(Context context) {
    super(context);
    setMyScroller();
  }

  public NonSwipeableViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    setMyScroller();
  }

  public void setSwipable(boolean swipable) {
    this.swipeable = swipable;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    final int action = event.getAction();
    final int actionMasked = action & MotionEvent.ACTION_MASK;
    if(!swipeable && (actionMasked == 2 || actionMasked == 1)) {
      return false;
    } else {
      return super.onTouchEvent(event);
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    final int action = event.getAction();
    final int actionMasked = action & MotionEvent.ACTION_MASK;
    if(!swipeable && (actionMasked == 2 || actionMasked == 1)) {
      return false;
    } else {
      return super.onInterceptTouchEvent(event);
    }
  }



  //down one is added for smooth scrolling

  private void setMyScroller() {
    try {
      Class<?> viewpager = ViewPager.class;
      Field scroller = viewpager.getDeclaredField("mScroller");
      scroller.setAccessible(true);
      scroller.set(this, new MyScroller(getContext()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public class MyScroller extends Scroller {
    public MyScroller(Context context) {
      super(context, new DecelerateInterpolator());
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
      super.startScroll(startX, startY, dx, dy, 250 /*1 secs*/);
    }
  }
}