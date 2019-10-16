SELECT  club, 
        MIN(strokes_gained), 
        AVG(strokes_gained), 
        MAX(strokes_gained)
FROM "player"."shot" 
WHERE game_id = 781
GROUP BY club
ORDER BY AVG(strokes_gained) DESC
;