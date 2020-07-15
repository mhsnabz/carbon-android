package com.gargisoft.carbon.Dialog;

import android.app.Activity;
import android.app.Dialog;

import com.gargisoft.carbon.R;

public class WaitDialog {

    Dialog dialog;
    Activity activity;

    public WaitDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity);
    }

   public void showWaitDialog(){
        dialog.setContentView(R.layout.wait_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public void dissmisWaitDialog(){
        dialog.dismiss();
    }
}
