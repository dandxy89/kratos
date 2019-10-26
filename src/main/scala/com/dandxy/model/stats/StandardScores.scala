package com.dandxy.model.stats

import com.dandxy.golf.input.Strokes
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class StandardScores(eagles: Strokes,
                                birdies: Strokes,
                                pars: Strokes,
                                bogeys: Strokes,
                                doubles: Strokes,
                                triples: Strokes,
                                others: Strokes)

object StandardScores {
  implicit val en: Encoder[StandardScores] = deriveEncoder
  implicit val de: Decoder[StandardScores] = deriveDecoder
}
