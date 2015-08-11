package vn.gq.udv.utils;

import android.content.Context;
import java.io.File;

public class FileHelper
{
  private static FileHelper instance;
  private Context context;

  public FileHelper(Context paramContext)
  {
    this.context = paramContext;
  }

  public static FileHelper getInstance(Context paramContext)
  {
    if (instance == null)
      instance = new FileHelper(paramContext);
    return instance;
  }

  // ERROR //
  public void copy(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: ldc 27
    //   2: aload_1
    //   3: invokevirtual 33	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   6: ifeq +4 -> 10
    //   9: return
    //   10: aconst_null
    //   11: astore_3
    //   12: new 35	java/io/File
    //   15: dup
    //   16: aload_2
    //   17: invokespecial 38	java/io/File:<init>	(Ljava/lang/String;)V
    //   20: astore 4
    //   22: aload 4
    //   24: invokevirtual 42	java/io/File:getParentFile	()Ljava/io/File;
    //   27: invokevirtual 46	java/io/File:isDirectory	()Z
    //   30: ifne +12 -> 42
    //   33: aload 4
    //   35: invokevirtual 42	java/io/File:getParentFile	()Ljava/io/File;
    //   38: invokevirtual 49	java/io/File:mkdirs	()Z
    //   41: pop
    //   42: aconst_null
    //   43: astore 5
    //   45: aload_0
    //   46: getfield 15	vn/gq/udv/utils/FileHelper:context	Landroid/content/Context;
    //   49: invokevirtual 55	android/content/Context:getAssets	()Landroid/content/res/AssetManager;
    //   52: aload_1
    //   53: invokevirtual 61	android/content/res/AssetManager:open	(Ljava/lang/String;)Ljava/io/InputStream;
    //   56: astore 5
    //   58: new 63	java/io/FileOutputStream
    //   61: dup
    //   62: aload 4
    //   64: invokespecial 66	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   67: astore 10
    //   69: sipush 1024
    //   72: newarray byte
    //   74: astore 11
    //   76: aload 5
    //   78: aload 11
    //   80: invokevirtual 72	java/io/InputStream:read	([B)I
    //   83: istore 12
    //   85: iload 12
    //   87: ifgt +24 -> 111
    //   90: aload 5
    //   92: invokevirtual 75	java/io/InputStream:close	()V
    //   95: aload 10
    //   97: ifnull +103 -> 200
    //   100: aload 10
    //   102: invokevirtual 78	java/io/FileOutputStream:flush	()V
    //   105: aload 10
    //   107: invokevirtual 79	java/io/FileOutputStream:close	()V
    //   110: return
    //   111: aload 10
    //   113: aload 11
    //   115: iconst_0
    //   116: iload 12
    //   118: invokevirtual 83	java/io/FileOutputStream:write	([BII)V
    //   121: goto -45 -> 76
    //   124: astore 6
    //   126: aload 10
    //   128: astore_3
    //   129: aload 6
    //   131: invokevirtual 86	java/io/IOException:printStackTrace	()V
    //   134: aload 5
    //   136: invokevirtual 75	java/io/InputStream:close	()V
    //   139: aload_3
    //   140: ifnull -131 -> 9
    //   143: aload_3
    //   144: invokevirtual 78	java/io/FileOutputStream:flush	()V
    //   147: aload_3
    //   148: invokevirtual 79	java/io/FileOutputStream:close	()V
    //   151: return
    //   152: astore 9
    //   154: return
    //   155: astore 7
    //   157: aload 5
    //   159: invokevirtual 75	java/io/InputStream:close	()V
    //   162: aload_3
    //   163: ifnull +11 -> 174
    //   166: aload_3
    //   167: invokevirtual 78	java/io/FileOutputStream:flush	()V
    //   170: aload_3
    //   171: invokevirtual 79	java/io/FileOutputStream:close	()V
    //   174: aload 7
    //   176: athrow
    //   177: astore 13
    //   179: return
    //   180: astore 8
    //   182: goto -8 -> 174
    //   185: astore 7
    //   187: aload 10
    //   189: astore_3
    //   190: goto -33 -> 157
    //   193: astore 6
    //   195: aconst_null
    //   196: astore_3
    //   197: goto -68 -> 129
    //   200: return
    //
    // Exception table:
    //   from	to	target	type
    //   69	76	124	java/io/IOException
    //   76	85	124	java/io/IOException
    //   111	121	124	java/io/IOException
    //   134	139	152	java/io/IOException
    //   143	151	152	java/io/IOException
    //   45	69	155	finally
    //   129	134	155	finally
    //   90	95	177	java/io/IOException
    //   100	110	177	java/io/IOException
    //   157	162	180	java/io/IOException
    //   166	174	180	java/io/IOException
    //   69	76	185	finally
    //   76	85	185	finally
    //   111	121	185	finally
    //   45	69	193	java/io/IOException
  }

