package com.dandxy.model.stats

import com.dandxy.golf.input.{ Distance, Strokes }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class PuttsByDistanceToHole(distance: Distance, averageStrokes: Strokes)

object PuttsByDistanceToHole {
  implicit val en: Encoder[PuttsByDistanceToHole] = deriveEncoder
  implicit val de: Decoder[PuttsByDistanceToHole] = deriveDecoder
}
