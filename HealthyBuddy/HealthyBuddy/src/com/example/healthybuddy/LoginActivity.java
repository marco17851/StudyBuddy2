package com.example.healthybuddy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import android.os.AsyncTask;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;



public class LoginActivity extends Activity {

    
    final Token EMPTY_TOKEN = null;
    int timesThrough = 0;
    Verifier v;
    String authURL = "";
    static OAuthService s;
    Context mContext = this;
    Token accToken;
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        
        final  WebView webview = (WebView) findViewById(R.id.webview1);

        //set up service and get request token as seen on scribe website 
        //https://github.com/fernandezpablo85/scribe-java/wiki/Getting-Started
        s = new ServiceBuilder()
							        	.provider(updefinition.class)
							        	.apiKey(Globals.APIKEY)
							        	.apiSecret(Globals.APISECRET)
							        	.callback("http://jesusrmoreno.com/StudyBuddy")
							        	.build();

        authURL = s.getAuthorizationUrl(EMPTY_TOKEN);
        authURL += "&scope=basic_read+sleep_read+cardiac_read+move_read+generic_event_write";

        Intent intent = new Intent(mContext, MainActivity.class);
        Bundle bundle = new Bundle();
        
        //attach WebViewClient to intercept the callback url
        
        //If access_key != null skip this. Write this method.
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	
            	Uri uri = Uri.parse(url);
            	
            	
            	if (timesThrough > 0) {
            		webview.setVisibility(View.GONE);
            		String verifier = uri.getQueryParameter("code");
            		v = new Verifier(verifier);
            		new getAccToken().execute();
            		startActivity(new Intent(mContext, MainActivity.class));
            		
            	}
            	
            	timesThrough++;
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        
        //send user to authorization page
        webview.loadUrl(authURL);
        
        
        
    }
    
    
    
    
    
    private class getAccToken extends AsyncTask<Void, Void, Void> {
    	
    	private Token accessToken;

    	
		protected Void doInBackground(Void... params) {
    		accessToken = s.getAccessToken(EMPTY_TOKEN, v);
    		accToken = accessToken;
    		                 
    	    StringBuilder builder = new StringBuilder();

    		OAuthRequest request = new OAuthRequest(Verb.GET, Globals.PROTECTED_RESOURCE_URL + "users/@me");
    		s.signRequest(accToken, request);
    	    request.addHeader("Authorization", "Bearer " + accToken.getToken());
    	    Response response = request.send();
    	 
			try {
				InputStream content;
				String responseBody = response.getBody();
				content = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
				 BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		            String line;
		            while ((line = reader.readLine()) != null) {
		              builder.append(line);
		            }
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			System.out.println(builder.toString());
           
			
    	    try {
    		    FileOutputStream fos = openFileOutput("userInfo", Context.MODE_PRIVATE);
    		    fos.write(response.getBody().getBytes());
    		    fos.close();
    		} catch (Exception e) {
    		    e.printStackTrace();
    		}
    	    
    	    request = new OAuthRequest(Verb.GET, Globals.PROTECTED_RESOURCE_URL + "users/@me/workouts");
    		s.signRequest(accToken, request);
    	    request.addHeader("Authorization", "Bearer " + accToken.getToken());
    	    
    	    response = request.send();
    	    

    	    
    	    
    	    try {
    		    FileOutputStream fos = openFileOutput("userWorkouts", Context.MODE_PRIVATE);
    		    fos.write(response.getBody().getBytes());
    		    fos.close();
    		} catch (Exception e) {
    		    e.printStackTrace();
    		}
    	    
    	    request = new OAuthRequest(Verb.GET, Globals.PROTECTED_RESOURCE_URL + "users/@me/sleep");
    		s.signRequest(accToken, request);
    	    request.addHeader("Authorization", "Bearer " + accToken.getToken());
    	    
    	    response = request.send();
    	    
    	    
    	    try {
    		    FileOutputStream fos = openFileOutput("userSleep", Context.MODE_PRIVATE);
    		    fos.write(response.getBody().getBytes());
    		    fos.close();
    		} catch (Exception e) {
    		    e.printStackTrace();
    		}
    	    
    	    
    		try {
    		    FileOutputStream fos = openFileOutput("DayTwentyTwoFile", Context.MODE_PRIVATE);
    		    fos.write(accessToken.getToken().getBytes());
    		    fos.close();
    		} catch (Exception e) {
    		    e.printStackTrace();
    		}
			return null;
		}
    }
}