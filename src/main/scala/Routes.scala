import MyActorWebserver.ActorWebserver
import akka.actor.ActorRef

/**
  * Created by araul on 12/3/17.
  */


class Routes(request: Request, actorWebserver: ActorWebserver) {

}

object Routes{
  def apply(funcList: PartialFunction[(Request, ActorRef), Unit]*) = {
    funcList.toList.reduceLeft { (functions, f) => functions orElse f }
  }
}


