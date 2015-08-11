package vn.gq.udv.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.AppTruyen.truyenkiemhiepoffline.ReaderActivity;
import org.json.JSONObject;
import vn.gq.udv.db.items.Category;
import vn.gq.udv.db.items.ChapterItem;

public class Bookmark
  implements View.OnClickListener
{
  public static final String PREF_CATEGORY_KEY = "pref_category";
  public static final String PREF_CHAPTER_KEY = "pref_chapter";
  public static final String PREF_NOTIFICATION_KEY = "pref_notification";
  private Context context;
  private TextView notifiTextView;
  private SharePreferenceUtil pref;

  public Bookmark(Context paramContext)
  {
    this.context = paramContext;
    this.pref = new SharePreferenceUtil(paramContext);
  }

  public Bookmark(Context paramContext, TextView paramTextView)
  {
    this.context = paramContext;
    this.pref = new SharePreferenceUtil(paramContext);
    this.notifiTextView = paramTextView;
    this.notifiTextView.setOnClickListener(this);
  }

  public Category getCategoryBookmark()
  {
    try
    {
      String str = this.pref.getString(PREF_CATEGORY_KEY, null);
      Category localObject = null;
      if (str != null)
      {
        Category localCategory = new Category(str);
        localObject = localCategory;
      }
      return localObject;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public ChapterItem getChapterBookmark()
  {
    try
    {
      ChapterItem localChapterItem = new ChapterItem(this.pref.getString(PREF_CHAPTER_KEY, null));
      return localChapterItem;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public boolean getNotification()
  {
    return this.pref.getBoolean(PREF_NOTIFICATION_KEY, false);
  }

  public void onClick(View paramView)
  {
    this.notifiTextView.setVisibility(8);
    setNotification(false);
    ChapterItem localChapterItem = getChapterBookmark();
    Category localCategory = getCategoryBookmark();
    int i = 0;
    if (localCategory != null)
      i = localCategory.getCatId();
    removeChapterBookmark();
    int j = localChapterItem.getScrollY();
    Context localContext = this.context;
    int k = 0;
    if (localChapterItem != null)
        k = localChapterItem.getPosition();
    ReaderActivity.startReaderActivity(localContext, null, k, i, j);
  }

  public void removeChapterBookmark()
  {
    this.pref.putString(PREF_CHAPTER_KEY, "");
    setNotification(false);
  }

  public void setCategoryBookmark(Category paramCategory)
  {
    this.pref.putString(PREF_CATEGORY_KEY, paramCategory.toJSONObject().toString());
  }

  public void setChapterBookmark(ChapterItem paramChapterItem)
  {
    this.pref.putString(PREF_CHAPTER_KEY, paramChapterItem.toJSonObject().toString());
    setNotification(true);
  }

  public void setNotification(boolean paramBoolean)
  {
    this.pref.putBoolean(PREF_NOTIFICATION_KEY, paramBoolean);
  }

  public void showNotificationOnStartUp()
  {
    showNotificationOnStartUp(false);
  }

  public void showNotificationOnStartUp(boolean paramBoolean)
  {
    if (getNotification())
    {
      this.notifiTextView.setVisibility(0);
      ChapterItem localChapterItem = getChapterBookmark();
      if (localChapterItem != null)
      {
        String str1 = "Bạn đang đọc: ";
        if (paramBoolean)
        {
          Category localCategory = getCategoryBookmark();
          if (localCategory != null)
            str1 = str1 + "truyện " + localCategory.getCatName() + " > ";
        }
        String str2 = str1 + localChapterItem.getTitle();
        this.notifiTextView.setText(str2);
        return;
      }
      setNotification(false);
      return;
    }
    this.notifiTextView.setVisibility(8);
  }
}