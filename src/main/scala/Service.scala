package com.political.speeches

object Service {

   def processPoliticans(politicians: List[Politician]): Result = {
    val most2013Speeches = politicians.filter(p => p.date.getYear == 2013).map(_.speaker).take(2) match {
      case xs if xs.length == 1 => Some(xs.head)
      case _ => None
    }
    val mostInternalSecuritySpeech = politicians.filter(p => p.topic == "Internal Security").map(_.speaker).take(2) match {
      case xs if xs.length == 1 => Some(xs.head)
      case _ => None
    }
    val fewestWordsOverall = politicians.sortBy(_.words).take(2) match {
      case xs if xs.length == 1 => Some(xs.head.speaker)
      case _ => None
    }
    Result(most2013Speeches, mostInternalSecuritySpeech, fewestWordsOverall)
  }

}
