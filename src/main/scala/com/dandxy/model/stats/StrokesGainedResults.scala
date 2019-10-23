package com.dandxy.model.stats

import com.dandxy.golf.entity.{ GolfClub, Location, Par }
import com.dandxy.golf.input.{ Distance, Strokes }
import com.dandxy.model.user.Identifier.Hole
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class StrokesGainedResults(hole: Hole,
                                      shot: Int,
                                      par: Par,
                                      distance: Distance,
                                      ballLocation: Location,
                                      club: GolfClub,
                                      strokesGained: Strokes,
                                      strokesIndex: Int)

object StrokesGainedResults {
  implicit val en: Encoder[StrokesGainedResults] = deriveEncoder
  implicit val de: Decoder[StrokesGainedResults] = deriveDecoder
}
