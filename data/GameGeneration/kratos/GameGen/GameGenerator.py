#!/usr/bin/env python
# -*- coding: utf-8 -*-
""" kratos/GameGen/GameGenerator
"""
import numpy as np


def generator(minimum: int, maximum: int) -> int:
    """ Bounder random selection to Integer
    """
    return np.int(np.random.uniform(minimum, maximum))


def find_club_id(clubs, identifier: int):
    """ Find club params by ID
    """
    return list(filter(lambda x: x[0] == identifier, clubs))[0]


def putt_length(limit):
    """ Putt length generator of at least 1 yard
    """
    return np.int(np.random.uniform() * limit) + 1


def fetch_putt(par, count):
    """ Calculate the number of putts

        20% chance of converting a 3 -> 2 putt
        20% chance of converting a 2 -> 1 putt

    """
    if count > par:
        return 3 - (1 if np.random.uniform() < 0.2 else 0)
    elif count == par:
        return 2 - (1 if np.random.uniform() < 0.2 else 0)
    else:
        return 1


def duff_generator(minimum: int, maximum: int) -> int:
    """ Generate a duff shot of a given length of at least 1 yard
    """
    return np.int(generator(minimum, maximum) * 0.45) + 1


def random_hole_selection(par_three, par_four, par_five):
    """ Select a hole within the constraints of the config
    """
    rv = np.random.randint(3)

    if rv == 0 and par_three[1] < par_three[0]:
        return rv, ((par_three[0], par_three[1] + 1), par_four, par_five), 3
    elif rv == 1 and par_four[1] < par_four[0]:
        return rv, (par_three, (par_four[0], par_four[1] + 1), par_four), 4
    elif rv == 2 and par_five[1] < par_five[0]:
        return rv, (par_three, par_four, (par_five[0], par_five[1] + 1)), 5
    else:
        # Recursively function will return once a holes been selected
        return random_hole_selection(par_three, par_four, par_five)


def shot_generator(
    config,
    clubs,
    remaining_shot,
    hole_type,
    remaining_dist,
    duff,
    game_id,
    index,
    hole_par,
):
    """ Shot generator
    """
    accum = 0
    accum_shot = 0
    hole_index = index
    index = remaining_shot - config[hole_type][7] if duff else remaining_shot
    hits = []

    for ind in np.arange(index):
        if duff:
            club = generator(config[hole_type][8], 24)
            shot_distance = duff_generator(0, find_club_id(clubs, club)[2])

        else:
            club = generator(
                config[hole_type][9 + ind][0], config[hole_type][9 + ind][1]
            )
            shot_distance = generator(
                find_club_id(clubs, club)[1], find_club_id(clubs, club)[2]
            )

        accum += shot_distance
        accum_shot += 1

        payload = {
            "gameId": game_id,
            "hole": hole_index + 1,
            "shot": 1 + ind,
            "par": hole_par,
            "distance": shot_distance,
            "location": 1 if (remaining_shot - (ind - 1)) == 1 else 2,  # TODO
            "club": club,
            "strokeIndex": index + 1,
        }

        hits.append(payload)

    return accum, accum_shot, hits


def generate_putts(putt_config, n_putt, game_id, index, shots_remaining, hole_par):
    """ Generate putt data
    """
    putt_lengths = [putt_length(putt_config[p][1]) for p in np.arange(n_putt)]
    putt_lengths.sort()
    all_shots = []

    for pl in putt_lengths:
        # Generate payload
        payload = {
            "gameId": game_id,
            "hole": index + 1,
            "shot": shots_remaining,
            "par": hole_par,
            "distance": pl,
            "location": 6,
            "club": 25,
            "strokeIndex": index + 1,
        }

        shots_remaining -= 1
        all_shots.append(payload)

    return all_shots


def combine_list(combined, new_list):
    """ Appends two lists
    """
    for sub_list in new_list:
        combined.append(sub_list)

    return combined


def generate_hole(config, clubs, criteria, putt_config, game_id, stroke_index):
    """ Generate a hole output
    """
    # Select a hole to generate
    (hole_type, criteria, hole_par) = random_hole_selection(
        criteria[0], criteria[1], criteria[2]
    )
    all_shots = []

    # Determine the number of shots to be taken
    shot_count = generator(config[hole_type][1], config[hole_type][2])

    # Determine the distance
    total_distance = 0
    dist = generator(config[hole_type][3], config[hole_type][4])
    n_putt = fetch_putt(config[hole_type][6], shot_count)

    # Generator putt lengths
    putts = generate_putts(
        putt_config, n_putt, game_id, stroke_index, shot_count, hole_par
    )
    all_shots = combine_list(all_shots, putts)

    # Shots remaining
    remaining_shot = shot_count - n_putt

    # Duff shot setup
    if (remaining_shot - config[hole_type][7]) > 0:
        (accum, accum_shot, hits) = shot_generator(
            config,
            clubs,
            remaining_shot,
            hole_type,
            0,
            True,
            game_id,
            stroke_index,
            hole_par,
        )

        # Remaining distances and shots
        dist -= accum
        remaining_shot -= accum_shot
        total_distance += accum
        all_shots = combine_list(all_shots, hits)

    # Take the remaining shots
    (accum, accum_shot, hits) = shot_generator(
        config,
        clubs,
        remaining_shot,
        hole_type,
        dist,
        False,
        game_id,
        stroke_index,
        hole_par,
    )
    all_shots = combine_list(all_shots, hits)

    # Remaining distances and shots
    total_distance += accum

    return criteria, shot_count, total_distance, all_shots


def generate_course(config, clubs, putt_config, game_id):
    """ Generate Course
    """
    # Determine the number of holes to generate
    shot_count = 0
    total_distance = 0
    num_hole = np.sum([item[0] for item in config])
    course_shots = []

    # Selection criteria
    criteria = ((config[0][0], 0), (config[1][0], 0), (config[2][0], 0))

    # Generate a hole via a loop
    for stroke_index in np.arange(num_hole):
        # Run the generate hole function
        (s, sc, td, hs) = generate_hole(
            config, clubs, criteria, putt_config, game_id, stroke_index
        )
        criteria = s
        shot_count += sc
        total_distance += td
        hs.reverse()

        # Append all of the shots
        course_shots = combine_list(course_shots, hs)

    # Print final shot count
    print("Final shot count:: {}".format(shot_count))
    print("Final shot distance:: {}".format(total_distance))

    return course_shots
