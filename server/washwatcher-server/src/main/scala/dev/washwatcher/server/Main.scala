package dev.washwatcher.server

import java.net.InetSocketAddress

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object Main {

  def main(args: Array[String]) {
    val context = new WebAppContext()
    context.setContextPath("/")
    context.setResourceBase("src/main/webapp")
    context.setInitParameter(ScalatraListener.LifeCycleKey, "dev.washwatcher.server.ScalatraBootstrap")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[HelloWorldServlet], "/")

    val server = new Server(new InetSocketAddress(8080))
    server.setHandler(context)
    server.start()

    server.join()
  }
}
