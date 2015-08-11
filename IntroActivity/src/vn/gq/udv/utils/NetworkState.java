package vn.gq.udv.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import com.AppTruyen.truyenkiemhiepoffline.NoNetworkActivity;
import com.actionbarsherlock.app.SherlockActivity;

public class NetworkState
{
  private Context context;

  public NetworkState(Context paramContext)
  {
    this.context = paramContext;
  }

  public void check()
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)this.context.getSystemService("connectivity")).getActiveNetworkInfo();
    if ((localNetworkInfo != null) && ((localNetworkInfo.getState() == NetworkInfo.State.CONNECTED) || (localNetworkInfo.getState() == NetworkInfo.State.CONNECTING)))
      return;
    ((SherlockActivity)this.context).finish();
    Intent localIntent = new Intent(this.context, NoNetworkActivity.class);
    localIntent.putExtra("INTENT", ((SherlockActivity)this.context).getIntent());
    this.context.startActivity(localIntent);
  }

  public boolean hasConnection()
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)this.context.getSystemService("connectivity")).getActiveNetworkInfo();
    return (localNetworkInfo != null) && ((localNetworkInfo.getState() == NetworkInfo.State.CONNECTED) || (localNetworkInfo.getState() == NetworkInfo.State.CONNECTING));
  }
}