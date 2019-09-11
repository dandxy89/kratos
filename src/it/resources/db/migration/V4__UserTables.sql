-- Player Lookup
CREATE TABLE player.playerlookup (
    player_id SERIAL,
    player_email VARCHAR(200) NOT NULL,
    update_time TIMESTAMP NOT NULL,
    first_name VARCHAR(200) NOT NULL,
    last_name VARCHAR(200) NOT NULL,

    CONSTRAINT master_player_id_pkey PRIMARY KEY(player_id)
);

CREATE UNIQUE INDEX player_email_index on player.playerlookup (player_email);

-- Password Storage
CREATE TABLE userSecurity.hashedpassword (
    player_email VARCHAR(200) PRIMARY KEY,
    hashed_password VARCHAR(200) NOT NULL,
    player_id INTEGER REFERENCES player.playerlookup
);

CREATE UNIQUE INDEX player_sec_email_index on userSecurity.hashedpassword (player_email);

-- Players clubs
CREATE TABLE player.club_data (
    club_data_serial SERIAL PRIMARY KEY,
    player_id INTEGER REFERENCES player.playerlookup,
    club INTEGER NOT NULL,
    typical_shape INTEGER,
    typical_height INTEGER,
    manufacturer INTEGER,
    typical_distance INTEGER NOT NULL,
    distanceType INTEGER NOT NULL
);

-- Players Games
CREATE TABLE player.game (
    game_id SERIAL PRIMARY KEY,
    player_id INTEGER REFERENCES player.playerlookup,
    course VARCHAR(200) NOT NULL,
    game_start_time TIMESTAMP NOT NULL,
    handicap NUMERIC(6, 1) NOT NULL,
    ball_used VARCHAR(200),
    green_speed VARCHAR(200),
    temperature NUMERIC(6, 3),
    wind_speed INTEGER
);

CREATE UNIQUE INDEX player_game_index on player.game (player_id, course, game_start_time);

-- Players Game result
CREATE TABLE player.game_result (
    game_result_id SERIAL PRIMARY KEY,
    game_id INTEGER REFERENCES player.game,
    score INTEGER NOT NULL,
    strokes_gained NUMERIC(6, 2),
    strokes_gained_off_tee NUMERIC(6, 2),
    strokes_gained_approach NUMERIC(6, 2),
    strokes_gained_around NUMERIC(6, 2),
    strokes_gained_putting NUMERIC(6, 2),
    points INTEGER NOT NULL
);

CREATE UNIQUE INDEX player_game_result_index on player.game_result (game_id);

-- Players hole result
CREATE TABLE player.hole_result (
    game_hole_result_id SERIAL PRIMARY KEY,
    game_id INTEGER REFERENCES player.game,
    hole INTEGER NOT NULL,
    score INTEGER NOT NULL,
    strokes_gained NUMERIC(6, 2),
    strokes_gained_off_tee NUMERIC(6, 2),
    strokes_gained_approach NUMERIC(6, 2),
    strokes_gained_around NUMERIC(6, 2),
    strokes_gained_putting NUMERIC(6, 2),
    points INTEGER NOT NULL
);

CREATE UNIQUE INDEX player_hole_result_index on player.hole_result (game_id, hole);

-- Players shot
CREATE TABLE player.shot (
    shot_serial SERIAL PRIMARY KEY,
    game_id INTEGER REFERENCES player.game,
    hole INTEGER NOT NULL,
    shot INTEGER NOT NULL,
    par INTEGER NOT NULL,
    distance INTEGER NOT NULL,
    ball_location INTEGER NOT NULL,
    club INTEGER NOT NULL,
    strokes_gained NUMERIC(6, 3),
    stroke_index INTEGER NOT NULL,
    orientation INTEGER,
    shot_shape INTEGER,
    shot_height INTEGER
);

CREATE UNIQUE INDEX player_shot_index on player.shot (game_id, hole, shot);
