package dev.washwatcher.server

import javax.servlet.ServletContext

import org.scalatra.LifeCycle

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) = {
    context.mount(new HelloWorldServlet, "/washwatcher/api/v1/hello")
  }
}
