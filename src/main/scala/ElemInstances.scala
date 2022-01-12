package com.political.speeches

import data.{Politician, Result}

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import kantan.csv.RowDecoder
import org.http4s.dsl.impl.OptionalValidatingQueryParamDecoderMatcher
import org.http4s.{ParseFailure, QueryParamDecoder}

import java.net.URL
import java.text.SimpleDateFormat
import java.time.ZoneId
import scala.util.{Failure, Success, Try}

object ElemInstances {

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
        case Success(u) => Right(u)
      }
    }

  implicit val politicianDecoder: RowDecoder[Politician] = RowDecoder.ordered {
    (n: String, s: String, d: String, w: Int) => {
      val sdf = new SimpleDateFormat("yyyy-MM-dd")
      val cd = sdf.parse(d).toInstant.atZone(ZoneId.systemDefault()).toLocalDate;
      data.Politician(n, s, cd, w)
    }

  }

  object URL1QueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[URL]("url1")

  object URL2QueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[URL]("url2")

  implicit val catalogDecoder: Decoder[Result] = deriveDecoder
  implicit val catalogEncoder: Encoder[Result] = deriveEncoder
}
