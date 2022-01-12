package com.political.speeches

import ElectionEndpoint.electionRoute

import org.http4s.server.blaze.BlazeServerBuilder
import zio._
import zio.console._
import zio.interop.catz._
import zio.interop.catz.implicits._

object Init extends App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    ZIO
      .runtime[ZEnv]
      .flatMap { implicit runtime =>
        BlazeServerBuilder[Task](runtime.platform.executor.asEC)
          .bindHttp(8080, "localhost")
          .withHttpApp(electionRoute)
          .resource
          .toManagedZIO
          .useForever
          .foldM(err => onError(err), _ => ZIO.succeed(ExitCode.success))
      }

  private def onError(e: Throwable): URIO[Console, ExitCode] =
    putStrLn(s"Execution failed with: $e").ignore *> ZIO
      .succeed(e.printStackTrace())
      .as(ExitCode.failure)
}
