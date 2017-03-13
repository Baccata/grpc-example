package com.github.baccata

import java.util.concurrent.TimeUnit

import com.github.baccata.protocol.{Credentials, UserServiceGrpc}
import io.grpc.ManagedChannel
import io.grpc.netty.NettyChannelBuilder

import scala.util.{Failure, Success}

object Client extends App {

  implicit val scheduler = scala.concurrent.ExecutionContext.Implicits.global

  val channel: ManagedChannel =
    NettyChannelBuilder.forAddress("localhost", 9090).usePlaintext(true).build

  val service = UserServiceGrpc.stub(channel)
  val result = service.login(Credentials("johndoe", "password11"))

  result.onComplete {
    case Success(m) => println(m)
    case Failure(e) => e.printStackTrace()
  }


  Thread.sleep(1000)
  channel.shutdown.awaitTermination(10, TimeUnit.SECONDS)

}
