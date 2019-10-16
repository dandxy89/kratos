-- AVERAGE SHOTS TAKEN FROM DISTANCE X - FROM PGA WEBSITE
CREATE TABLE pga.FairwayLookup (
    distance INTEGER NOT NULL,
    strokes NUMERIC(6, 3) NOT NULL
    CONSTRAINT fairway_pkey PRIMARY KEY (distance)
);

CREATE TABLE pga.GreenLookup (
    distance INTEGER NOT NULL,
    strokes NUMERIC(6, 3) NOT NULL
    CONSTRAINT green_pkey PRIMARY KEY (distance)
);

CREATE TABLE pga.RecoveryLookup (
    distance INTEGER NOT NULL,
    strokes NUMERIC(6, 3) NOT NULL
    CONSTRAINT recovery_pkey PRIMARY KEY (distance)
);

CREATE TABLE pga.RoughLookup (
    distance INTEGER NOT NULL,
    strokes NUMERIC(6, 3) NOT NULL
    CONSTRAINT rough_pkey PRIMARY KEY (distance)
);

CREATE TABLE pga.SandLookup (
    distance INTEGER NOT NULL,
    strokes NUMERIC(6, 3) NOT NULL
    CONSTRAINT sand_pkey PRIMARY KEY (distance)
);

CREATE TABLE pga.TeeLookup (
    distance INTEGER NOT NULL,
    strokes NUMERIC(6, 3) NOT NULL
    CONSTRAINT tee_pkey PRIMARY KEY (distance)
);

-- PROBABILITIES FROM DISTANCE X - TAKEN FROM PGA WEBSITE
CREATE TABLE pga.PuttingProb (
    distance INTEGER NOT NULL,
    one_putt NUMERIC(6, 3) NOT NULL,
    two_putt NUMERIC(6, 3) NOT NULL,
    three_putt NUMERIC(6, 3) NOT NULL,
    expected_putt NUMERIC(6, 3) NOT NULL
    CONSTRAINT putting_prob_pkey PRIMARY KEY (distance)
);

CREATE TABLE pga.UpAndDownPercentage (
    distance INTEGER NOT NULL,
    from_fairway INTEGER NOT NULL,
    from_rough INTEGER NOT NULL
    CONSTRAINT updown_pkey PRIMARY KEY (distance)
);

CREATE TABLE pga.GreenHitPercentage (
    percentage INTEGER NOT NULL,
    from_fairway INTEGER NOT NULL,
    from_rough INTEGER NOT NULL,
    from_sand INTEGER NOT NULL
    CONSTRAINT greenhit_pkey PRIMARY KEY (percentage)
);

CREATE TABLE pga.StrokesToGo (
    distance INTEGER NOT NULL,
    from_fairway NUMERIC(6, 3) NOT NULL,
    from_rough NUMERIC(6, 3) NOT NULL
    CONSTRAINT strokestogo_pkey PRIMARY KEY (distance)
);

