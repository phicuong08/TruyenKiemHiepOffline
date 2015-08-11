package vn.gq.udv.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.gq.udv.db.items.Category;
import vn.gq.udv.db.items.ChapterDetail;
import vn.gq.udv.db.items.ChapterItem;

public class DbAdapter extends DatabaseHelper
{
  public DbAdapter(Context paramContext) throws IOException
  {
    super(paramContext);
  }

  public String decrypt(String paramString)
  {
    return paramString;
  }

  public List<Category> getCategories()
  {
    ArrayList<Category> localArrayList = new ArrayList<Category>();
    openDatabase();
    try
    {
      if (this.mSQLiteDb != null)
      {
        Cursor localCursor = this.mSQLiteDb.query("udv_category", new String[] { "catId", "catName", "root" }, null, null, null, null, null);
        if (localCursor.moveToFirst())
        {
          boolean bool;
          do
          {
            Category localCategory = new Category();
            localCategory.setCatId(localCursor.getInt(localCursor.getColumnIndex("catId")));
            localCategory.setCatName(decrypt(localCursor.getString(localCursor.getColumnIndex("catName"))));
            localCategory.setRoot(localCursor.getInt(localCursor.getColumnIndex("root")));
            localArrayList.add(localCategory);
            bool = localCursor.moveToNext();
          }
          while (bool);
        }
      }
      close();
      return localArrayList;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      close();
      return localArrayList;
    }
  }

  // ERROR //
  public Category getCategoryById(int i)
  {
      Object obj;
      Object obj1;
      obj1 = null;
      openDatabase();
      obj = obj1;
      Object obj3;
      if (mSQLiteDb == null)
      {
          
      }
      obj = mSQLiteDb;
      obj3 = (new StringBuilder(" catId=")).append(i).toString();
      obj3 = ((SQLiteDatabase) (obj)).query("udv_category", new String[] {
          "catId", "catName", "root"
      }, ((String) (obj3)), null, null, null, null);
      obj = obj1;
      if (!((Cursor) (obj3)).moveToFirst())
      {
          
      }
      obj = new Category();
      ((Category) (obj)).setCatId(((Cursor) (obj3)).getInt(((Cursor) (obj3)).getColumnIndex("catId")));
      ((Category) (obj)).setCatName(decrypt(((Cursor) (obj3)).getString(((Cursor) (obj3)).getColumnIndex("catName"))));
      ((Category) (obj)).setRoot(((Cursor) (obj3)).getInt(((Cursor) (obj3)).getColumnIndex("root")));
      close();
      return ((Category) (obj));

  }

  public ChapterDetail getChapterDetailById(int i)
  {
      Object obj;
      Object obj1;
      obj1 = null;
      openDatabase();
      obj = obj1;
      Object obj3;
      if (mSQLiteDb == null)
      {
         
      }
      obj = mSQLiteDb;
      obj3 = (new StringBuilder(" id=")).append(i).toString();
      obj3 = ((SQLiteDatabase) (obj)).query("udv_story", new String[] {
          "id", "detail", "catId"
      }, ((String) (obj3)), null, null, null, null);
      obj = obj1;
      if (!((Cursor) (obj3)).moveToFirst())
      {
        
      }
      obj = new ChapterDetail();
      ((ChapterDetail) (obj)).setId(((Cursor) (obj3)).getInt(((Cursor) (obj3)).getColumnIndex("id")));
      ((ChapterDetail) (obj)).setDetail(decrypt(((Cursor) (obj3)).getString(((Cursor) (obj3)).getColumnIndex("detail"))));
      ((ChapterDetail) (obj)).setCatId(((Cursor) (obj3)).getInt(((Cursor) (obj3)).getColumnIndex("catId")));
      close();
      return ((ChapterDetail) (obj));
  }

  public List<ChapterItem> getChapterItems()
  {
    return getChapterItems(0);
  }

  public List<ChapterItem> getChapterItems(int paramInt)
  {
    ArrayList<ChapterItem> localArrayList = new ArrayList<ChapterItem>();
    openDatabase();
    try
    {
      if (this.mSQLiteDb != null)
      {
        Cursor localCursor = this.mSQLiteDb.query("udv_story", new String[] { "id", "catId", "title" }, " catId=" + paramInt, null, null, null, null);
        if (localCursor.moveToFirst())
        {
          boolean bool;
          do
          {
            ChapterItem localChapterItem = new ChapterItem();
            localChapterItem.setId(localCursor.getInt(localCursor.getColumnIndex("id")));
            localChapterItem.setCatId(localCursor.getInt(localCursor.getColumnIndex("catId")));
            localChapterItem.setTitle(decrypt(localCursor.getString(localCursor.getColumnIndex("title"))));
            localArrayList.add(localChapterItem);
            bool = localCursor.moveToNext();
          }
          while (bool);
        }
      }
      close();
      return localArrayList;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      close();
      return localArrayList;
    }
  }
}