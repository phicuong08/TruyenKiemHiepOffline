package vn.gq.udv.utils;

import android.content.Context;
import com.AppTruyen.truyenkiemhiepoffline.GQConst;
import com.AppTruyen.truyenkiemhiepoffline.GQConst.DB;
import vn.gq.udv.db.DatabaseHelper;

public class SQLiteDatabaseVersionHandler
{
  public static final String DBVERSION_KEY = "dbversion";
  public static final String OLD_DATABASE_PATH = "/data/data/" + GQConst.PACKAGENAME + "/databases/story";
  private Context context;
  private SharePreferenceUtil pref;

  public SQLiteDatabaseVersionHandler(Context paramContext)
  {
    this.context = paramContext;
    this.pref = new SharePreferenceUtil(paramContext);
  }

  public void checkDBVersionAndUpgrade()
  {
    int i = GQConst.DB.DBVERSION;
    int j = getDatabaseVersion();
    FileHelper localFileHelper = FileHelper.getInstance(this.context);
    if (j != i)
    {
      this.context.deleteDatabase(DatabaseHelper.DBNAME);
      localFileHelper.delete(GQConst.DB.DATABASEPATH_SD);
      setDatabaseVersion(i);
    }
    if ((localFileHelper.isExist(OLD_DATABASE_PATH)) && (!DatabaseHelper.DBNAME.equals("story")))
      this.context.deleteDatabase("story");
  }

  public int getDatabaseVersion()
  {
    return this.pref.getInt("dbversion", 1);
  }

  public void setDatabaseVersion(int paramInt)
  {
    this.pref.putInt("dbversion", paramInt);
  }
}