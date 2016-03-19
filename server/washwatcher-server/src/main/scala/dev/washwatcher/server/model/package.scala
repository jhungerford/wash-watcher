package dev.washwatcher.server

package object model {
  case class SensorRead(when: Long, x: Float, y: Float, z: Float)

  case class Magnitude(when: Long, magnitude: Double)
}
