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



public class ActivityMonitor extends AsyncTask {
	

private Boolean active;
private Context context;
private ArrayList<String> bannedApps;

public ActivityMonitor(ArrayList<String> blackList, Context parentcontext){

    active = false;
    bannedApps = blackList;
    context = parentcontext;
}


public void startMonitoring(){
	active = true;
}

public void stopMonitoring(){
	active = false;
}

private void monitorApps() {

	ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
	final List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();


	for(RunningAppProcessInfo app: runningApps){
		for(String bannedapp: bannedApps){
			if(app.processName.equalsIgnoreCase(bannedapp)){
				android.os.Process.killProcess(app.pid);
			}
		}
	}

}
@Override
protected Object doInBackground(Object... arg0) {
	if(active == true){
		monitorApps();
	}
	try {
		Thread.sleep(1000);
	}
	catch (InterruptedException e) {
		e.printStackTrace();
	}
	return null;
}
}
