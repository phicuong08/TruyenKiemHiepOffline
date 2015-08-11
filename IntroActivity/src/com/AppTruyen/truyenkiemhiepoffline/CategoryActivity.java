package com.AppTruyen.truyenkiemhiepoffline;

import com.AppTruyen.truyenkiemhiepoffline.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import vn.gq.udv.db.DbAdapter;
import vn.gq.udv.db.items.Category;
import vn.gq.udv.utils.Bookmark;
import vn.gq.udv.utils.ButtonHelper;
import vn.gq.udv.utils.SQLiteDatabaseVersionHandler;
import vn.gq.udv.utils.SharePreferenceUtil;

public class CategoryActivity extends SherlockActivity
  implements View.OnTouchListener
{
  public static final String PREF_STARTCATEGORYACTIVITY = "pref_startcategoryactivity";
  private Bookmark bookmark;
  private TextView chapterNotification;
  private ListView listView;
  private List<Category> mCategories = new ArrayList();
  private ProgressDialog mProgressDlg;

  private void setListAdapter(Runnable paramRunnable)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try {
			CategoryActivity.this.mCategories = new DbAdapter(CategoryActivity.this).getCategories();
		} catch (IOException e) {
			e.printStackTrace();
		}
      }
    }).start();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.catalog_scr);
    String str = getString(R.string.author_name);
    if (!str.trim().equals(""))
      getSupportActionBar().setSubtitle(str);
    this.mProgressDlg = ProgressDialog.show(this, "", "Đang tài dữ liệu, vui lòng chờ!", true);
    new SQLiteDatabaseVersionHandler(this).checkDBVersionAndUpgrade();
    this.listView = ((ListView)findViewById(R.id.chapterlist_ca));
    this.chapterNotification = ((TextView)findViewById(R.id.tvnotification_ca));
    this.chapterNotification.setSelected(true);
    this.bookmark = new Bookmark(this, this.chapterNotification);
    try {
		mCategories = new DbAdapter(CategoryActivity.this).getCategories();
	} catch (IOException e) {
		e.printStackTrace();
	}
    if (CategoryActivity.this.mCategories.size() == 0)
    {
      new SharePreferenceUtil(CategoryActivity.this).putBoolean("pref_startcategoryactivity", false);
      CategoryActivity.this.finish();
    }
    CategoryActivity.this.listView.setAdapter(new CategoryActivity.ChapterItemAdapter(CategoryActivity.this, R.layout.web_view, CategoryActivity.this.mCategories));
    if ((CategoryActivity.this.mProgressDlg != null) && (CategoryActivity.this.mProgressDlg.isShowing()))
      CategoryActivity.this.mProgressDlg.cancel();
    
    this.listView.setAdapter(new ChapterItemAdapter(this, R.id.text_view, this.mCategories));
    this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        if (paramLong != -1L)
        {
          Category localCategory = (Category)CategoryActivity.this.mCategories.get(paramInt);
          SharedPreferences.Editor localEditor = CategoryActivity.this.getSharedPreferences("tho", 0).edit();
          localEditor.putInt("CatIDSelect", localCategory.getCatId());
          localEditor.commit();
          CategoryActivity.this.bookmark.setCategoryBookmark(localCategory);
          ChapterActivity.startActivity(CategoryActivity.this, localCategory.getCatId(), localCategory.getCatName());
        }
      }
    });
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    default:
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
    this.bookmark.showNotificationOnStartUp(true);
    SharedPreferences localSharedPreferences = getSharedPreferences("tho", 0);
    if (localSharedPreferences.getInt("CatIDStore", 0) != localSharedPreferences.getInt("CatIDSelect", 0))
    {
      this.chapterNotification = ((TextView)findViewById(R.id.tvnotification_ca));
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

  private class ChapterItemAdapter extends ArrayAdapter<Category>
  {
    private Context context;
    private List<Category> mChapterItems;

    public ChapterItemAdapter(Context context1, int paramList, List<Category> arg3)
    {
      super(context1,paramList, arg3);
      this.context = context1;
      this.mChapterItems = arg3;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
        paramView = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(R.layout.item_list_catalog, null, false);
      Category localCategory = (Category)this.mChapterItems.get(paramInt);
      ((TextView)paramView.findViewById(R.id.text_view_catalog)).setText(localCategory.getCatName());
      paramView.setTag(localCategory);
      return paramView;
    }
  }
}