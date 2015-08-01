package dev.jhungerford.washwatcher.model;

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
        return String.format("{\"when\":%d,\"x\":%f,\"y\":%f,\"z\":%f}", now, x, y, z);
    }
}
