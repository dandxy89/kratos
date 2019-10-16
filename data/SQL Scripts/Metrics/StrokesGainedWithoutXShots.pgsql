SELECT SUM(strokes_gained)
FROM "player"."shot"
WHERE shot_serial NOT IN (
    SELECT  shot_serial
    FROM "player"."shot"
    WHERE game_id = 781
    ORDER BY strokes_gained ASC
    LIMIT 5
) AND game_id = 781
GROUP BY game_id
;
