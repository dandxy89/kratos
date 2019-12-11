#!/usr/bin/env python
# -*- coding: utf-8 -*-
""" kratos/GameGen/Constants
"""
HOLE_CONFIG = [
    # Course configuration
    # N_HOLE, MIN_S, MAX_S, MIN_D, MAX_D, Label, Par, Norm_shots_to_hole, Duff_shot_min_id
    # 0, 1, 2, 3, 4, 5, 6, 7, 8
    (6, 2, 6, 100, 230, "Par 3", 3, 1, 15, (15, 22), (), ()),
    (6, 3, 7, 285, 475, "Par 4", 4, 2, 19, (13, 16), (15, 23), ()),
    (6, 3, 8, 380, 579, "Par 5", 5, 3, 22, (13, 14), (15, 21), (18, 21)),
]

GOLF_CLUBS = [
    # Golf club configuration
    (25, 0, 30),  # PUTTER
    (24, 25, 60),  # WEDGE_LW
    (23, 55, 89),  # WEDGE_SW
    (22, 85, 110),  # WEDGE_GW
    (21, 105, 130),  # WEDGE_PW
    (20, 120, 145),  # IRON9
    (19, 135, 160),  # IRON8
    (18, 150, 175),  # IRON7
    (17, 170, 190),  # IRON6
    (16, 180, 205),  # IRON5
    (15, 200, 225),  # IRON4
    (14, 215, 260),  # WOOD5 # Actual ID 4
    (13, 260, 330),  # Driver # Actual ID 1
]

PUTT_LENGTH = [
    # N_PUTTS, LIMIT_LENGTH
    (1, 25),
    (2, 15),
    (3, 7),
]

DUFF_SHAPE = [6, 7, 8, 9]
