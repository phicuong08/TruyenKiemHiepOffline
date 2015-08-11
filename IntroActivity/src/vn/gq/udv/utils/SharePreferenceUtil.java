package vn.gq.udv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.AppTruyen.truyenkiemhiepoffline.GQApplication;

public class SharePreferenceUtil
{
  private static SharePreferenceUtil instance;
  private SharedPreferences pref;

  public SharePreferenceUtil(Context paramContext)
  {
    this.pref = paramContext.getSharedPreferences(GQApplication.getContext().getPackageName(), 0);
  }

  public static SharePreferenceUtil getInstance(Context paramContext)
  {
    if (instance == null)
      instance = new SharePreferenceUtil(paramContext);
    return instance;
  }

  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    return this.pref.getBoolean(paramString, paramBoolean);
  }

  public float getFloat(String paramString, float paramFloat)
  {
    return this.pref.getFloat(paramString, paramFloat);
  }

  public int getInt(String paramString, int paramInt)
  {
    return this.pref.getInt(paramString, paramInt);
  }

  public float getLong(String paramString, long paramLong)
  {
    return (float)this.pref.getLong(paramString, paramLong);
  }

  public String getString(String paramString1, String paramString2)
  {
    return this.pref.getString(paramString1, paramString2);
  }

  public void putBoolean(String paramString, boolean paramBoolean)
  {
    SharedPreferences.Editor localEditor = this.pref.edit();
    localEditor.putBoolean(paramString, paramBoolean);
    localEditor.commit();
  }

  public void putFloat(String paramString, float paramFloat)
  {
    SharedPreferences.Editor localEditor = this.pref.edit();
    localEditor.putFloat(paramString, paramFloat);
    localEditor.commit();
  }

  public void putInt(String paramString, int paramInt)
  {
    SharedPreferences.Editor localEditor = this.pref.edit();
    localEditor.putInt(paramString, paramInt);
    localEditor.commit();
  }

  public void putLong(String paramString, long paramLong)
  {
    SharedPreferences.Editor localEditor = this.pref.edit();
    localEditor.putLong(paramString, paramLong);
    localEditor.commit();
  }

  public void putString(String paramString1, String paramString2)
  {
    SharedPreferences.Editor localEditor = this.pref.edit();
    localEditor.putString(paramString1, paramString2);
    localEditor.commit();
  }
}