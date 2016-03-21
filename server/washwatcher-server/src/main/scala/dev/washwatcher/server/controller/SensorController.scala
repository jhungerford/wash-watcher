package dev.washwatcher.server.controller

import com.twitter.finatra.http.Controller
import com.twitter.finatra.request.QueryParam
import dev.washwatcher.server.model.{Magnitude, SensorRead}
import dev.washwatcher.server.sensor.SensorCache
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

  get("/api/v1/sensor/magnitudes") { req: MagnitudesRequest =>

    val magnitudes = (req.fromMS+req.everyMS to req.toMS by req.everyMS) map { end =>
      val reads = SensorCache.between(end - req.everyMS, end)

      val value = reads.isEmpty match {
        case true => 0.0
        case false =>
          reads.foldLeft(0.0) { (sum, read) =>
            sum + new Vector3D(read.x, read.y, read.z).getNorm
          } / reads.size
      }

      Magnitude(end, value)
    }

    response.ok(magnitudes)
  }

}

case class MagnitudesRequest(@QueryParam everyMS: Long, @QueryParam fromMS: Long, @QueryParam toMS: Long)
