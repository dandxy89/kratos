-- PLAYER LOOKUP
----------------
-- P1
INSERT INTO player.playerlookup (player_email, update_time, first_name, last_name)
VALUES ('test@dandxy.com', '01-01-2019 13:01:02', 'dan', 'dixey');
-- P2
INSERT INTO player.playerlookup (player_email, update_time, first_name, last_name)
VALUES ('gdpr@dandxy.com', '01-01-2019 13:01:02', 'dan', 'dixey');

-- SECURITY
-----------
-- P1
INSERT INTO usersecurity.hashedpassword (player_email, hashed_password, player_id)
VALUES ('test@dandxy.com', '123456', 1);
-- P2
INSERT INTO usersecurity.hashedpassword (player_email, hashed_password, player_id)
VALUES ('gdpr@dandxy.com', '123456', 2);

-- GAME
-------
INSERT INTO player.game (player_id, course, game_start_time, handicap)
VALUES (1, 'Dans Course', to_timestamp('02-01-2019 13:01:02', 'dd-mm-yyyy hh24:mi:ss'), 7.5);

-- P2 - Game 1
INSERT INTO player.game (player_id, course, game_start_time, handicap)
VALUES (2, 'GDPR Course', to_timestamp('03-01-2019 13:01:02', 'dd-mm-yyyy hh24:mi:ss'), 8.0);

-- P1 Game 2
INSERT INTO player.game (player_id, course, game_start_time, handicap)
VALUES (1, 'Dans Course 2', to_timestamp('03-01-2019 13:01:02', 'dd-mm-yyyy hh24:mi:ss'), 7.1);

-- CLUB DATA
------------
-- P1
INSERT INTO player.club_data (player_id, club, typical_shape, typical_height, manufacturer, typical_distance, distanceType)
VALUES (1, 1, 1, 1, 1, 200, 1);
-- P2
INSERT INTO player.club_data (player_id, club, typical_shape, typical_height, manufacturer, typical_distance, distanceType)
VALUES (2, 1, 1, 1, 1, 200, 1);

-- SHOT DATA
------------
-- P1
-- GAME 1
INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (1, 1, 1, 4, 446, 1, 1, 0.275, 2);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (1, 1, 2, 4, 116, 2, 22, -0.001, 2);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (1, 2, 3, 4, 17, 6, 25, 0.826, 2);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (3, 1, 1, 4, 446, 1, 1, 0.275, 2);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (3, 1, 2, 4, 116, 2, 22, -0.001, 2);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (3, 2, 3, 4, 17, 6, 25, 0.826, 2);

-- P2
INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (2, 1, 1, 4, 446, 1, 1, 0.275, 2);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (2, 1, 2, 4, 116, 2, 22, -0.001, 2);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
VALUES (2, 2, 3, 4, 17, 6, 25, 0.826, 2);
