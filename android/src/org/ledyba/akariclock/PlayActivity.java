package org.ledyba.akariclock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;

public class PlayActivity extends Activity implements OnPreparedListener {
	private final String TAG="PlayActivity";

	private MediaPlayer mp;
	private PowerManager.WakeLock wakeLock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BarReceiver");
		wakeLock.acquire();
		setContentView(R.layout.activity_main);
		Log.d(TAG,"Start");
		this.mp = new MediaPlayer();
		mp.reset();
		mp.setOnPreparedListener(this);
		final TextView t = new TextView(this);
		t.setText("Playing");
		setContentView(t);
	}

	@Override
	protected void onPause() {
		super.onPause();
		wakeLock.release();
	}
	
	private AssetFileDescriptor selectFile() throws IOException
	{
		final Calendar cal = Calendar.getInstance();
		final ArrayList<String> lst = new ArrayList<String>();
		for(String f : getAssets().list("")){
			if(f.endsWith("m4a") || f.endsWith("mp3")){
				lst.add(f);
			}
		}
		return getAssets().openFd(lst.get(cal.get(Calendar.DAY_OF_YEAR) % lst.size()));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		try {
			final AssetFileDescriptor fd = selectFile();
			mp.setLooping(true);
			mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
			mp.prepareAsync();
		} catch (IOException e) {
			Log.e(TAG, "Error on playing: ", e);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mp.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.reset();
		mp.release();
	}


}
