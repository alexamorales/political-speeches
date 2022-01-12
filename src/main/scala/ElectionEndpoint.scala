package com.political.speeches

import kantan.csv._
import kantan.csv.ops._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import zio.interop.catz._
import zio.{Task, _}

import java.net.URL
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

class ElectionEndpoint extends Http4sDsl[Task] {

  import ElectionEndpoint._

  val route = HttpRoutes
    .of[Task] {
      case GET -> Root / "evaluation" :? URL1QueryParamMatcher(url1) +& URL1QueryParamMatcher(url2) => {
        (url1, url2) match {
          case (Some(url1), Some(url2)) =>
            url1.fold(
              err => BadRequest(s"Url: ${err.head.details}"), { u =>
                ZIO
                  .fromTry{Using(Source.fromURL(u)) { source =>
                    val reader = source.mkString.asCsvReader[Politician](rfc)

                  }}
                  .foldM(error => BadRequest(s"File can`t be loaded: ${error.getMessage}"), res => Ok(res))
              }
            )

          case _ => BadRequest("Plese provide Query param in format ?url1=urlAddress&url2=urlAddress")
        }
      }
    }
    .orNotFound

}

object ElectionEndpoint {

  private val dsl = Http4sDsl[Task]
  import dsl._

  def logic(url1: URL, url2: Option[URL]): ZIO[Any, Throwable, Response[Task]] = {

    for {
     r <- ZIO.fromTry(Using(Source.fromURL(url1)) { source =>
        ZIO.fromEither(ReadResult.sequence(source.mkString.asCsvReader[Politician](rfc).toList))
      })
      .foldM(error => BadRequest(s"File can`t be loaded: ${error.getMessage}"), res => Ok(res))

     } yield r


    }

  val electionRoute = new ElectionEndpoint().route

  implicit val urlQueryParamDecoder: QueryParamDecoder[URL] =
    QueryParamDecoder[String].emap { url =>
      def failure(details: String): Either[ParseFailure, URL] =
        Left(
          ParseFailure(
            sanitized = "Invalid query parameter part",
            details = s"'${url}' is not properly formatted: ${details}"
          )
        )

      Try(new URL(url)) match {
        case Failure(exception) => failure(exception.getMessage)
        case Success(u)         => Right(u)
      }
    }

  object URL1QueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[URL]("url")(urlQueryParamDecoder)

  implicit val politician2Decoder: RowDecoder[Politician] = RowDecoder.decoder(1, 0, 2, 4)(Politician.apply)

   /* implicit val politicianDecoder: RowDecoder[Politician] = RowDecoder.ordered {
    (n: String, s: String, d: SimpleDateFormat, w: Int) =>
      new Politician(n, s, d, w)
  }*/

}
