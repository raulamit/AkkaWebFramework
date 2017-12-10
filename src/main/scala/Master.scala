import akka.actor.{Actor, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

/**
  * Created by rutpatel on 12/9/17.
  */



class WriterMaster extends Actor {
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[Writer])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case w: Writer =>
      router.route(w, sender())
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Writer])
      context watch r
      router = router.addRoutee(r)
  }
}
