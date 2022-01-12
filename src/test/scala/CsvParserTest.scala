package com.political.speeches

import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.File

class CsvParserTest  extends AnyWordSpec with Matchers with OptionValues {

  "Should parse CSV " should {

    import kantan.csv._
    import kantan.csv.ops._

     val csv = scala.io.Source.fromFile("politicians.csv")






}

}
