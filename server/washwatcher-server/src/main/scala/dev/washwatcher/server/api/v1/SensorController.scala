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

  post("/api/v1/sensors") { reads: Seq[SensorRead] =>
    reads.foreach { read =>
      SensorCache.add(read)
    }

    response.ok
  }

  get("/api/v1/sensor/magnitudes") { request: Request =>
    val everyMS = request.getLongParam("everySeconds", 0) * 1000

    val magnitudes = SensorCache.all()
      .foldLeft((0L, Seq.empty[SensorRead])) { case ((lastWhen, acc), read) =>
        if (lastWhen + everyMS > read.when) (lastWhen, acc)
        else (read.when, acc :+ read)
      }._2
      .map(read => Magnitude(read.when, magnitude(read)))
    response.ok(magnitudes)
  }

  def magnitude(read: SensorRead): Double = new Vector3D(read.x, read.y, read.z).getNorm
}

case class Magnitude(when: Long, magnitude: Double)
