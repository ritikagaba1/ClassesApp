package com.example.classesapp.Server;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class HttpConnection {
  static InputStream is = null;
  static JSONObject jObj = null;
  static String json = "";
  // constructor
  public HttpConnection() {
  }
  public JSONObject getJSONFromUrl(String url, JSONObject jsonObject ) {
    // Making HTTP request
    try {
      // defaultHttpClient
      DefaultHttpClient httpClient = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(url);
      String json = "";
  
     
      // 4. convert JSONObject to JSON to String
      json = jsonObject.toString();

      // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
      // ObjectMapper mapper = new ObjectMapper();
      // json = mapper.writeValueAsString(person); 

      // 5. set json to StringEntity
      StringEntity se = new StringEntity(json);
      Log.i("hey", json+"    "+url);
      // 6. set httpPost Entity
      httpPost.setEntity(se);

      // 7. Set some headers to inform server about the type of the content   
      httpPost.setHeader("Accept", "application/json");
      httpPost.setHeader("Content-type", "application/json");

      // 8. Execute POST request to the given URL
      HttpResponse httpResponse = httpClient.execute(httpPost);

      // 9. receive response as inputStream
      is = httpResponse.getEntity().getContent();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
    	
      BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      is.close();
      json = sb.toString();
      Log.e("Server Response", json);
    } catch (Exception e) {
      Log.e("Buffer Error", "Error converting result " + e.toString());
    }
    // try parse the string to a JSON object
    try {
      jObj = new JSONObject(json);
    } catch (JSONException e) {
      Log.e("JSON Parser", "Error parsing data " + e.toString());
    }
    // return JSON String
    return jObj;
  }

}