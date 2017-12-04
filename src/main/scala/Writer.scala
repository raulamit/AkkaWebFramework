import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by araul on 12/3/17.
  */
object Writer {
  //#printer-messages
  def props: Props = Props[Writer]
  //#printer-messages
//  final case class request(data: String)
  final case class out(req: Request)
}
//#printer-messages
//#printer-companion

//#printer-actor
class Writer extends Actor with ActorLogging {
  import Writer._

  def receive = {
    case out(req) =>{
      println("Fetch from User actor",req)
    }
  }
}
