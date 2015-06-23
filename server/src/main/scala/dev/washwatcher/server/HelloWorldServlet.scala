package dev.washwatcher.server

import org.scalatra.ScalatraServlet

class HelloWorldServlet extends ScalatraServlet {

  get("/hi") {
    "Hello Scalatra."
  }

}
