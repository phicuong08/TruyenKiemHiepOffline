package vn.gq.udv.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.AppTruyen.truyenkiemhiepoffline.GQConst;

public class ButtonHelper
{
  private Context context;

  public ButtonHelper(Context paramContext)
  {
    this.context = paramContext;
  }

  public void showMoreApp()
  {
    try
    {
      this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(GQConst.SEARCHURI)));
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      try
      {
        this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/search?q=pub:\"AppTruyen\"&c=apps&sort=1")));
        return;
      }
      catch (Exception localException)
      {
        Toast.makeText(this.context, "Không có ứng dụng Google Play trong máy", 0).show();
      }
    }
  }
}