-- Email verification table
CREATE TABLE player.email_verification (
    player_email VARCHAR(200) REFERENCES userSecurity.hashedpassword,
    verified BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX player_verification_index on player.email_verification (player_email);
