package dev.washwatcher.server.api.v1

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import dev.washwatcher.server.sensor.{SensorCache, SensorRead}
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D

class SensorController extends Controller {

  post("/api/v1/sensor") { read: SensorRead =>
    SensorCache.add(read)
    response.ok
  }

  get("/api/v1/sensor/magnitudes") { request: Request =>
    val magnitudes = SensorCache.all().map(read => Magnitude(read.when, magnitude(read)))
    response.ok(magnitudes)
  }

  def magnitude(read: SensorRead): Double = new Vector3D(read.x, read.y, read.z).getNorm
}

case class Magnitude(when: Long, magnitude: Double)
