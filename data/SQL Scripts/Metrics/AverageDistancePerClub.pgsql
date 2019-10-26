SELECT  club
        , AVG(distance)
FROM "player"."shot" 
WHERE   game_id = 781
GROUP BY club
ORDER BY club ASC
;
