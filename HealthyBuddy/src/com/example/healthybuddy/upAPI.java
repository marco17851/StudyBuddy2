package com.example.healthybuddy;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import com.androauth.oauth.OAuthService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class upAPI {
    static org.scribe.oauth.OAuthService s;

	private String aToken;
	private Token objAToken;
	
	public upAPI(Context mContext, Token s) {
		try {
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
		            mContext.openFileInput("DayTwentyTwoFile")));
		    String inputString;
		    StringBuffer stringBuffer = new StringBuffer();                
		    while ((inputString = inputReader.readLine()) != null) {
		        stringBuffer.append(inputString + "\n");
		    }
		    aToken = stringBuffer.toString();
		} catch (IOException e) {
		    e.printStackTrace();	
		}
		
		this.getUserInfo();
	}
	
	public void getUserInfo() {
		
		OAuthRequest request = new OAuthRequest(Verb.GET, Globals.PROTECTED_RESOURCE_URL + "users/@me");
	    
		s.signRequest(objAToken, request);
	    request.addHeader("Authorization", "Bearer " + aToken);
	    System.out.println(request.getHeaders());
	    Response response = request.send();
	    
	    System.out.println(response.getCode());
	}
}
