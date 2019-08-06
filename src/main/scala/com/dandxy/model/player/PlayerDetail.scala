package com.dandxy.model.player

import com.dandxy.model.user.PlayerId

sealed trait PlayerDetail {
  def playerId: PlayerId
}

object PlayerDetail {

  final case class UserProfile(playerId: PlayerId, firstName: String, lastName: String, handicap: Double, password: String)
      extends PlayerDetail

}
