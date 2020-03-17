package com.example.aspectjdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.aspect.Filter;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener()
        {
            @Filter
            @Override
            public void onClick(View v)
            {
                Log.e("MainActivity", "Process onClick");
            }
        });
    }
}
