package vn.gq.udv.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper
{
  public static final String DATABASE_NAME = "portal.db";
  public static final int DATABASE_VERSION = 1;
  public static final String TABLE_STORY = "story";
  private static final String TAG = MySQLiteHelper.class.getSimpleName();

  public MySQLiteHelper(Context paramContext)
  {
    super(paramContext, "portal.db", null, 1);
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    Log.d(TAG, "onCreate");
    paramSQLiteDatabase.execSQL(String.format("CREATE TABLE %s(id VARCHAR(32) UNIQUE PRIMARY KEY NOT NULL, title VARCHAR(100) NOT NULL, author VARCHAR(50), numChapter INTEGER DEFAULT 0, favorite INTEGER DEFAULT 0, lastvisit INTEGER DEFAULT 0, lastchapter INTEGER DEFAULT 0);", new Object[] { "story" }));
    paramSQLiteDatabase.execSQL(String.format("CREATE INDEX story_idx ON %S(id, favorite)", new Object[] { "story" }));
    paramSQLiteDatabase.execSQL(String.format("CREATE INDEX story_idx_history ON %S(lastvisit)", new Object[] { "story" }));
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    Log.d(TAG, "call Upgrade database");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS story");
    onCreate(paramSQLiteDatabase);
  }
}