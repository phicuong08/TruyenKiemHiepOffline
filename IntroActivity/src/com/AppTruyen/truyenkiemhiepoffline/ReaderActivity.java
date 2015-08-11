package com.AppTruyen.truyenkiemhiepoffline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import vn.gq.udv.db.DbAdapter;
import vn.gq.udv.db.items.ChapterDetail;
import vn.gq.udv.db.items.ChapterItem;
import vn.gq.udv.utils.Bookmark;
import vn.gq.udv.utils.MyViewPager;

public class ReaderActivity extends SherlockActivity
  implements View.OnTouchListener, View.OnClickListener
{
  public static final String CATID_KEY;
  public static final String CHAPTERLIST_KEY;
  public static final String POSITION_KEY;
  public static final String SCROLLYPOS;
  public static final String TAG = ReaderActivity.class.getSimpleName();
  public static final int TIMER = 2000;
  public ReaderAdapter localReaderAdapter;
  private AdView adView;
  private ImageView btnNext;
  private ImageView btnPrev;
  private ImageView btnZoomIn;
  private ImageView btnZoomOut;
  private int currentPosition = 0;
  private Runnable hideButtonRun = new Runnable()
  {
    public void run()
    {
      if (ReaderActivity.this.btnNext != null)
        ReaderActivity.this.btnNext.setVisibility(8);
      if (ReaderActivity.this.btnPrev != null)
        ReaderActivity.this.btnPrev.setVisibility(8);
      if (ReaderActivity.this.btnZoomIn != null)
        ReaderActivity.this.btnZoomIn.setVisibility(8);
      if (ReaderActivity.this.btnZoomOut != null)
        ReaderActivity.this.btnZoomOut.setVisibility(8);
    }
  };
  private int mCatId = 0;
  private ArrayList<ChapterItem> mChapterListItem;
  private ChapterItem mCurrentChapter;
  private Handler mHandler;
  private int mfontsize = 16;
  private int scrollYPos = 0;
  private MyViewPager viewPager;
  private WebView[] webviewStack;

  static
  {
    CATID_KEY = "catId";
    POSITION_KEY = "lastpos";
    CHAPTERLIST_KEY = "chapterlst";
    SCROLLYPOS = "scrollY";
  }

  private void init()
  {
    this.mHandler = new Handler();
    this.viewPager = ((MyViewPager)findViewById(R.id.viewPager));
    this.btnZoomIn = ((ImageView)findViewById(R.id.btnZoomIn));
    this.btnZoomOut = ((ImageView)findViewById(R.id.btnZoomOut));
    this.btnZoomIn.setOnClickListener(this);
    this.btnZoomOut.setOnClickListener(this);
    this.btnNext = ((ImageView)findViewById(R.id.btnNext));
    this.btnNext.setOnClickListener(this);
    this.btnPrev = ((ImageView)findViewById(R.id.btnPrev));
    this.btnPrev.setOnClickListener(this);
  }

  public static void startReaderActivity(Context paramContext, ArrayList<? extends Parcelable> paramArrayList, int paramInt1, int paramInt2)
  {
    startReaderActivity(paramContext, paramArrayList, paramInt1, paramInt2, 0);
  }

  public static void startReaderActivity(Context paramContext, ArrayList<? extends Parcelable> paramArrayList, int paramInt1, int paramInt2, int paramInt3)
  {
    Intent localIntent = new Intent(paramContext, ReaderActivity.class);
    localIntent.putParcelableArrayListExtra(CHAPTERLIST_KEY, paramArrayList);
    localIntent.putExtra(POSITION_KEY, paramInt1);
    localIntent.putExtra(CATID_KEY, paramInt2);
    localIntent.putExtra(SCROLLYPOS, paramInt3);
    paramContext.startActivity(localIntent);
  }

  public void onBack()
  {
    super.onBackPressed();
  }

  public void onBackPressed()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setMessage("Bạn có muốn lưu lại trang đang đọc không?").setCancelable(true).setTitle("ỨNG DỤNG AppTruyen").setIcon(17301516).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        SharedPreferences localSharedPreferences = ReaderActivity.this.getSharedPreferences("tho", 0);
        int i = ReaderActivity.this.mCatId;
        SharedPreferences.Editor localEditor = localSharedPreferences.edit();
        localEditor.putInt("bookmark", 1);
        localEditor.putInt("CatIDStore", i);
        localEditor.commit();
        new Bookmark(ReaderActivity.this).setChapterBookmark(ReaderActivity.this.mCurrentChapter);
        ReaderActivity.this.onBack();
      }
    }).setNegativeButton("Thoát", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        ReaderActivity.this.onBack();
      }
    });
    localBuilder.create().show();
  }

  public void onClick(View paramView)
  {
	int i= paramView.getId();
	WebView localWebView1;
	WebView localWebView2;
    if (i==R.id.btnNext)
    {
    	  this.currentPosition=this.getIntent().getExtras().getInt(POSITION_KEY);
	      if (this.currentPosition == -1 + this.mChapterListItem.size())
	        Toast.makeText(this, "Hết! Chúc các bạn vui vẻ", 0).show();
	      if (this.currentPosition <= -1 + this.mChapterListItem.size())
	        this.viewPager.setCurrentItem(1 + this.currentPosition);
	      this.getIntent().putExtra(POSITION_KEY, this.currentPosition);
    }
    else if(i==R.id.btnPrev)
    {
    	this.currentPosition=this.getIntent().getExtras().getInt(POSITION_KEY);
        if (this.currentPosition == 0)
            Toast.makeText(this, "Bạn đang ở chương đầu tiên !", 0).show();
	    if (this.currentPosition > 0)
	        this.viewPager.setCurrentItem(-1 + this.currentPosition);
	    this.getIntent().putExtra(POSITION_KEY, this.currentPosition);
    }
    else if(i==R.id.btnZoomIn)
    {
        if (this.mfontsize > 11)
        {
		    localWebView1 = this.webviewStack[this.currentPosition];
		    this.mfontsize = localWebView1.getSettings().getDefaultFontSize();
	        this.mfontsize = (-2 + this.mfontsize);
	        localWebView1.getSettings().setDefaultFontSize(this.mfontsize);
        }
    }
    else if(i==R.id.btnZoomOut)
    {
        if (this.mfontsize < 36)
        {
        	localWebView2 = this.webviewStack[this.currentPosition];
        	this.mfontsize = localWebView2.getSettings().getDefaultFontSize();
        	this.mfontsize = (2 + this.mfontsize);
        	localWebView2.getSettings().setDefaultFontSize(this.mfontsize);
        }
    }
  }

  protected void onCreate(Bundle adapter)
  {
    super.onCreate(adapter);
    setContentView(R.layout.reader_scr);
    init();
    String str1 = getString(R.string.author_name);
    if (!str1.trim().equals(""))
      getSupportActionBar().setSubtitle(str1);
    this.currentPosition = getIntent().getExtras().getInt(POSITION_KEY);
    if (this.currentPosition < 0)
      this.currentPosition = 0;
    this.scrollYPos = getIntent().getExtras().getInt(SCROLLYPOS);
    this.mChapterListItem = getIntent().getParcelableArrayListExtra(CHAPTERLIST_KEY);
    this.mCatId = getIntent().getExtras().getInt(CATID_KEY);
    localReaderAdapter = new ReaderAdapter(this);
    showButton(false);
    localReaderAdapter.load();
    
    viewPager.setAdapter(localReaderAdapter);
    if (GQConst.SHOW_NEXT_PREV_BTN)
      ReaderActivity.this.showButton(true);

    this.viewPager.setCurrentItem(this.currentPosition);
    if ((this.mChapterListItem != null) && (this.mChapterListItem.size() > 0))
    {
      this.mCurrentChapter = ((ChapterItem)this.mChapterListItem.get(this.currentPosition));
      this.mCurrentChapter.setPosition(this.currentPosition);
    }
    this.adView = new AdView(this);
    this.adView.setAdSize(AdSize.BANNER);
    String str2 = getString(R.string.ADS_Admob);
    this.adView.setAdUnitId(str2);
    ((LinearLayout)findViewById(R.id.ads_reader)).addView(this.adView);
    AdRequest localAdRequest = new AdRequest.Builder().build();
    this.adView.loadAd(localAdRequest);
    this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramInt)
      {
      }

      public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
      {
      }

      public void onPageSelected(int paramInt)
      {
        ReaderActivity.this.mCurrentChapter = ((ChapterItem)ReaderActivity.this.mChapterListItem.get(paramInt));
        ReaderActivity.this.mCurrentChapter.setPosition(paramInt);
        ReaderActivity.this.currentPosition = paramInt;
        AdRequest localAdRequest = new AdRequest.Builder().build();
        ReaderActivity.this.adView.loadAd(localAdRequest);
        ReaderActivity.this.mHandler.removeCallbacks(ReaderActivity.this.hideButtonRun);
        ReaderActivity.this.showButtonTimer(true);
        ReaderActivity.this.btnZoomIn.setEnabled(true);
        ReaderActivity.this.btnZoomOut.setEnabled(true);
      }
    });
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getSupportMenuInflater().inflate(R.menu.read, paramMenu);
    return super.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    super.onDestroy();
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    paramMenuItem.getItemId();
    return super.onOptionsItemSelected(paramMenuItem);
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return super.onPrepareOptionsMenu(paramMenu);
  }

  protected void onResume()
  {
    super.onResume();
    if (GQConst.SHOW_NEXT_PREV_BTN)
    {
      this.viewPager.setPagingEnable(false);
      showButtonTimer(true);
      return;
    }
    this.viewPager.setPagingEnable(true);
    showButton(false);
  }

  protected void onStop()
  {
    super.onStop();
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    WebView localWebView = this.webviewStack[this.currentPosition];
    switch (i & 0xFF)
    {
    default:
      return false;
    case 0:
      showButtonTimer(true);
      return false;
    case 1:
    }
    showButtonTimer(false);
    this.mCurrentChapter.setScrollYPos(localWebView.getScrollY());
    return false;
  }

  public void showButton(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.btnNext.setVisibility(0);
      this.btnPrev.setVisibility(0);
      this.btnZoomIn.setVisibility(0);
      this.btnZoomOut.setVisibility(0);
    }
    else{
	    this.btnNext.setVisibility(8);
	    this.btnPrev.setVisibility(8);
	    this.btnZoomIn.setVisibility(8);
	    this.btnZoomOut.setVisibility(8);
    }
  }

  public void showButtonTimer(boolean paramBoolean)
  {
    this.mHandler.removeCallbacks(this.hideButtonRun);
    if (paramBoolean)
    {
      this.btnNext.setVisibility(0);
      this.btnPrev.setVisibility(0);
      this.btnZoomIn.setVisibility(0);
      this.btnZoomOut.setVisibility(0);
      this.mHandler.postDelayed(this.hideButtonRun, 2000L);
      return;
    }
    this.mHandler.postDelayed(this.hideButtonRun, 2000L);
  }

  private class ReaderAdapter extends PagerAdapter
  {
    private Context context;
    private LayoutInflater mInflater;

    public ReaderAdapter(Context arg2)
    {
      this.context = arg2;
      this.mInflater = LayoutInflater.from(arg2);
    }

    public void destroyItem(View paramView, int paramInt, Object paramObject)
    {
      ((ViewPager)paramView).removeView((View)paramObject);
      ReaderActivity.this.webviewStack[paramInt] = null;
    }

    public int getCount()
    {
      return ReaderActivity.this.mChapterListItem.size();
    }

    public String getItem(int paramInt)
    {
      ChapterItem localChapterItem = (ChapterItem)ReaderActivity.this.mChapterListItem.get(paramInt);
      ChapterDetail localChapterDetail = null;
	try {
		localChapterDetail = new DbAdapter(this.context).getChapterDetailById(localChapterItem.getId());
	} catch (IOException e) {
		e.printStackTrace();
	}
      return ("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head>" + "<body style='" + "text-align:justify; line-height: 28px; font-size:1.3em;" + "'>" + localChapterDetail.getDetail() + "</body></html>").replaceAll("%", "&#37;");
    }

    @SuppressWarnings("deprecation")
	public Object instantiateItem(View paramView, int paramInt)
    {
      System.out.println("------------currentPos: " + ReaderActivity.this.currentPosition);
      System.out.println("------------ipos: " + paramInt);
      ViewGroup localViewGroup = (ViewGroup)this.mInflater.inflate(R.layout.web_view, null, false);
      final WebView localWebView = (WebView)localViewGroup.findViewById(R.id.web_view);
      localWebView.setOnTouchListener(ReaderActivity.this);
      final ProgressBar localProgressBar = (ProgressBar)localViewGroup.findViewById(R.id.progress_bar);
      final TextView localTextView = (TextView)localViewGroup.findViewById(R.id.text_view);
      localProgressBar.setVisibility(0);
      localWebView.setVisibility(8);
      localTextView.setVisibility(0);
      localWebView.loadDataWithBaseURL("file:///android_asset/", getItem(paramInt), "text/html", "UTF-8", "");
      localWebView.setWebViewClient(new WebViewClient()
      {
        public void onPageFinished(WebView paramWebView, String paramString)
        {
          localProgressBar.setVisibility(8);
          localTextView.setVisibility(8);
          localWebView.setVisibility(0);
          super.onPageFinished(paramWebView, paramString);
        }
      });
      if ((ReaderActivity.this.currentPosition == paramInt) && (ReaderActivity.this.scrollYPos != 0))
        localWebView.setPictureListener(new WebView.PictureListener()
        {
          @Deprecated
          public void onNewPicture(WebView paramWebView, Picture paramPicture)
          {
            if (ReaderActivity.this.getSharedPreferences("tho", 0).getInt("bookmark", 0) == 1)
            {
              SharedPreferences.Editor localEditor = ReaderActivity.this.getSharedPreferences("tho", 0).edit();
              localEditor.putInt("bookmark", 0);
              localEditor.commit();
              paramWebView.scrollTo(0, ReaderActivity.this.scrollYPos);
            }
          }
        });
      localWebView.setVisibility(0);
      ReaderActivity.this.webviewStack[paramInt] = localWebView;
      ((ViewPager)paramView).addView(localViewGroup);
      return localViewGroup;
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == (View)paramObject;
    }

    public void load()
    {
      if (ReaderActivity.this.mChapterListItem == null)
      {
	        try {
				ReaderActivity.this.mChapterListItem = ((ArrayList<ChapterItem>)new DbAdapter(ReaderActivity.this).getChapterItems(mCatId));
			} catch (IOException e) {
				e.printStackTrace();
			}
      }
      if (ReaderActivity.this.webviewStack == null)
        webviewStack = new WebView[mChapterListItem.size()];
    }
  }
}