  public void delete(String paramString)
  {
    try
    {
      File localFile = new File(paramString);
      if (localFile.isFile())
        localFile.delete();
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public String getFilePartName(String s, int i)
  {
      String s1 = s;
      String s2;
      if (i < 10)
      {
          s2 = String.format("%s.00%d", new Object[] {
              s, Integer.valueOf(i)
          });
      } else
      {
          if (i >= 10 && i < 100)
          {
              return String.format("%s.0%d", new Object[] {
                  s, Integer.valueOf(i)
              });
          }
          s2 = s1;
          if (i >= 100)
          {
              s2 = s1;
              if (i < 1000)
              {
                  return String.format("%s.%d", new Object[] {
                      s, Integer.valueOf(i)
                  });
              }
          }
      }
      return s2;
  }

  public boolean isExist(String paramString)
  {
    return new File(paramString).exists();
  }
  //ERROR
  public boolean isExistsFileAsset(String paramString)
  {
	  return false;
  }

  // ERROR //
  public void mergeAndCopy(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: new 35	java/io/File
    //   5: dup
    //   6: aload_2
    //   7: invokespecial 38	java/io/File:<init>	(Ljava/lang/String;)V
    //   10: astore 4
    //   12: aload 4
    //   14: invokevirtual 42	java/io/File:getParentFile	()Ljava/io/File;
    //   17: invokevirtual 46	java/io/File:isDirectory	()Z
    //   20: ifne +12 -> 32
    //   23: aload 4
    //   25: invokevirtual 42	java/io/File:getParentFile	()Ljava/io/File;
    //   28: invokevirtual 49	java/io/File:mkdirs	()Z
    //   31: pop
    //   32: new 63	java/io/FileOutputStream
    //   35: dup
    //   36: aload_2
    //   37: invokespecial 121	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   40: astore 5
    //   42: iconst_1
    //   43: istore 6
    //   45: iload 6
    //   47: bipush 100
    //   49: if_icmple +19 -> 68
    //   52: aload 5
    //   54: ifnull +184 -> 238
    //   57: aload 5
    //   59: invokevirtual 78	java/io/FileOutputStream:flush	()V
    //   62: aload 5
    //   64: invokevirtual 79	java/io/FileOutputStream:close	()V
    //   67: return
    //   68: aload_0
    //   69: aload_1
    //   70: iload 6
    //   72: invokevirtual 123	vn/gq/udv/utils/FileHelper:getFilePartName	(Ljava/lang/String;I)Ljava/lang/String;
    //   75: astore 11
    //   77: aload_0
    //   78: aload 11
    //   80: invokevirtual 125	vn/gq/udv/utils/FileHelper:isExistsFileAsset	(Ljava/lang/String;)Z
    //   83: istore 12
    //   85: iload 12
    //   87: ifeq -35 -> 52
    //   90: aconst_null
    //   91: astore 13
    //   93: aload_0
    //   94: getfield 15	vn/gq/udv/utils/FileHelper:context	Landroid/content/Context;
    //   97: invokevirtual 55	android/content/Context:getAssets	()Landroid/content/res/AssetManager;
    //   100: aload 11
    //   102: invokevirtual 61	android/content/res/AssetManager:open	(Ljava/lang/String;)Ljava/io/InputStream;
    //   105: astore 13
    //   107: sipush 1024
    //   110: newarray byte
    //   112: astore 15
    //   114: aload 13
    //   116: aload 15
    //   118: invokevirtual 72	java/io/InputStream:read	([B)I
    //   121: istore 16
    //   123: iload 16
    //   125: ifgt +19 -> 144
    //   128: aload 13
    //   130: ifnull +8 -> 138
    //   133: aload 13
    //   135: invokevirtual 75	java/io/InputStream:close	()V
    //   138: iinc 6 1
    //   141: goto -96 -> 45
    //   144: aload 5
    //   146: aload 15
    //   148: iconst_0
    //   149: iload 16
    //   151: invokevirtual 83	java/io/FileOutputStream:write	([BII)V
    //   154: goto -40 -> 114
    //   157: astore 14
    //   159: aload 13
    //   161: ifnull +8 -> 169
    //   164: aload 13
    //   166: invokevirtual 75	java/io/InputStream:close	()V
    //   169: aload 14
    //   171: athrow
    //   172: astore 9
    //   174: aload 5
    //   176: astore_3
    //   177: aload 9
    //   179: invokevirtual 95	java/lang/Exception:printStackTrace	()V
    //   182: aload_3
    //   183: ifnull -116 -> 67
    //   186: aload_3
    //   187: invokevirtual 78	java/io/FileOutputStream:flush	()V
    //   190: aload_3
    //   191: invokevirtual 79	java/io/FileOutputStream:close	()V
    //   194: return
    //   195: astore 10
    //   197: return
    //   198: astore 7
    //   200: aload_3
    //   201: ifnull +11 -> 212
    //   204: aload_3
    //   205: invokevirtual 78	java/io/FileOutputStream:flush	()V
    //   208: aload_3
    //   209: invokevirtual 79	java/io/FileOutputStream:close	()V
    //   212: aload 7
    //   214: athrow
    //   215: astore 17
    //   217: return
    //   218: astore 8
    //   220: goto -8 -> 212
    //   223: astore 7
    //   225: aload 5
    //   227: astore_3
    //   228: goto -28 -> 200
    //   231: astore 9
    //   233: aconst_null
    //   234: astore_3
    //   235: goto -58 -> 177
    //   238: return
    //
    // Exception table:
    //   from	to	target	type
    //   93	114	157	finally
    //   114	123	157	finally
    //   144	154	157	finally
    //   68	85	172	java/lang/Exception
    //   133	138	172	java/lang/Exception
    //   164	169	172	java/lang/Exception
    //   169	172	172	java/lang/Exception
    //   186	194	195	java/io/IOException
    //   32	42	198	finally
    //   177	182	198	finally
    //   57	67	215	java/io/IOException
    //   204	212	218	java/io/IOException
    //   68	85	223	finally
    //   133	138	223	finally
    //   164	169	223	finally
    //   169	172	223	finally
    //   32	42	231	java/lang/Exception
  }
}