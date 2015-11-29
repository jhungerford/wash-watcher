package dev.jhungerford.washwatcher.model;

import android.provider.Settings.Secure;

/**
 * Created by John on 8/1/2015.
 */
public class SensorData {

    public final long now = System.currentTimeMillis();
    public final float x;
    public final float y;
    public final float z;

    public SensorData(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toJson() {
        return String.format("{\"sensor\":{\"timestamp\":%d,\"device\":\"%s\",\"x\":%f,\"y\":%f,\"z\":%f}}", now, "android", x, y, z);
    }
}
