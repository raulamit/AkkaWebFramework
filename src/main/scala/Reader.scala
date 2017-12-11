/**
  * Created by araul on 12/3/17.
  */

import java.io.{BufferedReader, InputStreamReader}
import java.util.NoSuchElementException
import akka.actor.{Actor, ActorRef, Props}

object Reader {
  def props(writerRef: ActorRef): Props = Props(new Reader(writerRef))
}

class Reader(val master: ActorRef) extends Actor {

  def receive = {
    case ReadReq(socket, routes) => {
      val input = new BufferedReader(new InputStreamReader(socket.getInputStream()))
      val request: Request = requestFromServer(input)
      sender ! WhoToSend(socket, request, routes)
    }
  }

  private def requestFromServer(input: BufferedReader) = {
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
    var buffer = ""
    try {
      val i: Int = Integer.parseInt(headers("Content-Length"))
      val bufferRead = new Array[Char](i)
      input.read(bufferRead)
      buffer = buffer.concat(new String(bufferRead))
    } catch {
      case e: NoSuchElementException => {
      }
    }
    val request = method match {
      case "GET" => Request(GET(path))
      case "DELETE" => Request(DELETE(path))
      case "POST" => Request(POST(path, new String(buffer)))
      case "PUT" => Request(PUT(path, new String(buffer)))
    }
    request
  }
}