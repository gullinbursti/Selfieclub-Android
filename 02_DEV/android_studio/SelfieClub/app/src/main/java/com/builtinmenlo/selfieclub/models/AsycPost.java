package com.builtinmenlo.selfieclub.models;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Leonardo on 6/4/14.
 */
public class AsycPost extends AsyncTask<String,String,String> {
    private AsycPostProtocol asycPostProtocol;
    private HashMap<String,String> mData;

    public AsycPost(AsycPostProtocol asycPostProtocol){
        this.asycPostProtocol = asycPostProtocol;
    }

    @Override
    protected String doInBackground(String... params){
        InputStream is;
        String resultString ="";
        HttpClient httpClient = new DefaultHttpClient();

        try{
            HttpPost request = new HttpPost(params[0]);

            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = this.getmData().keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, getmData().get(key)));
            }
            request.setEntity(new UrlEncodedFormEntity(nameValuePair,"UTF-8"));

            HttpResponse httpResponse = httpClient.execute(request);
            StatusLine statusLine = httpResponse.getStatusLine();
            if(statusLine.getStatusCode()== HttpURLConnection.HTTP_OK){
                is = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                return  sb.toString();

            }
            else {
                this.asycPostProtocol.didReceiveError("Error doing the request.HTTP status:" + statusLine.getStatusCode());
            }

        }
        catch (Exception e){
            this.asycPostProtocol.didReceiveError("Exception :" + e.toString());
            resultString = null;
        }
        return resultString;
    }

    @Override
    protected void onPostExecute(String result){
        try{
            JSONArray jsonArray = new JSONArray(result);
            this.asycPostProtocol.didReceiveData(jsonArray);
        }
        catch (JSONException e){
            this.asycPostProtocol.didReceiveError("Error parsing Json:"+result);
        }

    }

    public HashMap<String, String> getmData() {
        return mData;
    }

    public void setmData(HashMap<String, String> mData) {
        this.mData = mData;
    }
}
