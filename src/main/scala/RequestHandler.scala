//import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
//import io.netty.handler.codec.http.HttpRequest
//
///**
//  * Created by rutpatel on 12/9/17.
//  */
//class RequestHandler(server: WebServer, routes: PartialFunction[SockoEvent, Unit]) extends  ChannelInboundHandlerAdapter{
//  override def channelRead(ctx: ChannelHandlerContext, e: AnyRef): Unit = {
//    e match {
//      case httpRequest: HttpRequest =>
//        httpRequest
//      case default =>
//      {
//        import io.netty.buffer.ByteBuf
//        import io.netty.util.ReferenceCountUtil
//        val in = e.asInstanceOf[ByteBuf]
//        try
//            while ( {
//              in.isReadable
//            }) { // (1)
//              System.out.print(in.readByte.toChar)
//              System.out.flush()
//            }
//        finally ReferenceCountUtil.release(e) // (2)
//      }
//    }
//  }
//}