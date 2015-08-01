package dev.jhungerford.washwatcher;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import dev.jhungerford.washwatcher.model.SensorData;
import dev.jhungerford.washwatcher.task.SendSensorDataTask;


public class MyActivity extends Activity implements SensorEventListener {

    public static final String EXTRA_MESSAGE = "dev.jhungerford.washwatcher.MESSAGE";
    public static final String TAG = "MyActivity";

    private TextView xTextView;
    private TextView yTextView;
    private TextView zTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        xTextView = (TextView) findViewById(R.id.acc_x);
        yTextView = (TextView) findViewById(R.id.acc_y);
        zTextView = (TextView) findViewById(R.id.acc_z);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        new SendSensorDataTask().execute(new SensorData(x, y, z));

        xTextView.setText(Float.toString(x));
        yTextView.setText(Float.toString(y));
        zTextView.setText(Float.toString(z));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
