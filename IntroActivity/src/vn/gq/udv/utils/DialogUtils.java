package vn.gq.udv.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogUtils
{
  private static volatile DialogUtils instance;
  private Context context;

  public DialogUtils(Context paramContext)
  {
    this.context = paramContext;
  }

  public static DialogUtils getInstance(Context paramContext)
  {
      if (instance == null)
        instance = new DialogUtils(paramContext);
      return instance;
  }

  public void showConfirm(String paramString, OnDialogConfirmListener paramOnDialogConfirmListener)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.context);
    localBuilder.setMessage(String.valueOf(paramString));
	    /*.setCancelable(true)
	    .setPositiveButton("Có", new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface paramDialogInterface, int paramInt)
	      {
	        paramDialogInterface.dismiss();
	        if (this.dlgConfirmListener != null)
	          this.val$dlgConfirmListener.onOk();
	      }
	    }).setNegativeButton("Không", new DialogInterface.OnClickListener(paramOnDialogConfirmListener)
	    {
	      public void onClick(DialogInterface paramDialogInterface, int paramInt)
	      {
	        paramDialogInterface.dismiss();
	        if (this.val$dlgConfirmListener != null)
	          this.val$dlgConfirmListener.onCancel();
	      }
    });*/
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.setCancelable(false);
    localAlertDialog.show();
  }

  public void showMessage(String paramString)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.context);
    localBuilder.setMessage(String.valueOf(paramString)).setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.dismiss();
      }
    });
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.setCancelable(false);
    localAlertDialog.show();
  }

  public static abstract interface OnDialogConfirmListener
  {
    public abstract void onCancel();

    public abstract void onOk();
  }
}