package com.hoangpro.amazingwords.base;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public void openActivity(Class target,Boolean isFinish){

        Intent intent= new Intent(this,target);
        startActivity(intent);
        if (isFinish)
            finish();
    }
}
