package dev.washwatcher.server.controller

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class HelloWorldController extends Controller {

  get("/api/v1/hi") { request: Request =>
    info("hi")
    "Hello " + request.params.getOrElse("name", "World") + "!"
  }
}
