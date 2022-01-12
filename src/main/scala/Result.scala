package com.political.speeches

import io.circe._
import io.circe.generic.semiauto._

case class Result(mostSpeeches: String, mostSecurity: String, leastWordy: String)

object JsonHelper {
  implicit val catalogDecoder: Decoder[Result] = deriveDecoder
  implicit val catalogEncoder: Encoder[Result] = deriveEncoder
}
