package com.dandxy.model.stats

import com.dandxy.golf.entity.GolfClub
import com.dandxy.golf.input.Strokes
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class StokesGainedByClub(club: GolfClub, minimum: Strokes, average: Strokes, maximum: Strokes)

object StokesGainedByClub {
  implicit val en: Encoder[StokesGainedByClub] = deriveEncoder
  implicit val de: Decoder[StokesGainedByClub] = deriveDecoder
}
