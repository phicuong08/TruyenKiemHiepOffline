package com.AppTruyen.truyenkiemhiepoffline;

import android.app.Application;
import android.content.Context;

public class GQApplication extends Application
{
  private static GQApplication instance;

  public static Context getContext()
  {
    return instance;
  }

  public static GQApplication getInstance()
  {
    return instance;
  }

  public void onCreate()
  {
    instance = this;
    super.onCreate();
  }
}