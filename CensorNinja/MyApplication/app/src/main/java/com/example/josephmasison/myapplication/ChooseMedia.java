package com.example.josephmasison.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import course.labs.alarmslab.R;

/**
 * Created by josephmasison on 4/17/16.
 */
public class ChooseMedia extends Activity {

    private ImageButton mToFaceBook;
    private ImageButton mToTwitter;
    private ImageButton mToInsta;
    private ImageButton mToPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_media);

        //set up buttons
        mToFaceBook = (ImageButton) findViewById(R.id.facebookButton);
        mToTwitter = (ImageButton) findViewById(R.id.twitterButton);
        mToInsta = (ImageButton) findViewById(R.id.instaButton);
        mToPin = (ImageButton) findViewById(R.id.pinButton);

        mToFaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Facebook activity
                Intent intent = new Intent(getApplicationContext(), FacebookActivity.class);
                startActivity(intent);
            }
        });

        mToTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Twitter activity
                Intent intent = new Intent(getApplicationContext(), TwitterActivity.class);
                startActivity(intent);
            }
        });

        mToInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Insta activity
                Intent intent = new Intent(getApplicationContext(), InstaActivity.class);
                startActivity(intent);
            }
        });

        mToPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Pin activity
                Intent intent = new Intent(getApplicationContext(), PinActivity.class);
                startActivity(intent);
            }
        });

    }



}
