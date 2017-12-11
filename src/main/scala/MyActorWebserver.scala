import java.net.ServerSocket
import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by araul on 12/3/17.
  */
object MyActorWebserver {

  class ActorWebserver(
                        val config: Config,
                        val routes: PartialFunction[(Request, ActorRef), Unit]) {

    private var started = false
    private var stopped = false
    private val serverSocket = new ServerSocket(this.config.port)

    def start(): Unit = if (!started) {
      started = true
      val system: ActorSystem = ActorSystem("WebServer", ConfigFactory.load().getConfig("myapp1"))
      val master: ActorRef = system.actorOf(Props[Master], "master")

      println("started")
      try
          while (!stopped) {
            val socket = serverSocket.accept()
            println(s"open connection ${socket.hashCode()}")
            master ! ReadReq(socket, this.routes)
          }
      finally
        serverSocket.close()
      println("stopped")
    }

    def stop(): Unit = stopped = true
  }

  def main(args: Array[String]): Unit = {
    val config = new Config()
    val routes = Routes({
      case (Request(GET("/hello")), responseWriter) => {
        while (true)
          println("befor get")
        responseWriter ! WriteRaw("actor hello")
        println("after get")
      }
      case (Request(GET("/hello1")), responseWriter) => {
        println("befor get")
        responseWriter ! WriteRaw("actor hello")
        println("after get")
      }
      case (Request(POST("/post", body)), responseWriter) => responseWriter ! WriteRaw(body)
    })
    val webserver = new ActorWebserver(config, routes)
    webserver.start()
    webserver.stop()
  }
}
