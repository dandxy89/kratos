package com.dandxy.model.error

import com.dandxy.model.golf.input.Distance

sealed trait DomainError {
  def msg: String
}

object DomainError {

  final case class InvalidDistance(distance: Distance) extends DomainError {
    def msg: String = s" Invalid distance: $distance"
  }

  final case class StatisticNotKnown(distance: Distance) extends DomainError {
    def msg: String = s" Statistic distance not known: $distance"
  }

}
