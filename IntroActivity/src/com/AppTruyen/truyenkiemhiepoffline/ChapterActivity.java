package com.AppTruyen.truyenkiemhiepoffline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import vn.gq.udv.db.DbAdapter;
import vn.gq.udv.db.items.ChapterItem;
import vn.gq.udv.utils.Bookmark;
import vn.gq.udv.utils.ButtonHelper;
import vn.gq.udv.utils.SQLiteDatabaseVersionHandler;

public class ChapterActivity extends SherlockActivity
  implements View.OnTouchListener
{
  private AdView adView;
  private Bookmark bookmark;
  private TextView chapterNotification;
  private ListView listView;
  private int mCatId = 0;
  private List<ChapterItem> mChapterList = new ArrayList<ChapterItem>();
  private ProgressDialog mProgressDlg;

  private void setListAdapter(Runnable paramRunnable)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try {
			mChapterList = new DbAdapter(ChapterActivity.this).getChapterItems(mCatId);
		} catch (IOException e) {
			e.printStackTrace();
		}
      }
    }).start();
  }

  public static void startActivity(Context paramContext, int paramInt, String paramString)
  {
    Intent localIntent = new Intent(paramContext, ChapterActivity.class);
    localIntent.putExtra("CATID", paramInt);
    localIntent.putExtra("CATNAME", paramString);
    paramContext.startActivity(localIntent);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.chapter_scr);
    String str1 = getString(R.string.author_name);
    if (!str1.trim().equals(""))
      getSupportActionBar().setSubtitle(str1);
    this.mProgressDlg = ProgressDialog.show(this, "", "Đang tài dữ liệu, vui lòng chờ!", true);
    new SQLiteDatabaseVersionHandler(this).checkDBVersionAndUpgrade();
    try
    {
      this.mCatId = getIntent().getExtras().getInt("CATID");
      this.listView = ((ListView)findViewById(R.id.chapterlist));
      this.chapterNotification = ((TextView)findViewById(R.id.tvnotification));
      this.chapterNotification.setSelected(true);
      this.bookmark = new Bookmark(this, this.chapterNotification);
      
      try {
			mChapterList = new DbAdapter(ChapterActivity.this).getChapterItems(mCatId);
		} catch (IOException e) {
			e.printStackTrace();
		}
      if ((ChapterActivity.this.mProgressDlg != null) && (ChapterActivity.this.mProgressDlg.isShowing()))
        ChapterActivity.this.mProgressDlg.cancel();

      listView.setAdapter(new ChapterItemAdapter(this, R.id.text_view, mChapterList));
      this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          if (paramLong != -1L)
          {
            ChapterItem localChapterItem = ChapterActivity.this.mChapterList.get(paramInt);
            localChapterItem.setPosition(paramInt);
            ReaderActivity.startReaderActivity(ChapterActivity.this, (ArrayList<ChapterItem>)ChapterActivity.this.mChapterList, paramInt, localChapterItem.getCatId());
          }
        }
      });
      this.adView = (AdView) findViewById(R.id.adView);
      AdRequest localAdRequest = new AdRequest.Builder().build();
      this.adView.loadAd(localAdRequest);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      return;
    }
    catch (NullPointerException localNullPointerException)
    {
      while (true)
        this.mCatId = 0;
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getSupportMenuInflater().inflate(R.menu.index, paramMenu);
    return super.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    if (this.mProgressDlg != null)
      this.mProgressDlg.cancel();
    super.onDestroy();
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    case android.R.id.home:
    	onBackPressed();
    case R.id.menu_info:
    	new ButtonHelper(this).showMoreApp();
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }

  protected void onResume()
  {
    super.onResume();
    this.bookmark.showNotificationOnStartUp();
    SharedPreferences localSharedPreferences = getSharedPreferences("tho", 0);
    if (localSharedPreferences.getInt("CatIDStore", 0) != localSharedPreferences.getInt("CatIDSelect", 0))
    {
      this.chapterNotification = ((TextView)findViewById(R.id.tvnotification));
      this.chapterNotification.setVisibility(8);
    }
  }

  protected void onStop()
  {
    super.onStop();
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    return false;
  }

  private class ChapterItemAdapter extends ArrayAdapter<ChapterItem>
  {
    private Context context;
    private List<ChapterItem> mChapterItems;

    public ChapterItemAdapter(Context contex1, int paramList, List<ChapterItem> localList)
    {
      super(contex1,paramList, localList);
      this.context = contex1;
      this.mChapterItems = localList;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
        paramView = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(R.layout.item_list, null, false);
      ChapterItem localChapterItem = this.mChapterItems.get(paramInt);
      ((TextView)paramView.findViewById(R.id.text_view)).setText(localChapterItem.getTitle());
      paramView.setTag(localChapterItem);
      return paramView;
    }
  }
}