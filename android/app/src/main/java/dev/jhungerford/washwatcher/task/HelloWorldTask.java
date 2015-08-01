package dev.jhungerford.washwatcher.task;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HelloWorldTask extends AsyncTask<Void, Void, Void> {

    public static final String TAG = "HelloWorldTask";

    private HttpClient httpClient = new DefaultHttpClient();

    @Override
    protected Void doInBackground(Void... params) {
        Log.i(TAG, "Saying hi");

        try {
            HttpGet get = new HttpGet("http://192.168.0.102:8888/api/v1/hi");

            HttpResponse response = httpClient.execute(get);
            String message = EntityUtils.toString(response.getEntity());

            Log.i(TAG, "Got response: " + message);
        } catch (IOException e) {
            Log.e(TAG, "Error saying hi", e);
        } catch (Throwable t) {
            Log.e(TAG, "API is super-grumpy", t);
        }

        return null;
    }
}
