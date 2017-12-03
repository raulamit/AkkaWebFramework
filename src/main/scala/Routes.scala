import MyActorWebserver.ActorWebserver

/**
  * Created by araul on 12/3/17.
  */


class Routes(request: Request, actorWebserver: ActorWebserver) {

}

object Routes{
  def apply(funcList: PartialFunction[(Request), Unit]*) = {
    funcList.toList.reduceLeft { (functions, f) => functions orElse f }
  }
}
case class Request private (method: String, path: String)
