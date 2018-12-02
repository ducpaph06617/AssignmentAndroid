package com.dev.ducpaph.assignmentandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView btRegister;
    private ImageView btMap;
    private ImageView btNews;
    private ImageView btSocial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRegister = (ImageView) findViewById(R.id.btRegister);
        btMap = (ImageView) findViewById(R.id.btMap);
        btNews = (ImageView) findViewById(R.id.btNews);
        btSocial = (ImageView) findViewById(R.id.btSocial);

        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        btNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewsActivity.class);
                startActivity(intent);
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SocialActivity.class);
                startActivity(intent);
            }
        });
    }

}
