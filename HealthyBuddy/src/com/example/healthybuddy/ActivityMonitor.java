package com.example.healthybuddy;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class ActivityMonitor extends Service {

	private Boolean active;
	private Context context;
	private ArrayList<String> blackList;
	private ArrayList<String> bannedApps;
	private Monitoring monitor;

	public ActivityMonitor() {
		active = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO do something useful
		context = this.getApplicationContext();
		//blackList = intent.getExtras().getStringArrayList("BL");
		startMonitoring();
		return Service.START_NOT_STICKY;
	}

	public void startMonitoring() {
		Log.d(Globals.TAG, "start");
		active = true;
		monitor = new Monitoring();
		monitor.execute();
	}

	public void stopMonitoring() {
		active = false;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private class Monitoring extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			if (active == true) {
				Log.d(Globals.TAG, "Monitoring...");
				monitorApps();
			}
		}
		
		private void monitorApps() {

			ActivityManager am = (ActivityManager) context
					.getSystemService(Activity.ACTIVITY_SERVICE);
			final List<RunningAppProcessInfo> runningApps = am
					.getRunningAppProcesses();

			for (RunningAppProcessInfo app : runningApps) {
				for (String bannedapp : bannedApps) {
					if (app.processName.equalsIgnoreCase(bannedapp)) {
						android.os.Process.killProcess(app.pid);
					}
				}
			}

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
