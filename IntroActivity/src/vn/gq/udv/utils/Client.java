package vn.gq.udv.utils;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONTokener;

public class Client
{
  private static final String TAG = Client.class.getSimpleName();
  private boolean parseJson = false;

  public void excuteAndListen(String paramString, IClientListener paramIClientListener)
  {
    excuteAndListen(paramString, paramIClientListener, true);
  }

  public void excuteAndListen(String paramString, IClientListener paramIClientListener, boolean paramBoolean)
  {
    DownloadTask localDownloadTask = new DownloadTask();
    localDownloadTask.setListener(paramIClientListener);
    this.parseJson = paramBoolean;
    localDownloadTask.execute(new String[] { paramString });
  }

  private class DownloadTask extends AsyncTask<String, Integer, Object>
  {
    private Client.IClientListener listener;
    private boolean success = false;

    private DownloadTask()
    {
    }

    protected Object doInBackground(String[] paramArrayOfString)
    {
      AndroidHttpClient localAndroidHttpClient = AndroidHttpClient.newInstance("android");
      HttpGet localHttpGet = new HttpGet(paramArrayOfString[0]);
      try
      {
        HttpResponse localHttpResponse = localAndroidHttpClient.execute(localHttpGet);
        int i = localHttpResponse.getStatusLine().getStatusCode();
        if (i != 200)
          return null;
        HttpEntity localHttpEntity = localHttpResponse.getEntity();
        String str1 = localHttpResponse.getFirstHeader("Content-Encoding").getValue();
        Object localObject2 = localHttpEntity.getContent();
        if ((str1 != null) && (str1.equals("gzip")))
          localObject2 = new GZIPInputStream((InputStream)localObject2, 4096);
        byte[] arrayOfByte = new byte[4096];
        StringBuilder localStringBuilder = new StringBuilder();
        while (true)
        {
          int j = ((InputStream)localObject2).read(arrayOfByte);
          if (j == -1)
          {
            ((InputStream)localObject2).close();
            this.success = true;
            String str2 = localStringBuilder.toString();
            return str2;
          }
          localStringBuilder.append(new String(arrayOfByte, 0, j));
        }
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
        return null;
      }
      finally
      {
        localAndroidHttpClient.close();
      }
    }

    protected void onPostExecute(Object paramObject)
    {
      super.onPostExecute(paramObject);
      if (Client.this.parseJson);
      try
      {
        Object localObject = new JSONTokener(String.valueOf(paramObject)).nextValue();
        paramObject = localObject;
        this.listener.onDone(this.success, paramObject);
        return;
      }
      catch (JSONException localJSONException)
      {
        while (true)
          localJSONException.printStackTrace();
      }
    }

    public void setListener(Client.IClientListener paramIClientListener)
    {
      this.listener = paramIClientListener;
    }
  }

  public static abstract interface IClientListener
  {
    public abstract void onDone(boolean paramBoolean, Object paramObject);
  }
}