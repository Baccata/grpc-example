package com.github.baccata

import com.github.baccata.protocol.UserServiceGrpc.UserService
import com.github.baccata.protocol.{Credentials, User, UserServiceGrpc}
import io.grpc.Status.Code
import io.grpc.netty.NettyServerBuilder
import io.grpc.{Metadata, StatusException}

import scala.concurrent.Future

object Server extends App {

  val scheduler = scala.concurrent.ExecutionContext.Implicits.global

  val db = Map[(String, String), User](
    ("johndoe64", "password") -> User("John", "Doe", "john@doe.com", false)
  )

  val service = new UserService {
    override def login(request: Credentials): Future[User] = {
      db.get((request.login, request.password)) match {
        case Some(user) => Future.successful(user)
        case None =>
          Future.failed(
            new StatusException(
              Code.NOT_FOUND.toStatus.withDescription("Didn't find user"),
              new Metadata()))
      }
    }
  }

  val server = NettyServerBuilder
    .forPort(9090)
    .addService(UserServiceGrpc.bindService(service, scheduler))
    .build
    .start

  println(s"Server running on ${server.getPort}")

  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run(): Unit = {
      System.err.println(
        "*** shutting down gRPC server since JVM is shutting down")
      server.shutdown
      System.err.println("*** server shut down")
    }
  })

  server.awaitTermination()
}
