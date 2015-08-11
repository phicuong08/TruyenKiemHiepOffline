package vn.gq.udv.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager
{
  private boolean enabled = true;

  public MyViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.enabled)
      return super.onInterceptTouchEvent(paramMotionEvent);
    return false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.enabled)
      return super.onTouchEvent(paramMotionEvent);
    return false;
  }

  public void setPagingEnable(boolean paramBoolean)
  {
    this.enabled = paramBoolean;
  }
}