package dev.washwatcher.server.sensor

case class SensorRead(when: Long, x: Float, y: Float, z: Float)
