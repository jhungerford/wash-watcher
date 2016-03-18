package dev.jhungerford.washwatcher;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import dev.jhungerford.washwatcher.model.SensorData;


public class WashWatcherActivity extends Activity implements SensorEventListener {

    public static final String EXTRA_MESSAGE = "dev.jhungerford.washwatcher.MESSAGE";
    public static final String TAG = "WashWatcherActivity";

    private static final int SENSOR_QUEUE_SIZE = 10000;

    private SensorManager sensorManager;
    private Sensor accSensor;

    private boolean running = false;
    private ExecutorService sendExecutorService = Executors.newSingleThreadExecutor();
    private SensorSender sensorSender;

    private BlockingQueue<SensorData> sensorQueue = new LinkedBlockingQueue<>(SENSOR_QUEUE_SIZE);

    private TextView xTextView;
    private TextView yTextView;
    private TextView zTextView;
    private TextView ipTextView;
    private TextView portTextView;
    private Button startStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_watcher);

        xTextView = (TextView) findViewById(R.id.acc_x);
        yTextView = (TextView) findViewById(R.id.acc_y);
        zTextView = (TextView) findViewById(R.id.acc_z);

        ipTextView = (TextView) findViewById(R.id.ip_input);
        portTextView = (TextView) findViewById(R.id.port_input);

        startStopButton = (Button) findViewById(R.id.start_stop_button);
        startStopButton.setOnClickListener(startStopButtonListener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private Button.OnClickListener startStopButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (running) {
                sensorSender.stop();
                running = false;
                startStopButton.setText(R.string.start_label);
                xTextView.setText("X: ");
                yTextView.setText("Y: ");
                zTextView.setText("Z: ");

            } else {
                try {
                    sensorSender = new SensorSender(sensorQueue, ipTextView.getText(), portTextView.getText());
                    sendExecutorService.submit(sensorSender);
                } catch (IOException e) {
                    Log.e(TAG, "Error creating sensor sender", e);
                }

                running = true;
                startStopButton.setText(R.string.stop_label);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            SensorData data = new SensorData(x, y, z);
            try {
                sensorQueue.add(data);
            } catch (IllegalStateException e) {
                Log.d(TAG, "Discarded sensor read - queue is full");
            }

            xTextView.setText(String.format("X: %3.2f", x));
            yTextView.setText(String.format("Y: %3.2f", y));
            zTextView.setText(String.format("Z: %3.2f", z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
