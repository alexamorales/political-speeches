package com.political.speeches

import data.{Politician, Result}
import service.StatisticsLogic

import org.scalatest.{EitherValues, stats}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

class StatisticsTest extends AnyWordSpec with Matchers with EitherValues {

  "trying to map csv to case class" in {

    import ElemInstances._
    import kantan.csv._
    import kantan.csv.ops._
    import io.circe._
    import io.circe.generic.semiauto._


    val rawData = scala.io.Source.fromFile("src/main/resources/politicians.csv")
    val politicians = ReadResult.sequence(rawData.mkString.asCsvReader[Politician](rfc).toList).value

    val statisticResult = StatisticsLogic.processPoliticans(politicians)

    val jsonResult = statisticResult.asJson

  }

}
