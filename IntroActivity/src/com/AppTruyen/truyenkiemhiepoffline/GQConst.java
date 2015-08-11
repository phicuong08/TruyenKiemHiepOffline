package com.AppTruyen.truyenkiemhiepoffline;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import vn.gq.udv.utils.GQUtils;

public class GQConst
{
  public static final String DETAILURI;
  public static final String DS;
  public static final String EMAIL;
  public static final boolean FINISHED = true;
  public static final String PACKAGENAME;
  public static final String SDCARDROOT;
  public static final String SEARCHURI = "market://search?q=pub:\"AppTruyen\"";
  public static final boolean SHOW_CATEGORY = true;
  public static final boolean SHOW_NEXT_PREV_BTN;
  public static final boolean TYPE_BOOK=false;

  static
  {
    DETAILURI = "market://details?id=" + GQApplication.getContext().getPackageName();
    PACKAGENAME = GQApplication.getContext().getPackageName();
    SDCARDROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    EMAIL = "mailto:tranvantho.apptruyen@gmail.com?subject=report: " + getString(R.string.app_name);
    SHOW_NEXT_PREV_BTN = true;
    DS = File.separator;
  }

  private static String getString(int paramInt)
  {
    return GQApplication.getContext().getString(paramInt);
  }

  public static class DB
  {
    public static final String DATABASENAME = "truyenkiemhiep";
    public static final String DATABASEPATH= "/data/data/" + GQConst.PACKAGENAME + "/databases/" + DATABASENAME;;
    public static final String DATABASEPATH_SD=GQConst.SDCARDROOT + GQConst.DS + ".gqudv" + GQConst.DS + GQUtils.md5(GQConst.PACKAGENAME) + GQConst.DS + GQUtils.md5(DATABASENAME);;
    public static final int DBVERSION = 1;
    public static final boolean PreferExternalStorage = true;
    public static final boolean ENCRYPTED=false;
  }

  public static class WebViewConfig
  {
    public static final boolean LOAD_LOCAL_IMAGES = true;
    public static final boolean TEXT_JUSTIFY = true;
  }
}