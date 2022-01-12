package com.political.speeches
package server

import server.ElectionEndpoint.electionRoute

import org.http4s.server.blaze.BlazeServerBuilder
import zio.console.{Console, putStrLn}
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.{App, ExitCode, Task, URIO, ZEnv, ZIO}

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
