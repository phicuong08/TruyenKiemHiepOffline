package vn.gq.udv.utils;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto
{
  public static final String CIPHER_ALGORITHM = "AES";
  public static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
  public static final int KEYSPEC_LENGTH = 16;
  public static byte[] iv = new byte[16];
  private static IvParameterSpec ivSpec = new IvParameterSpec(iv);
  private static SecretKeySpec skeySpec;
  private Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

  public Crypto(String paramString)
    throws Exception
  {
    skeySpec = new SecretKeySpec(toBytePwd(paramString), "AES");
  }

  public static String rightPad(String paramString, int paramInt)
  {
    return String.format("%-" + paramInt + "s", new Object[] { paramString }).replace(' ', '\000');
  }

  public static byte[] toBytePwd(String paramString)
  {
    try
    {
      byte[] arrayOfByte = rightPad(paramString, 16).getBytes("UTF-8");
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
    return null;
  }

  public byte[] decrypt(byte[] paramArrayOfByte)
    throws Exception
  {
    this.cipher.init(2, skeySpec, ivSpec);
    return this.cipher.doFinal(paramArrayOfByte);
  }

  public String decryptFromBase64(String paramString)
    throws Exception
  {
    return new String(decrypt(Base64.decode(paramString, 8)), "UTF-8");
  }

  public byte[] encrypt(String paramString)
    throws Exception
  {
    this.cipher.init(1, skeySpec, ivSpec);
    return this.cipher.doFinal(paramString.getBytes("UTF-8"));
  }

  public String encryptToBase64(String paramString)
    throws Exception
  {
    return Base64.encodeToString(encrypt(paramString), 8);
  }
}