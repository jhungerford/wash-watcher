package dev.jhungerford.washwatcher;

import android.util.Log;

import com.google.common.collect.Queues;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntityHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtilsHC4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import dev.jhungerford.washwatcher.model.SensorData;

public class SensorSender implements Runnable {

    public static final String TAG = "SensorSender";

    public static final int MAX_BATCH_SIZE = 100;
    public static final int MAX_WAIT_MS = 1000;

    private final BlockingQueue<SensorData> sensorQueue;
    private final String url;
    private final CloseableHttpClient httpClient;

    private AtomicBoolean running;

    public SensorSender(BlockingQueue<SensorData> sensorQueue, CharSequence ip, CharSequence port) throws IOException {
        this.running = new AtomicBoolean(true);
        this.sensorQueue = sensorQueue;

        url = "http://" + ip + ":" + port + "/api/v1/sensors";
        httpClient = HttpClients.createDefault();
        // TODO: keepalive connection manager: https://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html
    }

    @Override
    public void run() {
        while (running.get()) {
            List<SensorData> sendData = new ArrayList<>(MAX_BATCH_SIZE);
            Queues.drainUninterruptibly(sensorQueue, sendData, MAX_BATCH_SIZE, MAX_WAIT_MS, TimeUnit.MILLISECONDS);

            if (! sendData.isEmpty()) {
                Log.i(TAG, "Sending " + sendData.size() + " sensor reads.  Queue size: " + sensorQueue.size());
                transmitData(sendData);
            }
        }
    }

    private void transmitData(List<SensorData> sendData) {
        CloseableHttpResponse response = null;

        try {
            HttpPostHC4 post = new HttpPostHC4(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntityHC4(toJson(sendData), ContentType.APPLICATION_JSON));

            response = httpClient.execute(post);

            Log.i(TAG, "Got " + response.getStatusLine() + " response from server");
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Log.i(TAG, EntityUtilsHC4.toString(response.getEntity()));
            }

        } catch (IOException e) {
            Log.e(TAG, "Error sending sensor data", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing response", e);
                }
            }
        }
    }

    private String toJson(List<SensorData> sendData) {
        StringBuilder bldr = new StringBuilder();

        bldr.append('[');

        boolean first = true;
        for (SensorData data : sendData) {
            if (first) {
                first = false;
            } else {
                bldr.append(',');
            }

            bldr.append(data.toJson());
        }

        bldr.append(']');

        return bldr.toString();
    }


    public void stop() {
        running.set(false);

        try {
            httpClient.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing http client", e);
        }
    }
}
