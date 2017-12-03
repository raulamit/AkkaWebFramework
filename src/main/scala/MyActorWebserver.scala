import java.io._
import java.net.ServerSocket

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

      println("started")
      try
          while (!stopped) {
            val socket = serverSocket.accept()
            println(s"open connection ${socket.hashCode()}")
            try {
              val request = readRequestFrom(new BufferedReader(new InputStreamReader(socket.getInputStream())))
//              Request("request.method","request.url")
              writeResponse(request, socket.getOutputStream())
            } finally
              socket.close()
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
      println("")
      Request(method, path)
    }

    private def writeResponse(request: Request, output: OutputStream) =
      try
        this.routes(Request("GET", "/"))
//        handler(request, this).writeTo(output)
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
      case Request("GET", "/hell") => println("actor hel")
      case Request("GET", _) => println("helloWorld")
      //TextResponse("helloworld")
      //case ("GET", "/img") => FileResponse("123.png", "image/png").and(_.withHeader("X-x", "O-o"))
      //      case ("GET", s) => Response.guessType(s)
    })
//    { server => new RequestHandler(server, routes) }
      routes(Request("GET", "sdsfd"))
//    val webserver = new ActorWebserver(config, routes)
//    webserver.start()
  }
}
