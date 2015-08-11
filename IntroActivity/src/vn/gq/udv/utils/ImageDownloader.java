package vn.gq.udv.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ImageDownloader
{
  private static final int DELAY_BEFORE_PURGE = 10000;
  private static final int HARD_CACHE_CAPACITY = 10;
  private static final String LOG_TAG = "ImageDownloader";
  private static final ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap(5);
  private Mode mode = Mode.CORRECT;
  private final Handler purgeHandler = new Handler();
  private final Runnable purger = new Runnable()
  {
    public void run()
    {
      ImageDownloader.this.clearCache();
    }
  };
  private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap(5, 0.75F, true)
  {
    protected boolean removeEldestEntry(Entry paramEntry)
    {
      if (size() > 10)
      {
        ImageDownloader.sSoftBitmapCache.put((String)paramEntry.getKey(), new SoftReference((Bitmap)paramEntry.getValue()));
        return true;
      }
      return false;
    }
  };

  private void addBitmapToCache(String paramString, Bitmap paramBitmap)
  {
    if (paramBitmap != null)
      synchronized (this.sHardBitmapCache)
      {
        this.sHardBitmapCache.put(paramString, paramBitmap);
        return;
      }
  }

  private static boolean cancelPotentialDownload(String paramString, ImageView paramImageView)
  {
    BitmapDownloaderTask localBitmapDownloaderTask = getBitmapDownloaderTask(paramImageView);
    if (localBitmapDownloaderTask != null)
    {
      String str = localBitmapDownloaderTask.url;
      if ((str == null) || (!str.equals(paramString)))
        localBitmapDownloaderTask.cancel(true);
    }
    else
    {
      return true;
    }
    return false;
  }

  private static int computeInitialSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2)
  {
    double d1 = paramOptions.outWidth;
    double d2 = paramOptions.outHeight;
    int i=0;
    int j=0;
    /*
    if (paramInt2 == -1)
    {
      i = 1;
      if (paramInt1 != -1)
        break label60;
      j = 128;
      label31: if (j >= i)
        break label84;
    }
    label60: label84: 
    do
    {
      return i;
      i = (int)Math.ceil(Math.sqrt(d1 * d2 / paramInt2));
      break;
      j = (int)Math.min(Math.floor(d1 / paramInt1), Math.floor(d2 / paramInt1));
      break label31;
      if ((paramInt2 == -1) && (paramInt1 == -1))
        return 1;
    }
    while (paramInt1 == -1);*/
    return j;
  }

  private static int computeSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2)
  {
    int i = computeInitialSampleSize(paramOptions, paramInt1, paramInt2);
    if (i <= 8)
    {
      int j = 1;
      while (true)
      {
        if (j >= i)
          return j;
        j <<= 1;
      }
    }
    return 8 * ((i + 7) / 8);
  }

  private void forceDownload(String s, ImageView imageview)
  {
      if (s == null)
      {
          imageview.setImageDrawable(null);
      } else
      if (cancelPotentialDownload(s, imageview))
      {
          BitmapDownloaderTask bitmapdownloadertask;
          switch (mode.ordinal())
          {
          default:
              return;

          case 1: // '\001'
              Bitmap bitmap = downloadBitmap(s);
              addBitmapToCache(s, bitmap);
              imageview.setImageBitmap(bitmap);
              return;

          case 2: // '\002'
              imageview.setMinimumHeight(156);
              (new BitmapDownloaderTask(imageview)).execute(new String[] {
                  s
              });
              return;

          case 3: // '\003'
              bitmapdownloadertask = new BitmapDownloaderTask(imageview);
              break;
          }
          imageview.setImageDrawable(new DownloadedDrawable(bitmapdownloadertask));
          imageview.setMinimumHeight(156);
          bitmapdownloadertask.execute(new String[] {
              s
          });
          return;
      }
  }

  private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView paramImageView)
  {
    if (paramImageView != null)
    {
      Drawable localDrawable = paramImageView.getDrawable();
      if ((localDrawable instanceof DownloadedDrawable))
        return ((DownloadedDrawable)localDrawable).getBitmapDownloaderTask();
    }
    return null;
  }

  private Bitmap getBitmapFromCache(String paramString)
  {
    synchronized (this.sHardBitmapCache)
    {
      Bitmap localBitmap1 = (Bitmap)this.sHardBitmapCache.get(paramString);
      if (localBitmap1 != null)
      {
        this.sHardBitmapCache.remove(paramString);
        this.sHardBitmapCache.put(paramString, localBitmap1);
        return localBitmap1;
      }
      SoftReference localSoftReference = (SoftReference)sSoftBitmapCache.get(paramString);
      if (localSoftReference == null)
    	  return null;
      Bitmap localBitmap2 = (Bitmap)localSoftReference.get();
      if (localBitmap2 != null)
        return localBitmap2;
    }
    sSoftBitmapCache.remove(paramString);
    return null;
  }

  private void resetPurgeTimer()
  {
    this.purgeHandler.removeCallbacks(this.purger);
    this.purgeHandler.postDelayed(this.purger, 10000L);
  }

  public void clearCache()
  {
    this.sHardBitmapCache.clear();
    sSoftBitmapCache.clear();
  }

  public void download(String paramString, ImageView paramImageView)
  {
    resetPurgeTimer();
    Bitmap localBitmap = getBitmapFromCache(paramString);
    if (localBitmap == null)
    {
      forceDownload(paramString, paramImageView);
      return;
    }
    cancelPotentialDownload(paramString, paramImageView);
    paramImageView.setImageBitmap(localBitmap);
  }

  public Bitmap downloadBitmap(String paramString)
  {
    Object localObject1=null;
    if (this.mode == Mode.NO_ASYNC_TASK)
      localObject1 = new DefaultHttpClient();
    while (true)
    {
      HttpGet localHttpGet = new HttpGet(paramString);
      Object localObject2 = null;
      try
      {
        HttpResponse localHttpResponse = ((HttpClient)localObject1).execute(localHttpGet);
        int i = localHttpResponse.getStatusLine().getStatusCode();
        if (i != 200)
        {
          Log.w("ImageDownloader", "Error " + i + " while retrieving bitmap from " + paramString);
          return null;
          ///localObject1 = AndroidHttpClient.newInstance("Android");
          //continue;
        }
        /*localHttpEntity = localHttpResponse.getEntity();
        localObject2 = null;
        if (localHttpEntity != null)
          localInputStream = null;*/
      }
      catch (IOException localIOException)
      {
      }
      catch (IllegalStateException localIllegalStateException)
      {
        while (true)
        {
          HttpEntity localHttpEntity;
          InputStream localInputStream;
          Bitmap localBitmap;
          localHttpGet.abort();
          Log.w("ImageDownloader", "Incorrect URL: " + paramString);
          if (!(localObject1 instanceof AndroidHttpClient))
            continue;
          ((AndroidHttpClient)localObject1).close();
        }
      }
      catch (Exception localException)
      {
        while (true)
        {
          localHttpGet.abort();
          Log.w("ImageDownloader", "Error while retrieving bitmap from " + paramString, localException);
          if (!(localObject1 instanceof AndroidHttpClient))
            continue;
          ((AndroidHttpClient)localObject1).close();
        }
      }
      finally
      {
        if ((localObject1 instanceof AndroidHttpClient))
          ((AndroidHttpClient)localObject1).close();
      }
    }
  }

  public void setMode(Mode paramMode)
  {
    this.mode = paramMode;
    clearCache();
  }

  class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap>
  {
    private final WeakReference<ImageView> imageViewReference;
    private String url;

    public BitmapDownloaderTask(ImageView arg2)
    {
      Object localObject=new Object();
      this.imageViewReference = new WeakReference(localObject);
    }

    protected Bitmap doInBackground(String[] paramArrayOfString)
    {
      this.url = paramArrayOfString[0];
      return ImageDownloader.this.downloadBitmap(this.url);
    }

    protected void onPostExecute(Bitmap paramBitmap)
    {
      if (isCancelled())
        paramBitmap = null;
      ImageDownloader.this.addBitmapToCache(this.url, paramBitmap);
      if (this.imageViewReference != null)
      {
        ImageView localImageView = (ImageView)this.imageViewReference.get();
        //if ((this == ImageDownloader.localImageView) || (ImageDownloader.this.mode != ImageDownloader.Mode.CORRECT))
         // localImageView.setImageBitmap(paramBitmap);
      }
    }
  }

  static class DownloadedBitmap extends BitmapDrawable
  {
    private final WeakReference<ImageDownloader.BitmapDownloaderTask> bitmapDownloaderTaskReference;

    public DownloadedBitmap(ImageDownloader.BitmapDownloaderTask paramBitmapDownloaderTask, Bitmap paramBitmap)
    {
      super();
      this.bitmapDownloaderTaskReference = new WeakReference(paramBitmapDownloaderTask);
    }

    public ImageDownloader.BitmapDownloaderTask getBitmapDownloaderTask()
    {
      return (ImageDownloader.BitmapDownloaderTask)this.bitmapDownloaderTaskReference.get();
    }
  }

  static class DownloadedDrawable extends ColorDrawable
  {
    private final WeakReference<ImageDownloader.BitmapDownloaderTask> bitmapDownloaderTaskReference;

    public DownloadedDrawable(ImageDownloader.BitmapDownloaderTask paramBitmapDownloaderTask)
    {
      super();
      this.bitmapDownloaderTaskReference = new WeakReference(paramBitmapDownloaderTask);
    }

    public ImageDownloader.BitmapDownloaderTask getBitmapDownloaderTask()
    {
      return (ImageDownloader.BitmapDownloaderTask)this.bitmapDownloaderTaskReference.get();
    }
  }

  static class FlushedInputStream extends FilterInputStream
  {
    public FlushedInputStream(InputStream paramInputStream)
    {
      super(paramInputStream);
    }

    public long skip(long paramLong)
      //throws IOException
    {
      /*long l1 = 0L;
      while (true)
      {
        if (l1 >= paramLong);
        long l2;
        while (true)
        {
          return l1;
          l2 = this.in.skip(paramLong - l1);
          if (l2 != 0L)
            break;
          if (read() < 0)
            continue;
          l2 = 1L;
        }
        l1 += l2;
      }*/
    	return 1L;
    }
  }

  public enum Mode
  {
      NO_ASYNC_TASK,
      NO_DOWNLOADED_DRAWABLE,
      CORRECT,
      arrayOfMode
  }
}