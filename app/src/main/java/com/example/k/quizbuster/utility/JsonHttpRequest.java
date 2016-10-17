package com.example.k.quizbuster.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by brian on 18/10/2016.
 */

public class JsonHttpRequest extends AsyncTask<Void, Void, Void> {

    private String url;
    private String content;
    private String errorMessage;

    private JsonHttpRequestCallback callback;

    public JsonHttpRequest(String url, JsonHttpRequestCallback callback){
        this.url = url;
        this.content = null;
        this.errorMessage = null;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {

        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(this.url);
            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("connection", "close");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "");
            }

            // Append Server Response To Content String
            content = sb.toString();
        }
        catch(Exception exception)
        {
            errorMessage = exception.getMessage();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(this.errorMessage == null){
            try {
                this.callback.onCompleted(new JSONObject(this.content));
            } catch (JSONException exception) {
                Log.e(this.getClass().getSimpleName(), "JSON Exception encountered", exception);
            }
        }else{
            this.callback.onError(this.errorMessage);
        }
    }
}
