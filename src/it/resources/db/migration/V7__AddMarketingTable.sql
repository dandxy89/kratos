-- Email Marketing table
CREATE TABLE player.email_marketing (
    player_id INTEGER REFERENCES player.playerlookup,
    marketing BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX player_marketing_index on player.email_marketing (player_id);
