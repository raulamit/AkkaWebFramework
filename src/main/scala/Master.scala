import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.routing._

/**
  * Created by rutpatel on 12/9/17.
  */

class Master extends Actor {
  val router1 = context.actorOf(Writer.props.withRouter(FromConfig), "writerRouter")

  val router2 = context.actorOf(Reader.props(self).withRouter(FromConfig), "readerRouter")

  def receive = {

    //writer messages
    case WhoToSend(socket, request, routes) =>
      router1 ! (WhoToSend(socket, request, routes))
    case WriteRaw(response) => {
      router1 ! (WriteRaw(response), sender())
    }
    //reader messages
    case ReadReq(socket,routes) => {
    router2 ! ReadReq(socket,routes)
    }
  }
}

object MasterActor extends App{
  val system: ActorSystem = ActorSystem("webserver")
  val writerActorRef = system.actorOf(Props[Master],"master")
}