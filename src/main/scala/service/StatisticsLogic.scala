package com.political.speeches
package service

import data.{Politician, Result}

object StatisticsLogic {

  def processPoliticans(politicians: List[Politician]): Result = {
    val most2013Speeches = politicians.filter(p => p.date.getYear == 2013).map(_.speaker).take(2) match {
      case xs if xs.length == 1 => Some(xs.head)
      case _ => None
    }
    val mostInternalSecuritySpeech = politicians.filter(p => p.topic.trim.equalsIgnoreCase("Internal Security")).map(_.speaker).take(2) match {
      case xs if xs.length == 1 => Some(xs.head)
      case _ => None
    }
    /*val fewestWordsOverall = politicians.groupBy(_.speaker)
    val mapWithNames = fewestWordsOverall.map {
      nameToTaskMap: (String, List[Politician]) =>
        val (name, taskList) = nameToTaskMap
        val valueList = taskList.map(_.words).flatten
        val sum: Int = valueList.sum
        (name, sum)
    }*/



    Result(most2013Speeches, mostInternalSecuritySpeech, None)
  }

}
