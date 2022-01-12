package com.political.speeches
package server

import com.political.speeches.service.StatisticsLogic.processPoliticans

import com.political.speeches.data.Politician
import kantan.csv._
import kantan.csv.ops._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import zio.Task
import zio.interop.catz._

import scala.util.{Failure, Success, Try}

class ElectionEndpoint extends Http4sDsl[Task] {

  import ElemInstances._

  val route = HttpRoutes
    .of[Task] {
      case GET -> Root / "evaluation" :? URL1QueryParamMatcher(url1) +& URL2QueryParamMatcher(url2) =>
        (url1, url2) match {
          case (Some(url1), _) =>
            url1.fold(
              err => BadRequest(s"Url: ${err.head.details}"), { url =>
                Try {
                  scala.io.Source.fromURL(url).mkString
                }.flatMap(rawData => (ReadResult.sequence(rawData.asCsvReader[Politician](rfc).toList).toTry)
                ) match {
                  case Failure(exception) => BadRequest("not ok" + exception.getMessage)
                  case Success(value) => Ok(processPoliticans(value))
                }
              }
            )

          case _ => BadRequest("Plese provide Query param in format ?url1=urlAddress&url2=urlAddress")
        }
    }
    .orNotFound
}

object ElectionEndpoint {
  val electionRoute = new ElectionEndpoint().route
}

