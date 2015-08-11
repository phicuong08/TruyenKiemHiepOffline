package vn.gq.udv.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher
{
  public static final int BLOCK_SIZE = 32;
  private Cipher cipher;
  private String iv = "fedcba9876543210";
  private IvParameterSpec ivSpec = new IvParameterSpec(this.iv.getBytes());
  private SecretKeySpec keySpec;

  public AESCipher(String paramString)
  {
    try
    {
      this.keySpec = new SecretKeySpec(padString(paramString).getBytes("cp1251"), "AES");
      this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
      return;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      localNoSuchAlgorithmException.printStackTrace();
      return;
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      localNoSuchPaddingException.printStackTrace();
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
  }

  private static String padString(String paramString)
  {
    int i = 32 - paramString.length() % 32;
    char c = (char)i;
    for (int j = 0; ; j++)
    {
      if (j >= i)
        return paramString;
      paramString = paramString + c;
    }
  }

  private static String unpadString(String paramString)
  {
    return paramString.substring(0, paramString.length() - paramString.charAt(-1 + paramString.length()));
  }

  public String decrypt(String paramString)
    throws Exception
  {
    return decrypt(paramString, 0);
  }

  public String decrypt(String paramString, int paramInt)
    throws Exception
  {
    if ((paramString == null) || (paramString.length() == 0))
      throw new Exception("Empty string");
    try
    {
      byte[] arrayOfByte1 = Base64.decode(paramString, paramInt);
      byte[] arrayOfByte2 = subByteArray(arrayOfByte1, 0, 15);
      byte[] arrayOfByte3 = subByteArray(arrayOfByte1, 16, -1);
      this.ivSpec = new IvParameterSpec(arrayOfByte2);
      this.cipher.init(2, this.keySpec, this.ivSpec);
      String str = unpadString(new String(this.cipher.doFinal(arrayOfByte3)));
      return str;
    }
    catch (Exception localException)
    {
    	throw new Exception("[decrypt] " + localException.getMessage());
    }
  }

  public String encrypt(String paramString)
    throws Exception
  {
    return encrypt(paramString, 0);
  }

  public String encrypt(String paramString, int paramInt)
    throws Exception
  {
    if ((paramString == null) || (paramString.length() == 0))
      throw new Exception("Empty string");
    this.cipher.init(1, this.keySpec, this.ivSpec);
    byte[] arrayOfByte = this.cipher.doFinal(padString(paramString).getBytes("cp1251"));
    return Base64.encodeToString(mergeByteArray(this.iv.getBytes(), arrayOfByte), paramInt);
  }

  public byte[] mergeByteArray(byte[] first, byte[] second)
  {
      byte[] result = Arrays.copyOf(first, first.length + second.length);
      System.arraycopy(second, 0, result, first.length, second.length);
      return result;
  }

  public byte[] subByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = null;
    int i=0;
    int j=0;
    if (paramArrayOfByte != null)
    {
      if (paramInt2 == -1)
        paramInt2 = -1 + paramArrayOfByte.length;
      i = 1 + (paramInt2 - paramInt1);
      arrayOfByte = null;
      if (i > 0)
      {
        arrayOfByte = new byte[i];
        j = 0;
      }
    }
    for (int k = 0 + paramInt1; ; k++)
    {
      if (j >= i)
        return arrayOfByte;
      arrayOfByte[j] = paramArrayOfByte[k];
      j++;
    }
  }
}