CREATE TABLE player.playerlookup (
    player_id SERIAL,
    player_email VARCHAR(200) NOT NULL,
    update_time TIMESTAMP NOT NULL,
    first_name VARCHAR(200) NOT NULL,
    last_name VARCHAR(200) NOT NULL,

    CONSTRAINT master_player_id_pkey PRIMARY KEY(player_id)
    -- TODO: Add index for the email
);

CREATE TABLE userSecurity.hashedpassword (
    player_email VARCHAR(200) NOT NULL,
    hashed_password VARCHAR(200) NOT NULL,
    player_id INTEGER REFERENCES player.playerlookup,

    CONSTRAINT player_email_pkey PRIMARY KEY(player_email)
);

CREATE TABLE player.club_data (
    player_id INTEGER REFERENCES player.playerlookup,
    club INTEGER NOT NULL,
    typical_shape INTEGER NOT NULL,
    typical_height INTEGER NOT NULL,
    manufacturer INTEGER NOT NULL,
    typical_distance INTEGER NOT NULL,
    distanceType INTEGER NOT NULL,

    CONSTRAINT club_data_player_id_pkey PRIMARY KEY(player_id)
);

CREATE TABLE player.game (
    game_id SERIAL PRIMARY KEY,
    player_id INTEGER REFERENCES player.playerlookup,
    course VARCHAR(200) NOT NULL,
    game_start_time TIMESTAMP NOT NULL,
    handicap NUMERIC(6, 1) NOT NULL,
    ball_used VARCHAR(200),
    green_speed VARCHAR(200),
    temperature VARCHAR(200),
    wind_speed VARCHAR(200)
);

CREATE TABLE player.shot (
    game_id INTEGER REFERENCES player.game,
    hole INTEGER NOT NULL,
    shot INTEGER NOT NULL,
    par INTEGER NOT NULL,
    distance INTEGER NOT NULL,
    ball_location INTEGER NOT NULL,
    club VARCHAR(200) NOT NULL,
    strokes_gained NUMERIC(6, 3) NOT NULL,
    orientation VARCHAR(200),
    shot_shape VARCHAR(200),
    shot_height VARCHAR(200),
    stroke_index VARCHAR(200),

    CONSTRAINT player_shot_pkey PRIMARY KEY(game_id, hole, shot)
);