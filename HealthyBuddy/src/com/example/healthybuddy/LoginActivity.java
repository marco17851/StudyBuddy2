package com.example.healthybuddy;

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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class LoginActivity extends Activity {

    final static String APIKEY = "J44U6FeWoN4";
    final static String APISECRET = "4116fd78f42650d1be594bd0e70fc2d83de3c213";
    final Token EMPTY_TOKEN = null;
    int timesThrough = 0;
    Verifier v;
    String authURL = "";
    OAuthService s;
    Context mContext = this;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        
        final  WebView webview = (WebView) findViewById(R.id.webview1);

        //set up service and get request token as seen on scribe website 
        //https://github.com/fernandezpablo85/scribe-java/wiki/Getting-Started
        s = new ServiceBuilder()
							        	.provider(updefinition.class)
							        	.apiKey(APIKEY)
							        	.apiSecret(APISECRET)
							        	.callback("http://jesusrmoreno.com/StudyBuddy")
							        	.build();

        authURL = s.getAuthorizationUrl(EMPTY_TOKEN);
        authURL += "&scope=basic_read+sleep_read+cardiac_read+move_read+generic_event_write";

        //attach WebViewClient to intercept the callback url
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	Log.d(Globals.TAG, "In shouldOverride");
            	Uri uri = Uri.parse(url);
            	
            	
            	if (timesThrough > 0) {
            		webview.setVisibility(View.GONE);
            		String verifier = uri.getQueryParameter("code");
            		v = new Verifier(verifier);
            		System.out.println(verifier);
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
    		Log.d(Globals.TAG, accessToken + "");
			return null;
		}
    }
}


/*
import java.util.Locale;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private static final String PROTECTED_RESOURCE_URL = "https://jawbone.com/nudge/api/";
	String authorizationUrl = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button logButt = (Button) findViewById(R.id.login_button);
		
		final Token EMPTY_TOKEN = null;
		
		final OAuthService service = new ServiceBuilder()
        .provider(updefinition.class)
        .apiKey("J44U6FeWoN4")
        .apiSecret("4116fd78f42650d1be594bd0e70fc2d83de3c213")
        .callback("http://jesusrmoreno.com/StudyBuddy")
        .build();
		
		authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
		authorizationUrl += "&scope=basic_read+sleep_read+cardiac_read+move_read+generic_event_write";
		
		logButt.setOnClickListener(new View.OnClickListener()
		  {
			  public void onClick(View v) {
				  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl)));
			  }
		  });
		
	}
	
	
	
}
//	
//	/**
//	 * The {@link android.support.v4.view.PagerAdapter} that will provide
//	 * fragments for each of the sections. We use a
//	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
//	 * will keep every loaded fragment in memory. If this becomes too memory
//	 * intensive, it may be best to switch to a
//	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
//	 */
//	SectionsPagerAdapter mSectionsPagerAdapter;
//
//	/**
//	 * The {@link ViewPager} that will host the section contents.
//	 */
//	ViewPager mViewPager;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		// Create the adapter that will return a fragment for each of the three
//		// primary sections of the app.
//		mSectionsPagerAdapter = new SectionsPagerAdapter(
//				getSupportFragmentManager());
//
//		// Set up the ViewPager with the sections adapter.
//		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mViewPager.setAdapter(mSectionsPagerAdapter);
//		mViewPager.setCurrentItem(1);
//		mViewPager.setPageTransformer(true, new DepthPageTransformer());
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	/**
//	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//	 * one of the sections/tabs/pages.
//	 */
//	public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//		public SectionsPagerAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int position) 
//		{
//			Fragment fragment = new Fragment();
//			switch (position)
//			{
//			case 0:
//				fragment = new AppFragment();
//				return fragment;
//			case 1:
//				fragment = new MainFragment();
//				return fragment;
//			case 2:
//				fragment = new AboutFragment();
//			default:
//				return fragment;
//			}
//		}
//
//		@Override
//		public int getCount() {
//			// Show 3 total pages.
//			return 3;
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			Locale l = Locale.getDefault();
//			switch (position) {
//			case 0:
//				return getString(R.string.title_apps_to_lock).toUpperCase(l);
//			case 1:
//				return getString(R.string.title_study_buddy).toUpperCase(l);
//			case 2:
//				return getString(R.string.title_about).toUpperCase(l);
//			}
//			return null;
//		}
//	}
//
//}

