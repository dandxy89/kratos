-- Email verification table
CREATE TABLE player.email_verification (
    player_id INTEGER REFERENCES player.playerlookup,
    verified BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX player_verification_index on player.email_verification (player_id);
