import java.io._
import java.net.ServerSocket

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.FromConfig
import server.{HttpServer, Request, Response}
//import server.{FileResponse, HttpServer, Response, TextResponse}

/**
  * Created by araul on 12/3/17.
  */
object MyActorWebserver {

  class ActorWebserver(
                         val config: Config,
                         val routes: PartialFunction[(Request, ActorRef), Unit]) {

    private var started = false
    private var stopped = false
    private val serverSocket = new ServerSocket(this.config.port)

    def start(): Unit = if (!started) {
      started = true
      val system: ActorSystem = ActorSystem("helloAkka")
      val writer: ActorRef = system.actorOf(MasterWriter.props(config.numberOfActors),"writer")
      //val writerMaster: ActorRef = system.actorOf(Props[WriterMaster], "router1")
      val reader: ActorRef = system.actorOf(MasterReader.props(writer, config.numberOfActors), "Reader")
      println("started")
      try
          while (!stopped) {
            val socket = serverSocket.accept()
            println(s"open connection ${socket.hashCode()}")
              reader ! (socket, this.routes)
//            println(s"close connection ${socket.hashCode()}")
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
      case (Request(GET("/hello")), responseWriter) => {
        println("befor get")
        while(true) {
          val i:Int=0
        }
        responseWriter ! WriteRaw("actor hello")
        println("after get")
      }
      case (Request(POST("/post", body)), responseWriter) => responseWriter ! WriteRaw(body)
    })
    val webserver = new ActorWebserver(config, routes)
    webserver.start()
    webserver.stop()
  }
}
