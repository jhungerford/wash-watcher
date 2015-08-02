package dev.washwatcher.server

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class WebController extends Controller {
  get("/:*") { request: Request =>
    response.ok.fileOrIndex("web/" + request.params("*"), "web/index.html")
  }
}
