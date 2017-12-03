import java.io._
import java.net.ServerSocket

import akka.actor.Actor
import server.ResponseInfo

/**
  * Created by rutpatel on 12/2/17.
  */


case class Start()

case class Stop(port:Int,host:String)

case class ReadRequest()

case class TextResponse(body: String,status:String,header:Map[String,String])

class WebServer(log: String=>Unit,port:Int,host:String) extends Actor  {

  var serverSocket: ServerSocket

  case class Request private (method: String, path: String, headers: Map[String, String])

  private var isStarted=false;

  def receive() = {

    case Start() => {
      if (!isStarted) {
        serverSocket = new ServerSocket(port)
        isStarted = true
        val socket = serverSocket.accept()
        log("started")
        try
            while (isStarted) {
              val socket = serverSocket.accept()
              log(s"open connection ${socket.hashCode()}")
              try {
                //TODO take buffered reader and send message to this server to read request
              } finally
                socket.close()
              log(s"close connection ${socket.hashCode()}")
            }
        finally
          serverSocket.close()
        log("stopped")
      }
    }

    case ReadRequest() => {
      val serverSocket = new ServerSocket(port)
      val socket = serverSocket.accept()
      val out = socket.getOutputStream()
      val in:BufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))

      val Array(method, path, _*) = input.readLine().split(" ")
      log(method + " " + path)
      var headers = Map[String, String]()
      var line = input.readLine()
      while (line != null && line.nonEmpty) {
        log(line)
        val Array(header, value, _*) = line.split(": ")
        headers += (header -> value)
        line = input.readLine()
      }
      log("")
      Request(method, path, headers)
      //TODO sends message to appropriate request handler actor to process the request.
    }

    case TextResponse(body: String,status:String,header:Map[String,String]) => {
      val serverSocket = new ServerSocket(port)
      val socket = serverSocket.accept()
      val out = socket.getOutputStream()
      val res = status + "\r\n" + header.map { case (key, value) => s"$key: $value" }.mkString("\r\n") + "\r\n\r\n" + body
      out.write(res.getBytes())
    }
  }
}

