import io.netty.handler.codec.http.QueryStringDecoder
import io.netty.util.CharsetUtil
import scala.collection.JavaConversions.{`deprecated iterableAsScalaIterable`, mapAsScalaMap}

/**
  * Created by rutpatel on 12/9/17.
  */
case class EndPoint(method: String,
                    host: String,
                    uri: String
                   ) {

  lazy val isGET = method == "GET"
  lazy val isPOST = method == "POST"
  lazy val isPUT = method == "PUT"
  lazy val isDELETE = method == "DELETE"


  lazy val queryStringMap: Map[String, List[String]] = {
    val m = new QueryStringDecoder(uri, CharsetUtil.UTF_8).parameters.toMap
    // Map the Java list values to Scala list
    m.map { case (key, value) => (key, value.toList)}
  }


//  def getQueryString(name: String): Option[String] = {
//    try {
//      val v = queryStringMap(name)
//      if (v == null)
//        None
//      else
//        Some(v.get(0))
//    } catch {
//      case ex: NoSuchElementException => None
//    }
//  }

}