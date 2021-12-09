package com.albert.okutils.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;

import com.albert.okutils.BarUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);
        findViewById(R.id.tv4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv1) {
            //BarUtils.setStatusBarVisible(getWindow());
        } else if (id == R.id.tv2) {
            //BarUtils.setStatusBarGone(getWindow());
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } else if (id == R.id.tv3) {

        } else if (id == R.id.tv4) {

        }
    }
}