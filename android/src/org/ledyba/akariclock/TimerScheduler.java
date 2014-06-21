package  org.ledyba.akariclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimerScheduler {
	private final String TAG="TimerScheduler";
	private Context context;
	private final AlarmManager alarm;
	
	public TimerScheduler(final Context context) {
		this.context = context;
		this.alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	public <T> void set(Class<T> launch_service, long duration_time, int service_id) {
		final Intent intent = new Intent(context.getApplicationContext(), launch_service);
		Log.d(TAG,"Sending");
		final PendingIntent act1 = PendingIntent.getActivity(context, service_id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		final PendingIntent act2 = PendingIntent.getActivity(context, service_id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarm.cancel(act1);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, duration_time, AlarmManager.INTERVAL_DAY, act2);
	}
	
	public void clearAll()
	{
	}

	/*
	 * 起動したい時刻(hour:minuite)を指定するバージョン
	 * 指定した時刻で毎日起動する
	 */
	public <T> void setByTime(Class<T> launch_service, int hour, int minuite, int service_id) {
		final Calendar cal_target = Calendar.getInstance();
		cal_target.set(Calendar.HOUR_OF_DAY, hour);
		cal_target.set(Calendar.MINUTE, minuite);
		cal_target.set(Calendar.SECOND, 0);
		cal_target.set(Calendar.MILLISECOND, 0);
		Calendar cal_now = Calendar.getInstance();
		final long target_ms = cal_target.getTimeInMillis();
		final long now_ms = cal_now.getTimeInMillis();

		//今日ならそのまま指定
		if (target_ms >= now_ms) {
			Log.d(TAG, "Today");
			set(launch_service, target_ms, service_id);
		//過ぎていたら明日の同時刻を指定
		} else {
			Log.d(TAG, "Tommorow");
			cal_target.add(Calendar.DAY_OF_MONTH, 1);
			final long target_ms2 = cal_target.getTimeInMillis();
			set(launch_service, target_ms2, service_id);
		}

	}

	/*
	 * キャンセル用
	 */
	public <T> void cancel(Class<T> launch_service, long wake_time, int service_id) {
		final Intent intent = new Intent(context.getApplicationContext(), launch_service);
		final PendingIntent action = PendingIntent.getActivity(context, service_id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarm.cancel(action);
	}
}
