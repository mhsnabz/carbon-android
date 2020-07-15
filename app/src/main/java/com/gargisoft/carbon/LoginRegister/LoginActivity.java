package com.gargisoft.carbon.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gargisoft.carbon.Dialog.WaitDialog;
import com.gargisoft.carbon.Helper.Utils;
import com.gargisoft.carbon.R;

import okhttp3.internal.Util;

public class LoginActivity extends AppCompatActivity {
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        WaitDialog dialog = new WaitDialog(this);
        dialog.showWaitDialog();
    }

    public void signUp(View view)
    {
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);

        startActivity(i);
        Utils.go(LoginActivity.this);

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
