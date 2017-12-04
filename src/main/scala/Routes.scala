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


case class Request private (method: Method)
sealed abstract trait Method
case class GET(path: String) extends Method
case class DELETE(path: String) extends Method
case class PUT(path: String, body: String) extends Method
case class POST(path: String, body: String) extends Method