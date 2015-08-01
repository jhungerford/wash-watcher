package dev.washwatcher.server.api.v1

import com.twitter.finatra.http.Controller
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D

class SensorController extends Controller {

  post("/api/v1/sensor") { read: SensorRead =>
    val vector = new Vector3D(read.x, read.y, read.z)
    info(s"${read.when} - ${vector.getNorm}")

    response.ok
  }
}

case class SensorRead(when: Long, x: Float, y: Float, z: Float)
