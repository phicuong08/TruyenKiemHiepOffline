package com.AppTruyen.truyenkiemhiepoffline;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import vn.gq.udv.utils.ButtonHelper;

public class IntroActivity extends SherlockActivity
{
  private InterstitialAd interstitial;
  public int ktexit;

  public void exit()
  {
    if (this.interstitial.isLoaded())
      this.interstitial.show();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setMessage("Hãy dành chút thời gian nhận xét cho ứng dụng!").setCancelable(true).setTitle("ỨNG DỤNG AppTruyen").setIcon(17301516).setPositiveButton("Nhận xét", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
        try
        {
          IntroActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(GQConst.DETAILURI)));
          IntroActivity.this.onDestroy();
          System.runFinalizersOnExit(true);
          System.exit(0);
          return;
        }
        catch (ActivityNotFoundException localActivityNotFoundException)
        {
          while (true)
            Toast.makeText(IntroActivity.this, "Ứng dụng Google Play không được cài đặt trên máy", 0).show();
        }
      }
    }).setNegativeButton("Thoát", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        IntroActivity.this.onDestroy();
        System.runFinalizersOnExit(true);
        System.exit(0);
      }
    });
    localBuilder.create().show();
  }

  public void onBackPressed()
  {
    if (this.ktexit == 1)
    {
      this.ktexit = 2;
      exit();
      return;
    }
    onDestroy();
    System.runFinalizersOnExit(true);
    System.exit(0);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.firstscr);
    String str1 = getString(R.string.author_name);
    this.ktexit = 1;
    if (!str1.trim().equals(""))
      getSupportActionBar().setSubtitle(str1);
    ((Button)findViewById(R.id.btnExit)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (IntroActivity.this.ktexit == 1)
        {
          IntroActivity.this.ktexit = 2;
          IntroActivity.this.exit();
          return;
        }
        IntroActivity.this.onDestroy();
        System.runFinalizersOnExit(true);
        System.exit(0);
      }
    });
    ((Button)findViewById(R.id.btnStore)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        new ButtonHelper(IntroActivity.this).showMoreApp();
      }
    });
    ((Button)findViewById(R.id.btnRead)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        IntroActivity.this.startActivity(new Intent(IntroActivity.this, CategoryActivity.class));
      }
    });
    ((Button)findViewById(R.id.btnContact)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        Intent localIntent = new Intent("android.intent.action.SENDTO");
        localIntent.setData(Uri.parse(GQConst.EMAIL));
        IntroActivity.this.startActivity(Intent.createChooser(localIntent, "Send mail..."));
      }
    });
    this.interstitial = new InterstitialAd(this);
    String str2 = getString(R.string.ADS_Admob_full);
    this.interstitial.setAdUnitId(str2);
    AdRequest localAdRequest = new AdRequest.Builder().build();
    this.interstitial.loadAd(localAdRequest);
    this.interstitial.setAdListener(new AdListener()
    {
      public void onAdLoaded()
      {
      }
    });
  }

  public void onPause()
  {
    super.onPause();
  }

  public void onResume()
  {
    super.onResume();
  }
}