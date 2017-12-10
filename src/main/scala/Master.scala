import akka.actor.{Actor, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

/**
  * Created by rutpatel on 12/9/17.
  */



class WriterMaster extends Actor {
  var router = {
    val routees = Vector.fill(1) {
      val r = context.actorOf(Props[Writer])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case WhoToSend(socket) =>
      println("hwereffffffffff")
      router.route(WhoToSend(socket), sender())
    case WriteRaw(response) =>
      router.route(WriteRaw(response), sender())
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Writer])
      context watch r
      router = router.addRoutee(r)
  }
}
