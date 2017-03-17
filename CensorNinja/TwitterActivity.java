package com.example.josephmasison.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/*import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;*/

import java.util.ArrayList;

import course.labs.alarmslab.R;


/**
 * Created by josephmasison on 4/17/16.
 */
public class TwitterActivity extends Activity {
    public class Tweet {
        public String username;
        public String message;
        public String image_url;
        public Tweet(String username, String message, String url) {
            this.username = username;
            this.message = message;
            this.image_url = url;
        }
    }

    private Button mPost, mMenu;
    private ListView listView;
    private ArrayList<String> tweets = new ArrayList<String>();
    int selected =0;
    int temp;String text="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter);

        //set up buttons
        mPost = (Button) findViewById(R.id.postButton);
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Facebook activity
                Intent intent = new Intent(getApplicationContext(), AlarmCreateActivity.class);
                startActivity(intent);
            }
        });

        mMenu = (Button) findViewById(R.id.menu);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_popup();
            }
        });

        ImageView mImageView = (ImageView) findViewById(R.id.feed);
        Resources resources = getResources();

        switch (selected) {
            case 0:
                Log.i("TAG", Integer.toString(selected));
                mImageView.setImageDrawable(resources.getDrawable(R.drawable.tw));
                Toast.makeText(getApplicationContext(), "You're in the US", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Log.i("TAG", Integer.toString(selected));
                mImageView.setImageDrawable(resources.getDrawable(R.drawable.face));
                Toast.makeText(getApplicationContext(), "You're in Egypt", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Log.i("TAG", Integer.toString(selected));
                mImageView.setImageDrawable(resources.getDrawable(R.drawable.ffeed));
                Toast.makeText(getApplicationContext(), "You're in Russia", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Log.i("TAG", Integer.toString(selected));
                mImageView.setImageDrawable(resources.getDrawable(R.drawable.in));
                Toast.makeText(getApplicationContext(), "You're in China", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void build_popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Demo Popup");

        builder.setSingleChoiceItems(R.array.arr, selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                temp = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected = temp;

                Log.i("TAG", Integer.toString(temp));
                switch (selected) {
                    case 0:
                        text = "0";
                        break;
                    case 1:
                        text = "1";
                        break;
                    case 2:
                        text = "2";
                        break;
                    case 3:
                        text = "3";
                        break;
                }
            }
        });

        //for building Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog al =builder.create();
        al.show();
    }
/*
    @Override
    protected void onResume(){
        super.onResume();
        //TODO - getTweets and inflate listView
        Intent intent = getIntent();

        String tweet = intent.getStringExtra("TWEET");

        Log.d("TAG", "Resume");

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.feed);

        // Defined Array values to show in ListView
        if(tweet!= null)
            tweets.add(tweet);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, tweets);

        if(listView!=null) {
            // Assign adapter to ListView
            listView.setAdapter(adapter);

            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    // ListView Clicked item index
                    int itemPosition = position;

                    // ListView Clicked item value
                    String itemValue = (String) listView.getItemAtPosition(position);

                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();

                }

            });
        }
    }
/*
    public ArrayList<Tweet> getTweets(String searchTerm, int page) {
        String searchUrl =
                "http://search.twitter.com/search.json?q=@"
                        + searchTerm + "&rpp=100&page=" + page;

        ArrayList<Tweet> tweets =
                new ArrayList<Tweet>();

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(searchUrl);

        ResponseHandler<String> responseHandler =
                new BasicResponseHandler();

        String responseBody = null;
        try {
            responseBody = client.execute(get, responseHandler);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        JSONObject jsonObject = null;
        /*JSONParser parser=new JSONParser();

        try {
            Object obj = parser.parse(responseBody);
            jsonObject=(JSONObject)obj;
        }catch(Exception ex){
            Log.v("TEST", "Exception: " + ex.getMessage());
        }

        JSONArray arr = null;

        try {
            Object j = jsonObject.get("results");
            arr = (JSONArray)j;
        } catch(Exception ex){
            Log.v("TEST","Exception: " + ex.getMessage());
        }

        for(Object t : arr) {
            Tweet tweet = new Tweet(
                    ((JSONObject)t).get("from_user").toString(),
                    ((JSONObject)t).get("text").toString(),
                    ((JSONObject)t).get("profile_image_url").toString()
            );
            tweets.add(tweet);
        }

        return tweets;
    }*/
}
