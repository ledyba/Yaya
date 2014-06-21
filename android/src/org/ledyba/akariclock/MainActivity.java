package org.ledyba.akariclock;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private final String TAG="MainActivity";
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.JAPAN);

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG,"Start");
		final TimerScheduler ts = new TimerScheduler(this);
		ts.setByTime(PlayActivity.class, 22, 0 , 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
