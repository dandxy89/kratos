package com.dandxy.model.user

import com.dandxy.golf.entity.Par
import com.dandxy.golf.input.{ Distance, Strokes }
import com.dandxy.model.user.Identifier.{ GameId, Hole }

final case class AggregateGameResult(
  gameId: GameId,
  hole: Hole,
  distance: Distance,
  par: Par,
  strokeIndex: Int,
  shotCount: Int,
  strokes_gained: Option[Strokes]
)
