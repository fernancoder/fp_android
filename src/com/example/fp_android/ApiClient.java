package com.example.fp_android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

public final class ApiClient {
	
	  static final String TAG = "FPTest";

	  /*Some APIs for here*/
	  
	  static String query(final String terms) {
	    Log.i(TAG, "buscando (terms = " + terms + ")");
	    String serverUrl = "http://www.fernancoder.com:2031?action=query&terms=" + terms.replace(" ", "%20");
	    return get(serverUrl);
	  }
	 
	  /*Some APIs for there*/

	  /**
	   * Issue a POST request to the server.
	   *
	   * @param endpoint POST address.
	   * @param params request parameters.
	   *
	   * @throws IOException propagated from POST.
	   */
	  private static String get(String url){
	    String result = "";
	    InputStream content = null;
	    try {
	      URI uriCod = new URI(url);
	    		
	      HttpClient httpclient = new DefaultHttpClient();
	      HttpResponse response = httpclient.execute(new HttpGet(uriCod.toString()));
	      content = response.getEntity().getContent();
	      BufferedReader in = new BufferedReader(new InputStreamReader(content));
	      StringBuffer sb = new StringBuffer("");
	      String line = "";
	      String NL = System.getProperty("line.separator");
	      while ((line = in.readLine()) != null) {                    
	        sb.append(line + NL);
	      }
	      in.close();
	      result = sb.toString();
	      Log.i(TAG, result);
	    } catch (Exception e) {
	      Log.e("[GET REQUEST]", "Network exception", e);
	    }
	    return result;
	  }
	}
