package vn.gq.udv.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class DBAdapter
{
  public static final int FIELD_TYPE_BLOB = 4;
  public static final int FIELD_TYPE_FLOAT = 2;
  public static final int FIELD_TYPE_INTEGER = 1;
  public static final int FIELD_TYPE_NULL = 0;
  public static final int FIELD_TYPE_STRING = 3;
  private static final String TAG = DBAdapter.class.getSimpleName();
  private static volatile DBAdapter instance;
  private Context context;
  private SQLiteDatabase db;
  private MySQLiteHelper dbHelper;

  public DBAdapter(Context paramContext)
  {
    this.context = paramContext;
  }

  public static DBAdapter getInstance(Context paramContext)
  {
      if (instance == null)
        instance = new DBAdapter(paramContext);
      return instance;
  }

  public void close()
  {
    if (this.db != null)
    {
      if (this.db.isOpen())
        this.db.close();
      this.db = null;
    }
    if (this.dbHelper != null)
    {
      this.dbHelper.close();
      this.dbHelper = null;
    }
  }

  public boolean contains(String paramString1, String paramString2, String paramString3)
  {
    LinkedHashMap localLinkedHashMap = fetchOne(paramString1, new String[] { paramString2 }, paramString3, null);
    return (localLinkedHashMap != null) && (localLinkedHashMap.size() > 0);
  }

  public boolean delete(String s, String s1)
  {
      boolean flag1 = false;
      boolean flag = flag1;
      if (isOpen())
      {
          int i;
          try
          {
              i = db.delete(s, s1, null);
          }
          catch (SQLiteException localSQLiteException)
          {
              localSQLiteException.printStackTrace();
              return false;
          }
          flag = flag1;
          if (i > 0)
          {
              flag = true;
          }
      }
      return flag;
  }

  public void excuteSQL(String paramString)
  {
    try
    {
      this.db.execSQL(paramString);
      return;
    }
    catch (SQLiteException localSQLiteException)
    {
      localSQLiteException.printStackTrace();
    }
  }

  public ArrayList<LinkedHashMap<String, Object>> fetch(String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, int paramInt1, int paramInt2, String paramString4)
  {
    ArrayList localArrayList = new ArrayList();
    Hashtable localHashtable=null;
    Cursor localCursor=null;
    LinkedHashMap localLinkedHashMap=null;
    int j=0;
    if (isOpen())
    {
      localHashtable = getAllOfColumnsOfTable(paramString1);
      String str1 = QueryBuilder.selectCommandSql(paramString1, paramArrayOfString, paramString2, paramString3, paramInt1, paramInt2, paramString4);
      System.out.println("SQL Query = " + str1);
      localCursor = this.db.rawQuery(str1, null);
      if ((localCursor != null) && (localCursor.moveToFirst()))
        do
        {
          localLinkedHashMap = new LinkedHashMap();
          if ((paramArrayOfString.length == 1) && (paramArrayOfString[0].equals("*")))
            paramArrayOfString = localCursor.getColumnNames();
          int i = paramArrayOfString.length;
          j = 0;
          if (j < i)
            break;
          if (localLinkedHashMap.size() <= 0)
            continue;
          localArrayList.add(localLinkedHashMap);
        }
        while (localCursor.moveToNext());
      localCursor.close();
    }
    String str2 = paramArrayOfString[j];
    int k = localCursor.getColumnIndex(str2);
    switch (((Integer)localHashtable.get(str2)).intValue())
    {
	    case 3:
	    case 2:
	    case 1:
	    case 4:
	    	 localLinkedHashMap.put(str2, new String(localCursor.getString(k)));
	    	 localLinkedHashMap.put(str2, new Float(localCursor.getFloat(k)));
	    	 localLinkedHashMap.put(str2, new Integer(localCursor.getInt(k)));
	    	 localLinkedHashMap.put(str2, new String(localCursor.getBlob(k)));
    }
    return localArrayList;
  }

  public ArrayList<LinkedHashMap<String, Object>> fetchAll(String paramString1, String paramString2, String paramString3)
  {
    return fetchAll(paramString1, paramString2, paramString3, 0, 0);
  }

  public ArrayList<LinkedHashMap<String, Object>> fetchAll(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
  {
    return fetch(paramString1, QueryBuilder.ALL_COLUMN, paramString2, paramString3, paramInt1, paramInt2, null);
  }

  public LinkedHashMap<String, Object> fetchOne(String paramString1, String[] paramArrayOfString, String paramString2, String paramString3)
  {
    ArrayList localArrayList = fetch(paramString1, paramArrayOfString, paramString2, paramString3, 1, 0, null);
    if (localArrayList.size() > 0)
      return (LinkedHashMap)localArrayList.get(0);
    return new LinkedHashMap();
  }

  protected void finalize()
    throws Throwable
  {
    close();
    super.finalize();
  }

  public Hashtable<String, Integer> getAllOfColumnsOfTable(String paramString)
  {
	    Hashtable localHashtable = new Hashtable();
	    try
	    {
	      Cursor localCursor = this.db.rawQuery("PRAGMA table_info(" + paramString + ")", null);
	      if (localCursor != null)
	      {
	        String str1="";
	        String str2="";
	        int i=0;
	        if (localCursor.moveToFirst())
	        {
	          str1 = localCursor.getString(localCursor.getColumnIndex("name"));
	          str2 = localCursor.getString(localCursor.getColumnIndex("type"));
	          if (!str2.matches(".*INT.*"))
	          {
	        	  if (str2.matches(".*CHAR.*|TEXT|CLOB"))
		          {
		            i = 3;
		            localHashtable.put(str1, Integer.valueOf(i));
		          }
	        	  else
		          {
		        	  i = 1;
		          }
	          }
	        }
	        /*while (true)
	        {
	          localHashtable.put(str1, Integer.valueOf(i));
	          if (localCursor.moveToNext())
	            break;
	          localCursor.close();
	          //return localHashtable;
	          if (str2.matches(".*CHAR.*|TEXT|CLOB"))
	          {
	            i = 3;
	            continue;
	          }
	          if (str2.matches(".*DOUBLE.*|REAL|FLOAT"))
	          {
	            i = 2;
	            continue;
	          }
	          boolean bool = str2.matches("BLOB");
	          if (bool)
	          {
	            i = 4;
	            continue;
	          }
	        }*/
	      }
	    }
	    catch (SQLiteException localSQLiteException)
	    {
	      localSQLiteException.printStackTrace();
	    }
	    return localHashtable;
  }

  public void insert(String paramString, ContentValues paramContentValues)
  {
      while (paramContentValues == null || !isOpen()) 
      {
          return;
      }
    try
    {
      this.db.insert(paramString, null, paramContentValues);
      return;
    }
    catch (SQLiteException localSQLiteException)
    {
      localSQLiteException.printStackTrace();
    }
  }

  public void insert(String paramString, LinkedHashMap<String, Object> paramLinkedHashMap)
  {

    while (paramLinkedHashMap == null || !isOpen());
    while (true)
    {
      StringBuilder localStringBuilder;
      Iterator localIterator1;
      Iterator localIterator2;
      try
      {
        localStringBuilder = new StringBuilder("INSERT INTO " + paramString + "(");
        localIterator1 = paramLinkedHashMap.keySet().iterator();
        if (!localIterator1.hasNext())
        {
          localStringBuilder.deleteCharAt(-1 + localStringBuilder.length());
          localStringBuilder.append(") VALUES(");
          localIterator2 = paramLinkedHashMap.values().iterator();
          if (localIterator2.hasNext())
        {
        	  Object localObject = localIterator2.next();
              if ((localObject instanceof String))
              {
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = String.valueOf(localObject);
                localStringBuilder.append(String.format("'%s',", arrayOfObject));
                continue;
              }
              localStringBuilder.append(String.valueOf(localObject) + ",");
        }
          localStringBuilder.deleteCharAt(-1 + localStringBuilder.length());
          localStringBuilder.append(");");
          this.db.execSQL(localStringBuilder.toString());
          return;
        }
      }
      catch (SQLiteException localSQLiteException)
      {
        localSQLiteException.printStackTrace();
        return;
      }
    }
  }

  public boolean isOpen()
  {
    return (this.dbHelper != null) && (this.db != null) && (this.db.isOpen());
  }

  public void open()
  {
    if (!isOpen())
    {
      this.dbHelper = new MySQLiteHelper(this.context);
      this.db = this.dbHelper.getWritableDatabase();
      Log.d(TAG, "call open database, database isOpen: " + this.db.isOpen());
    }
  }

  public void update(String paramString1, String paramString2, String paramString3)
  {
    try
    {
      StringBuilder localStringBuilder = new StringBuilder("UPDATE " + paramString1 + " SET " + paramString2);
      if ((paramString3 != null) && (!paramString3.equals("")))
        localStringBuilder.append(" WHERE " + paramString3);
      this.db.execSQL(localStringBuilder.toString());
      return;
    }
    catch (SQLException localSQLException)
    {
      localSQLException.printStackTrace();
    }
  }

  public boolean update(String paramString1, ContentValues paramContentValues, String paramString2)
  {
      boolean flag1 = false;
      boolean flag = flag1;
      if (isOpen())
      {
      	int i=0;
	    try
	    {
	      i = this.db.update(paramString1, paramContentValues, paramString2, null);
	    }
	    catch (SQLiteException localSQLiteException)
	    {
	      localSQLiteException.printStackTrace();
	    }
        flag = flag1;
        if (i > 0)
        {
            flag = true;
        }
      }
      return flag;
	    
  }

  private static class QueryBuilder
  {
    public static final String[] ALL_COLUMN = { "*" };

    public static String selectCommandSql(String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, int paramInt1, int paramInt2, String paramString4)
    {
      StringBuilder localStringBuilder = new StringBuilder("SELECT ");
      for (int i = 0; ; i++)
      {
        if (i >= -1 + paramArrayOfString.length)
        {
          if (paramArrayOfString.length > 0)
            localStringBuilder.append(paramArrayOfString[(-1 + paramArrayOfString.length)] + " ");
          localStringBuilder.append("FROM " + paramString1 + " ");
          if ((paramString2 != null) && (!"".equals(paramString2)))
            localStringBuilder.append("WHERE " + paramString2 + " ");
          if ((paramString3 != null) && (!paramString3.equals("")))
            localStringBuilder.append("ORDER BY " + paramString3 + " ");
          if (paramInt1 > 0)
            localStringBuilder.append("LIMIT " + paramInt1 + " ");
          if (paramInt2 > 0)
            localStringBuilder.append("OFFSET " + paramInt2 + " ");
          if ((paramString4 != null) && (!paramString2.equals("")))
            localStringBuilder.append(paramString2);
          return localStringBuilder.toString();
        }
        localStringBuilder.append(paramArrayOfString[i] + ",");
      }
    }
  }
}