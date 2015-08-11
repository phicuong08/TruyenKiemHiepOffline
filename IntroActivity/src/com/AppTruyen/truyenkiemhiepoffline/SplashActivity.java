package com.AppTruyen.truyenkiemhiepoffline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class SplashActivity extends Activity
{
  protected boolean active = true;
  protected int splashTime = 2000;

  protected void onCreate(Bundle paramBundle)
  {
    requestWindowFeature(1);
    super.onCreate(paramBundle);
    setContentView(R.layout.splash);
    ((ImageView)findViewById(R.id.imageview)).setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
          SplashActivity.this.active = false;
        return true;
      }
    });
    new Thread()
    {
      public void run()
      {
        int i = 0;
        try
        {
          while (true)
          {
            if (SplashActivity.this.active)
            {
              int j = SplashActivity.this.splashTime;
              if (i < j);
            }
            else
            {
              Intent localIntent3;
              return;
            }
            sleep(100L);
            boolean bool = SplashActivity.this.active;
            if (!bool)
              continue;
            i += 100;
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          Intent localIntent2;
          return;
        }
        finally
        {
          Intent localIntent1 = new Intent(SplashActivity.this, IntroActivity.class);
          SplashActivity.this.startActivity(localIntent1);
          SplashActivity.this.finish();
        }
      }
    }
    .start();
  }
}