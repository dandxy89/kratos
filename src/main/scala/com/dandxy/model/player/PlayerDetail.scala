package com.dandxy.model.player

sealed trait PlayerDetail {
  def playerId: PlayerId
}

object PlayerDetail {

  final case class UserProfile(playerId: PlayerId, firstName: String, lastName: String, handicap: Double, password: String)
      extends PlayerDetail

}
