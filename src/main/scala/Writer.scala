

import java.io.{OutputStream, PrintWriter, StringWriter, Writer}
import java.net.Socket

import akka.actor.{Actor, ActorLogging, ActorRef, Props}


/**
  * Created by araul on 12/3/17.
  */
object Writer {
  def props: Props = Props[Writer]
//  final case class outWrite(req : Request)
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
    println("Inside TEXT response")
    output.write(result.getBytes)
    output.flush()
  }
  override def and(f: (ResponseInfo) => ResponseInfo) = copy(info = f(info))
}
class Writer extends Actor with ActorLogging{

//  info: ResponseInfo = ResponseInfo()

  def receive ={

    case (request, socket: Socket) => {
      request match
      {
        case Request(POST("/post", body)) => {
          println("It matched !!!!")
          println(body)

          TextResponse(body, socket.getOutputStream()).writeTo(socket.getOutputStream())
        }

        case Request(GET("/hello")) =>{
          TextResponse("OK", socket.getOutputStream()).writeTo(socket.getOutputStream())
        }

        case Request(PUT("/putTest", body)) => {
          TextResponse(body, socket.getOutputStream()).writeTo(socket.getOutputStream())
        }
      }
    }
   }

  }



