package dev.jhungerford.washwatcher.task;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import dev.jhungerford.washwatcher.model.SensorData;

public class SendSensorDataTask extends AsyncTask<SensorData, Void, Void> {

    public static final String TAG = "SendSensorDataTask";

    private HttpClient httpClient = new DefaultHttpClient();

    @Override
    protected Void doInBackground(SensorData... sensorData) {
        try {
            for (SensorData data : sensorData) {
                HttpPost post = new HttpPost("http://192.168.0.106:4000/api/sensors");
                post.addHeader("Content-Type", "application/json");
                post.setEntity(new StringEntity(data.toJson()));

                httpClient.execute(post); // Ignore the response.
            }

        } catch (IOException e) {
            Log.e(TAG, "Error sending sensor data", e);
        } catch (Throwable t) {
            Log.e(TAG, "Error posting data", t);
        }

        return null;
    }
}
