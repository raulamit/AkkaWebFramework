import java.io._
import java.net.ServerSocket

import akka.actor.{ActorRef, ActorSystem, Props}
import server.{HttpServer, Request, Response}
//import server.{FileResponse, HttpServer, Response, TextResponse}

/**
  * Created by araul on 12/3/17.
  */
object MyActorWebserver {

  class ActorWebserver(
                         val config: Config,
                         val routes: PartialFunction[Request, Unit]) {

    private var started = false
    private var stopped = false
    private val serverSocket = new ServerSocket(this.config.port)

    def start(): Unit = if (!started) {
      started = true
      val system: ActorSystem = ActorSystem("helloAkka")
      val writer: ActorRef = system.actorOf(Writer.props,"writer")
      val reader: ActorRef = system.actorOf(Reader.props(writer), "Reader")

      println("started")
      try
          while (!stopped) {
            val socket = serverSocket.accept()
            println(s"open connection ${socket.hashCode()}")
              reader ! socket
            println(s"close connection ${socket.hashCode()}")
          }
      finally
        serverSocket.close()
      println("stopped")
    }

    def stop(): Unit = stopped = true
  }

  def main(args: Array[String]): Unit = {
    val config = new Config()
    val routes =  Routes({
      case Request(GET("/hello")) => println("actor hello")
      case Request(POST("/post", body)) => body
    })
    val webserver = new ActorWebserver(config, routes)
    webserver.start()
    val num = 1000
    var i=0
    while(i < num) {
      i+=1;
    }
    webserver.stop()
  }
}
