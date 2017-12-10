import io.netty.channel.ChannelHandlerContext
import java.util.Date

/**
  * Created by rutpatel on 12/9/17.
  */
abstract class SockoEvent() {

  // netty channel associated with this request
  val context: ChannelHandlerContext

  /**
    * The end point to which the message was addressed
    */
  val endPoint: EndPoint

  /**
    * Store of items that can be used to pass data from route to processor and between processors.
    *
    * This map is not synchronized and not thread-safe. In most cases, we expect this cache to be used by a single
    * thread - hence a standard map is faster.
    *
    * If you do need to use a thread safe map, from your route, instance and store a `ConcurrentHashMap` as an item
    * in this cache.
    */
  lazy val items: collection.mutable.Map[String, Any] = collection.mutable.Map.empty[String, Any]

  val createdOn: Date = new Date()

  def duration(): Long = {
    new Date().getTime - createdOn.getTime
  }
}