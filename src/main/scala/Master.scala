import akka.actor.{Actor, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

/**
  * Created by rutpatel on 12/9/17.
  */



class WriterMaster extends Actor {
  var router = {
    val routees = Vector.fill(30) {
      val r = context.actorOf(Props[Writer])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case WhoToSend(socket, request,routes) =>
      println("hwereffffffffff")
      router.route(WhoToSend(socket, request,routes), sender())
    case WriteRaw(response) => {
      println("before master write")
      router.route(WriteRaw(response), sender())
      println("after master write")
    }
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Writer])
      context watch r
      router = router.addRoutee(r)
  }
}
