import java.net.Socket

import akka.actor.ActorRef

/**
  * Created by rutpatel on 12/10/17.
  */
trait Event extends Serializable

case class WhoToSend(socket: Socket, request: Request, routes: PartialFunction[(Request, ActorRef), Unit]) extends Event

case class WriteRaw(rawResponse: String) extends Event

trait Method extends Event

case class Request(method: Method) extends Event
case class GET(path: String) extends Method
case class DELETE(path: String) extends Method
case class PUT(path: String, body: String) extends Method
case class POST(path: String, body: String) extends Method

case class ReadReq(socket: Socket, routes : PartialFunction[(Request, ActorRef), Unit]) extends Event