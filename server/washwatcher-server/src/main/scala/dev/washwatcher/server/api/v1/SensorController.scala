package dev.washwatcher.server.api.v1

import com.twitter.finatra.http.Controller

class SensorController extends Controller {

  post("/api/v1/sensor") { read: SensorRead =>
    info(s"Got sensor data: $read")
    response.ok(s"Got read at ${read.when}")
  }
}

case class SensorRead(when: Long, x: Float, y: Float, z: Float)
