package com.political.speeches

import org.scalatest.EitherValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CsvParserTest extends AnyWordSpec with Matchers with EitherValues {

      "trying to map csv to case class" in {
        import ElemInstances._

        import kantan.csv._
        import kantan.csv.ops._

        val rawData = scala.io.Source.fromFile("src/main/resources/politicians.csv")
        val politicians = ReadResult.sequence(rawData.mkString.asCsvReader[Politician](rfc).toList).value

        politicians.size mustBe 4
      }

}
