package pl.indev.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RestService {
	private final String url;
	
	private DefaultHttpClient httpClient;
    
    public RestService(String url) {
    	this.url = url;
    	httpClient = new DefaultHttpClient();
    }
	
	public String invoke(String methodName, String type, String json, int userId) {
		String ret = "";        
		String requestUrl = url + methodName;
		
		if( userId != 0 ) {
			 requestUrl = requestUrl + "?id=" + userId;
		}
		
		HttpUriRequest request;		
		if( type.equals("PUT") ) {
			request = new HttpPut(requestUrl);
			try {
				((HttpPut)request).setEntity(new StringEntity(json));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			request = new HttpGet(requestUrl); 
		}
		
        request.setHeader("Content-type", "application/json");
        
        try {
			
        	HttpResponse response = httpClient.execute(request);
			ret = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}        
		
		return ret;
	}
}
