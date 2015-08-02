package dev.washwatcher.server.sensor

import java.util.concurrent.TimeUnit
import scala.collection.JavaConversions._

import com.google.common.cache.{Cache, CacheBuilder}

object SensorCache {
  val cache: Cache[java.lang.Long, SensorRead] = CacheBuilder.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build[java.lang.Long, SensorRead]()

  def add(sensorRead: SensorRead): Unit = {
    cache.put(sensorRead.when, sensorRead)
  }

  def allLaterThan(timestamp: Long): Seq[SensorRead] = {
    cache.asMap()
      .filter(_._1 > timestamp)
      .toSeq
      .sortBy(_._1)
      .map(_._2)
  }

  def all(): Seq[SensorRead] = {
    cache.asMap()
      .toSeq
      .sortBy(_._1)
      .map(_._2)
  }
}
