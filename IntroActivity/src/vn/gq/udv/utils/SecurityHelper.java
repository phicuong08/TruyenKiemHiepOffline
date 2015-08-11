package vn.gq.udv.utils;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class SecurityHelper
{
  private static volatile SecurityHelper instance;
  private Context context;

  public SecurityHelper(Context paramContext)
  {
    this.context = paramContext;
  }

  public static SecurityHelper getInstance(Context paramContext)
  {
      if (instance == null)
        instance = new SecurityHelper(paramContext);
      return instance;
  }

  public String encrypt(String paramString)
    throws Exception
  {
    return new String(Base64.encode(encryptToByte(paramString), 8));
  }

  public byte[] encryptToByte(String paramString)
    throws Exception
  {
    if ((paramString == null) || (paramString.equals("")))
      return "".getBytes();
    InputStream localInputStream = this.context.getAssets().open("public.key");
    if (localInputStream == null)
      throw new FileNotFoundException("public.key");
    byte[] arrayOfByte = new byte[localInputStream.available()];
    localInputStream.read(arrayOfByte);
    X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(arrayOfByte);
    PublicKey localPublicKey = KeyFactory.getInstance("RSA").generatePublic(localX509EncodedKeySpec);
    Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    localCipher.init(1, localPublicKey);
    return localCipher.doFinal(paramString.getBytes());
  }
}