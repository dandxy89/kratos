SELECT  hole, 
        shot, 
        par, 
        distance, 
        ball_location, 
        club, 
        strokes_gained, 
        stroke_index
FROM "player"."shot" 
WHERE game_id = 781
ORDER BY strokes_gained ASC
LIMIT 10
;