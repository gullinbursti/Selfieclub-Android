package com.builtinmenlo.selfieclub.models;


import android.os.AsyncTask;


import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Created by Leonardo on 5/31/14.
 */

public class AsyncGet extends AsyncTask<String,String,String> {

    private AsyncGetProtocol scAsyncGetInterface;

    public AsyncGet(AsyncGetProtocol scAsyncGetInterface){
        this.scAsyncGetInterface = scAsyncGetInterface;
    }

    @Override
    protected String doInBackground(String... params){
        InputStream is;
        String resultString ="";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        try{
            URI website = new URI(params[0]);

            request.setURI(website);
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
                this.scAsyncGetInterface.didReceiveError("Error doing the request.HTTP status:"+statusLine.getStatusCode());
            }

        }
        catch (Exception e){
                this.scAsyncGetInterface.didReceiveError("Exception :"+e.toString());
                resultString = null;
        }

        return resultString;
    }

    @Override
    protected void onPostExecute(String result){
        try{
            JSONArray jsonArray = new JSONArray(result);
            this.scAsyncGetInterface.didReceiveData(jsonArray);
        }
        catch (JSONException e){
            this.scAsyncGetInterface.didReceiveError("Error parsing Json:"+result);
        }

    }


}
