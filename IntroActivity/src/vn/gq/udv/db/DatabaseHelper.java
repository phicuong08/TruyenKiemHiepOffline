package vn.gq.udv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.AppTruyen.truyenkiemhiepoffline.GQConst;
import com.AppTruyen.truyenkiemhiepoffline.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import vn.gq.udv.db.items.Category;
import vn.gq.udv.db.items.ChapterDetail;
import vn.gq.udv.db.items.ChapterItem;
import vn.gq.udv.utils.GQUtils;

public abstract class DatabaseHelper extends SQLiteOpenHelper
{
  public static final String DBNAME=GQConst.DB.DATABASENAME;
  public static final String DBPATH=GQConst.DB.DATABASEPATH;
  public static final String TAG = DatabaseHelper.class.getSimpleName();
  public static final int VERSION=1;
  protected Context mContext;
  protected SQLiteDatabase mSQLiteDb;
  private String db_path = "";

  public DatabaseHelper(Context paramContext) throws IOException
  {
    super(paramContext, DBNAME, null, VERSION);
    this.mContext = paramContext;
    this.db_path=paramContext.getDatabasePath(paramContext.getResources().getString(R.string.database_name)).getPath();
    createDatabase();
  }

  public DatabaseHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt) throws IOException
  {
    super(paramContext, paramString, paramCursorFactory, paramInt);
    this.db_path=paramContext.getDatabasePath(paramContext.getResources().getString(R.string.database_name)).getPath();
    createDatabase();
  }

  private void copyDatabase() throws IOException
  {
	    InputStream localInputStream = this.mContext.getAssets().open(DBNAME);
	    FileOutputStream localFileOutputStream = new FileOutputStream(this.db_path);
	    byte[] arrayOfByte = new byte[1024];
	    while (true)
	    {
	      int i = localInputStream.read(arrayOfByte);
	      if (i <= 0)
	      {
	        localFileOutputStream.flush();
	        localFileOutputStream.close();
	        localInputStream.close();
	        return;
	      }
	      localFileOutputStream.write(arrayOfByte, 0, i);
	    }
  }

  public void close()
  {
    try
    {
      if ((this.mSQLiteDb != null) && (this.mSQLiteDb.isOpen()))
        this.mSQLiteDb.close();
      super.close();
      return;
    }
    catch (Exception localException)
    {
        localException.printStackTrace();
    }
  }

  public void createDatabase() throws IOException
  {
    if (!isExists())
    {
      getReadableDatabase();
      copyDatabase();
    }
  }

  public abstract List<Category> getCategories();

  public abstract Category getCategoryById(int paramInt);

  public abstract ChapterDetail getChapterDetailById(int paramInt);

  public abstract List<ChapterItem> getChapterItems();

  public abstract List<ChapterItem> getChapterItems(int paramInt);

  public SQLiteDatabase getDatabase()
  {
    return SQLiteDatabase.openDatabase(getDatabasePath(), null, 1);
  }

  public String getDatabasePath()
  {
    return db_path;
  }
  public boolean isExists()
  {
	  return new File(this.db_path).exists();
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    try {
		copyDatabase();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    onCreate(paramSQLiteDatabase);
  }

  public void openDatabase()
  {
	  try
	  {
		this.mSQLiteDb = SQLiteDatabase.openDatabase(db_path, null, 0);
	  }
	  catch (SQLiteException localSQLiteException)
	  {
		  localSQLiteException.printStackTrace();
	  }
  }

  public boolean sdcardAvailable()
  {
    return (GQUtils.sdCardIsReady()) && (GQConst.DB.PreferExternalStorage);
  }
}