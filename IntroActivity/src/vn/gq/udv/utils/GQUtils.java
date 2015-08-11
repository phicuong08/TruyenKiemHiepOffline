package vn.gq.udv.utils;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GQUtils
{
  public static final String DS = File.separator;
  public static final long MIN_FREE_SPACE = -2147483648L;

  public static long getSdCardFreeMem()
  {
    StatFs localStatFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
    localStatFs.restat(Environment.getExternalStorageDirectory().getAbsolutePath());
    return localStatFs.getAvailableBlocks() * localStatFs.getBlockSize();
  }

  public static String md5(String paramString)
  {
    try
    {
      StringBuilder localStringBuilder = new StringBuilder();
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramString.getBytes());
      byte[] arrayOfByte = localMessageDigest.digest();
      for (int i = 0; ; i++)
      {
        if (i >= arrayOfByte.length)
          return localStringBuilder.toString();
        localStringBuilder.append(Integer.toHexString(0x100 | 0xFF & arrayOfByte[i]).substring(1, 3));
      }
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      localNoSuchAlgorithmException.printStackTrace();
    }
    return null;
  }

  public static boolean sdCardIsReady()
  {
    return (Environment.getExternalStorageState().equals("mounted")) && (getSdCardFreeMem() > -2147483648L);
  }
}