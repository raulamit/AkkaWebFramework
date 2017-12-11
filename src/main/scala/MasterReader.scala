import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import akka.actor.{Actor, ActorRef, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
/**
  * Created by araul on 12/10/17.
  */

object MasterReader {
  def props(writerRef: ActorRef, numActors: Int): Props = Props(new MasterReader(writerRef, numActors))
}

class MasterReader(val writer: ActorRef,val numActors:Int)  extends Actor{
  var router = {
    val routees = Vector.fill(numActors) {
      val r = context.actorOf(Reader.props(writer))
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case (socket: Socket, routes : PartialFunction[(Request, ActorRef), Unit]) => {
      router.route((socket,routes),sender())
    }
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Reader])
      context watch r
      router = router.addRoutee(r)
  }
}
