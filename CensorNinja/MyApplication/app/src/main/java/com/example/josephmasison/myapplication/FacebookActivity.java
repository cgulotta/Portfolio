package com.example.josephmasison.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import course.labs.alarmslab.R;

/**
 * Created by josephmasison on 4/17/16.
 */
public class FacebookActivity extends Activity {

    private Button mPost;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb);

        mPost = (Button) findViewById(R.id.postButton);
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlarmCreateActivity.class);
                startActivity(intent);
            }
        });

    }
}
