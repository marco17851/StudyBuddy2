package com.example.healthybuddy;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class PrepareRequestTokenActivity extends Activity {
	
	final String TAG = getClass().getName();
	String authorizationUrl = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			System.setProperty("debug", "true");
			final Token EMPTY_TOKEN = null;
			  
			  final OAuthService service = new ServiceBuilder()
			                                .provider(updefinition.class)
			                                .apiKey("J44U6FeWoN4")
			                                .apiSecret("4116fd78f42650d1be594bd0e70fc2d83de3c213")
			                                .callback("http://jesusrmoreno.com/StudyBuddy")
			                                .build();
			  
			  String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
			  authorizationUrl += "&scope=basic_read+sleep_read+cardiac_read+move_read+generic_event_write";
		} catch (Exception e) {
			Log.e(TAG, "Error creating consumer / provider", e);
		}
		
	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl)));

	}
	
}
