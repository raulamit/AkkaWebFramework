

import java.io.{OutputStream}
import java.net.Socket

import akka.actor.{Actor, ActorLogging, ActorRef, Props}


/**
  * Created by araul on 12/3/17.
  */
object Writer {
  def props: Props = Props[Writer]
}

trait Response {
  def info: ResponseInfo
  def writeTo(output: OutputStream): Unit
  def and(f: ResponseInfo => ResponseInfo): Response
}

case class ResponseInfo(status: String = "HTTP/1.1 200 OK",
                        headers: Map[String, String] = Map("Content-Type" -> "text/html", "Connection" -> "close")) {
  def withStatus(value: String): ResponseInfo = copy(status = value)

  def withHeader(header: String, value: String): ResponseInfo = copy(headers = headers.updated(header, value))

  def withContentType(value: String): ResponseInfo = withHeader("Content-Type", value)

  def serialized: String = status + "\r\n" + headers.map { case (key, value) => s"$key: $value" }.mkString("\r\n")
}

case class TextResponse(body: String, output: OutputStream,info: ResponseInfo = ResponseInfo()) extends Response {

  override def writeTo(output: OutputStream) = {
    val result = info.withHeader("Content-Length", body.length.toString).serialized + "\r\n\r\n" + body
    output.write(result.getBytes)
    output.flush()
  }
  override def and(f: (ResponseInfo) => ResponseInfo) = copy(info = f(info))
}
class Writer extends Actor with ActorLogging{

  var socket: Socket = _
  def receive ={
    case WhoToSend(socket, request,routes) => {
      this.socket=socket
      routes(request, this.self)
    }
    case WriteRaw(response) => {

      val output: OutputStream = socket.getOutputStream
      val info: ResponseInfo = ResponseInfo()
      val result = info.withHeader("Content-Length", response.length.toString).serialized + "\r\n\r\n" + response
      output.write(result.getBytes)
      output.flush()
      output.close()
      println(s"close connection ${socket.hashCode()}")
      socket.close()
    }
   }
}

case class WhoToSend(socket: Socket, request: Request, routes: PartialFunction[(Request, ActorRef), Unit])