package com.dandxy.model.stats

import com.dandxy.golf.entity.GolfClub
import com.dandxy.golf.input.Strokes

final case class StokesGainedByClub(club: GolfClub, minimum: Strokes, average: Strokes, maximum: Strokes)
