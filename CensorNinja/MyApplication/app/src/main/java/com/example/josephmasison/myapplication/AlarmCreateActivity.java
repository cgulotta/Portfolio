package com.example.josephmasison.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

import course.labs.alarmslab.R;

public class AlarmCreateActivity extends Activity {

	public static final String TWEET_STRING = "TWEET";
	private AlarmManager mAlarmManager;

	private static final String TAG = "AlarmCreateActivity";
	private EditText mTweetTextView;
	private TextView mPreviewText;
	private int mID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//TO/DO - Set up an AlarmManager
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		mTweetTextView = (EditText) findViewById(R.id.text);
		mPreviewText = (TextView) findViewById(R.id.previewText);
	}

	public void preview(View v) {
		String tweetText = mTweetTextView.getText().toString();
		String pText = tweetText.replace("Maryland", "Banana");

		mPreviewText.setText(pText);
		mPreviewText.setTextSize(24);
	}

	public void set(View v) {
		String tweetText = mTweetTextView.getText().toString();
		tweetText = tweetText.replace("Maryland", "Banana");

		//TO/DO - Create an Intent to start the AlarmTweetService
		Intent mAlarmTweetServiceIntent = new Intent(AlarmCreateActivity.this,
				AlarmTweetService.class);

		//TO/DO - Add the tweet as an extra to the Intent
		mAlarmTweetServiceIntent.putExtra(TWEET_STRING, tweetText);

		//TO/DO - Create a PendingIntent that will use the Intent above to start the
		// AlarmTweetService. Use PendingIntent.getService().

		// Pass in a unique value for the request code. Otherwise, your
		// pendingIntent will be overridden if there are two or more at any one time.

		// Pass in PendingIntent.FLAG_ONE_SHOT as a flag, which will make
		// sure that your PendingIntent is only used once.

		Random rand = new Random();
		mID = rand.nextInt();
		Log.d(TAG, Integer.toString(mID));
		PendingIntent pendingIntent = PendingIntent.getService(
				this.getApplicationContext(),
				mID,
				mAlarmTweetServiceIntent,
				PendingIntent.FLAG_ONE_SHOT);

		Log.d(TAG, "Tweet sent to AlarmTweetService");
		Log.d(TAG, Long.toString(System.currentTimeMillis()));
		//TO/DO - Use the AlarmManager set method to set the Alarm

		Long delay = 1000L;
		mAlarmManager.set(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + delay,
				pendingIntent);

		Intent intent = new Intent(getApplicationContext(), TwitterActivity.class);
		intent.putExtra(TWEET_STRING, tweetText);
		startActivity(intent);
	}

	public void clear(View v) {
		mTweetTextView.setText("");
		mPreviewText.setText("");
	}
}
