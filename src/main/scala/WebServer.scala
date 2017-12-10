/**
  * Created by rutpatel on 12/7/17.
  */

import akka.actor.ActorRefFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.util.concurrent.GlobalEventExecutor

//class WebServer(val config: WebServerConfig,
//                val actorFactory: ActorRefFactory,
//                handlerFactory: WebServer => ChannelInboundHandler) {
//  val allChannels = new DefaultChannelGroup("socko", GlobalEventExecutor.INSTANCE)
//
//  def start(): Unit = {
//    val bossGroup = new NioEventLoopGroup
//    val workerGroup = new NioEventLoopGroup
//    val bootstrap = new ServerBootstrap()
//      .group(bossGroup, workerGroup)
//      .channel(classOf[NioServerSocketChannel])
//
//    bootstrap.childHandler(new ChannelInitializer[SocketChannel] { // (4)
//      @Override
//      def initChannel(ch: SocketChannel) {
//        ch.pipeline().addLast(new RequestHandler());
//      }
//    })
//    val bindFutures = bootstrap.bind("localhost", 8080)
//
//  }
//}
//
//
//object Test {
//  @Override
//  def main(args:Array[String])={
//    new WebServer().start()
//  }
//}