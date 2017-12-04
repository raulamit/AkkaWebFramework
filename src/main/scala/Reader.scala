/**
  * Created by araul on 12/3/17.
  */

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

object Reader {

  def props(writerRef: ActorRef): Props = Props(new Reader(writerRef))

  final case class WhoToGreet(who: String)

  case object Greet

}

class Reader(printerActor: ActorRef) extends Actor {

  import Reader._
  import Writer._

  var greeting = ""

  def receive: Request = {
    case socket: Socket => {
      val input = new BufferedReader(new InputStreamReader(socket.getInputStream()))
      val request: Request = requestFromServer(input)

    }
  }

  private def requestFromServer(input:BufferedReader) = {
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
    val i: Int = Integer.parseInt((headers("Content-Length")))
    val buffer = new Array[Char](i)
    input.read(buffer)
    println("")
    val request = method match {
      case "GET" => Request(GET(path))
      case "DELETE" => Request(DELETE(path))
      case "POST" => Request(POST(path, new String(buffer)))
      case "PUT" => Request(PUT(path, new String(buffer)))
    }
    request
  }
}