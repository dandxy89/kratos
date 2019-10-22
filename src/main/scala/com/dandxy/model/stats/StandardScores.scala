package com.dandxy.model.stats

import com.dandxy.golf.input.Strokes

final case class StandardScores(eagles: Strokes,
                                birdies: Strokes,
                                pars: Strokes,
                                bogeys: Strokes,
                                doubles: Strokes,
                                triples: Strokes,
                                others: Strokes)
