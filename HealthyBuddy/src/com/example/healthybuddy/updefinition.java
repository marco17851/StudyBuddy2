package com.example.healthybuddy;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.*;
import org.scribe.oauth.*;
import org.scribe.utils.OAuthEncoder;

public class updefinition extends DefaultApi20 {
	private static final String AUTHORIZE_URL = "https://jawbone.com/auth/oauth2/auth/?client_id=%s&redirect_uri=%s&response_type=code";
	private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
	
	@Override
	public AccessTokenExtractor getAccessTokenExtractor(){
		return new JsonTokenExtractor();
	}
	
	@Override
	public String getAccessTokenEndpoint() {
		return "https://jawbone.com/auth/oauth2/token?grant_type=authorization_code";
	}
	
	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		if (config.hasScope()) {
			  return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
	    } else {
	      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
	    }
	}
}