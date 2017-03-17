package com.example.charles.censorninjamobile2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;
import com.twitter.sdk.android.tweetui.TwitterListTimeline;
import com.twitter.sdk.android.tweetui.UserTimeline;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class CensorNinja extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Y0bwzJniOVfTCbMN31y35osGW";
    private static final String TWITTER_SECRET = "TksfQJrSiziRcqm1wDnjfSVS57OqLT1WIIOs3bAVlWYZT2MeDk";

    private TwitterLoginButton loginButton;
    private View loginButtonView;
    private CensorNinja self1 = this;
    private int censorship_level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censor_ninja);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TwitterCore(authConfig), new TweetComposer());
        censorship_level = 8;

        //LOGIN BUTTON
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButtonView = findViewById(R.id.twitter_login_button);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        loginButtonView.performClick();

        //HOME_TIMELINE
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        final StatusesService statusesService = twitterApiClient.getStatusesService();
        final Callback cb = new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                ListView listView = (ListView) findViewById(R.id.listView);
                List<Tweet> tweets = result.data;

                //CENSORING ALGORITHM
                List<Tweet> censoredtweets = new ArrayList<Tweet>();
                for (Tweet t : tweets) {
                    String msg = new String(t.text);
                    String[] words = msg.split(" ");
                    if (censorship_level != 8) {
                        for (int j = 0; j < words.length; j += censorship_level) {
                            String censored_word = "";
                            for (int i = 0; i < words[j].length(); i++) {
                                censored_word = censored_word + "\u2588";
                            }
                            msg = msg.replace(words[j], censored_word);
                        }
                    }

                    censoredtweets.add(new Tweet(t.coordinates, t.createdAt, t.currentUserRetweet, t.entities,
                            t.favoriteCount, t.favorited, t.filterLevel, t.id, t.idStr, t.inReplyToScreenName, t.inReplyToStatusId,
                            t.inReplyToStatusIdStr, t.inReplyToUserId, t.inReplyToUserIdStr, t.lang, t.place,
                            t.possiblySensitive, t.scopes, t.retweetCount, t.retweeted, t.retweetedStatus,
                            t.source, msg, t.truncated, t.user, t.withheldCopyright, t.withheldInCountries, t.withheldScope));
                }

                FixedTweetTimeline ftTimeline = new FixedTweetTimeline.Builder().setTweets(censoredtweets).build();
                TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(self1).setTimeline(ftTimeline).build();
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(TwitterException e) {
                Log.e("CB", "callback failed");
                Log.e("CB-FAIL",e.toString());
            }
        };
        statusesService.homeTimeline(100, null, null, null, null, null, null, cb);

        //REFRESH
        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        assert swipeLayout != null;
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ListView listView = (ListView) findViewById(R.id.listView);
                SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
                swipeLayout.setRefreshing(true);
                TweetTimelineListAdapter adapter = (TweetTimelineListAdapter) listView.getAdapter();
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        //ListView listView = (ListView) findViewById(R.id.listView);
                        statusesService.homeTimeline(100, null, null, null, null, null, null, cb);
                        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
                        //listView.invalidate();
                        swipeLayout.invalidate();
                        Log.e("SWIPELAYOUT", "refresh success!");
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.e("SWIPELAYOUT", "refresh failed!");
                    }
                });
            }
        });

        //POST
        Button postButton = (Button) findViewById(R.id.postbutton);
        assert postButton != null;
        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Tweet Composer
                File myImageFile = new File("drawable/censoricon2.png");
                Uri myImageUri = Uri.fromFile(myImageFile);

                TweetComposer.Builder builder = new TweetComposer.Builder(self1)
                        .text("")
                        .image(myImageUri);
                builder.show();
            }
        });

        //COUNTRY SELECT
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterA = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapterA);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //ListView listView = (ListView) findViewById(R.id.listView);
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                censorship_level = 8 - position;
                Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                statusesService.homeTimeline(100, null, null, null, null, null, null, cb);
                SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
                //listView.invalidate();
                swipeLayout.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                censorship_level = 8 - 0;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
