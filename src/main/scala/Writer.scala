import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by araul on 12/3/17.
  */
object Writer {
  //#printer-messages
  def props: Props = Props[Printer]
  //#printer-messages
  final case class Writer(data: String)
}
//#printer-messages
//#printer-companion

//#printer-actor
class Printer extends Actor with ActorLogging {
  import Writer._

  def receive = {
    case Greeting(greeting) =>
      log.info(s"Greeting received (from ${sender()}): $greeting")
  }
}
