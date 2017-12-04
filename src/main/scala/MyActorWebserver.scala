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
//            try {
//              val request = readRequestFrom(new BufferedReader(new InputStreamReader(socket.getInputStream())))
              reader ! socket
//              writeResponse(request, socket.getOutputStream())
//            } finally
//              socket.close()
            println(s"close connection ${socket.hashCode()}")
          }
      finally
        serverSocket.close()
      println("stopped")
    }
    private def readRequestFrom(input: BufferedReader) = {
      val Array(method, path, _*) = input.readLine().split(" ")
      println(method + " " + path)
      var headers = Map[String, String]()
      var line = input.readLine()
      while (line != null && line.nonEmpty) {
        println(line)
        val Array(header, value, _*) = line.split(": ")
        headers += (header -> value)
        line = input.readLine()
      }
      val i:Int = Integer.parseInt(headers("Content-Length"))
      val buffer = new Array[Char](i)
      input.read(buffer)
      println("")
      val request = method match {
        case "GET" => Request(GET(path))
        case "DELETE" => Request(DELETE(path))
        case "POST" => Request(POST(path, new String(buffer)))
        case "PUT" => Request(PUT(path, new String(buffer)))
      }
      
    }

    private def writeResponse(request: Request, output: OutputStream) =
      try
        this.routes(request)
      catch {
        case _: MatchError => Response.notFound.writeTo(output)
        case e: Throwable =>
          val sw = new StringWriter
          val pw = new PrintWriter(sw)
          e.printStackTrace(pw)
          println(sw.toString)
          Response.serverError.writeTo(output)
      }
    def stop(): Unit = stopped = true
  }

  def main(args: Array[String]): Unit = {
    val config = new Config()
    val routes =  Routes({
      case Request(GET("/hell")) => println("actor hel")
      case Request(POST("/post", body)) => println(body)
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
