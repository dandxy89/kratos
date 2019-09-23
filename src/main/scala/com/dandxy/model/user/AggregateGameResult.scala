package com.dandxy.model.user

import com.dandxy.golf.entity.Par
import com.dandxy.golf.input.{ Distance, Strokes }
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class AggregateGameResult(
  gameId: GameId,
  hole: Hole,
  distance: Distance,
  par: Par,
  strokeIndex: Int,
  shotCount: Int,
  strokes_gained: Option[Strokes]
)

object AggregateGameResult {
  implicit val e: Encoder[AggregateGameResult] = deriveEncoder
  implicit val d: Decoder[AggregateGameResult] = deriveDecoder
}
