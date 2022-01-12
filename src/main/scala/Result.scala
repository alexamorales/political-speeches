package com.political.speeches

import io.circe._
import io.circe.generic.semiauto._

case class Result(mostSpeeches: Option[String], mostSecurity: Option[String], leastWordy: Option[String])