import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by araul on 12/3/17.
  */
object Writer {
  //#printer-messages
  def props: Props = Props[Writer]
  //#printer-messages
  final case class Out(data: String)
}
//#printer-messages
//#printer-companion

//#printer-actor
class Writer extends Actor with ActorLogging {
  import Writer._

  def receive = {
    case Out(data) =>
      println("Writing data")//(s"Data received (from ${sender()}): $data")
  }
}
