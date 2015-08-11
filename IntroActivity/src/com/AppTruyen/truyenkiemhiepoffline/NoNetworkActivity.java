package com.AppTruyen.truyenkiemhiepoffline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import vn.gq.udv.utils.NetworkState;

public class NoNetworkActivity extends SherlockActivity
{
  private Handler mHandler;
  private Intent mIntent;
  private ProgressBar mProgressBar;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mHandler = new Handler();
    setContentView(R.layout.no_network_layout);
    this.mIntent = ((Intent)getIntent().getExtras().get("INTENT"));
    Button localButton = (Button)findViewById(R.id.btn_reconnect);
    this.mProgressBar = ((ProgressBar)findViewById(R.id.progress));
    this.mProgressBar.setVisibility(8);
    localButton.setOnClickListener(new android.view.View.OnClickListener()
    {
      Button btnRetry;
      TextView label=(TextView)findViewById(R.id.label_no_network);
      public void onClick(View paramView)
      {
        NoNetworkActivity.this.mProgressBar.setVisibility(0);
        this.label.setVisibility(8);
        this.btnRetry.setVisibility(8);
        if (new NetworkState(NoNetworkActivity.this).hasConnection())
        {
          NoNetworkActivity.this.mHandler.postDelayed(new Runnable()
          {
            public void run()
            {
              NoNetworkActivity.this.mProgressBar.setVisibility(8);
            }
          }
          , 500L);
          NoNetworkActivity.this.startActivity(NoNetworkActivity.this.mIntent);
          NoNetworkActivity.this.finish();
          return;
        }
/*        NoNetworkActivity.this.mHandler.postDelayed(new Runnable(this.label, this.btnRetry)
        {
          public void run()
          {
            NoNetworkActivity.this.mProgressBar.setVisibility(8);
            this.label.setVisibility(0);
            this.btnRetry.setVisibility(0);
          }
        }
        , 1000L);*/
      }
    });
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getSupportMenuInflater().inflate(R.menu.none, paramMenu);
    return super.onCreateOptionsMenu(paramMenu);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
	    case android.R.id.home:
	    	onBackPressed();
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
}