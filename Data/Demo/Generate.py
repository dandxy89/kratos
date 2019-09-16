import pandas as pd
import numpy as np


HOLE_CONFIG = [
    # Course configuration
    # N_HOLE, MIN_S, MAX_S, MIN_D, MAX_D, Label, Par, Norm_shots_to_hole, Duff_shot_min_id
    # 0, 1, 2, 3, 4, 5, 6, 7, 8
    (6, 2, 6, 100, 230, "Par 3", 3, 1, 15, (15, 22), (), ()),
    (6, 3, 7, 285, 475, "Par 4", 4, 2, 19, (13, 16), (15, 23), ()),
    (6, 3, 8, 380, 579, "Par 5", 5, 3, 22, (13, 14), (15, 21), (18, 21))
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
    (13, 260, 330)  # Driver # Actual ID 1
]

PUTT_LENGTH = [
    # N_PUTTS, LIMIT_LENGTH
    (1, 25),
    (2, 15),
    (3, 7)
]

DUFF_SHAPE = [6, 7, 8, 9]


def generator(min, max):
    """ Bounder random selection to Integer
    """
    return np.int(np.random.uniform(min, max))


def find_club_id(clubs, id):
    """ Find club params by ID
    """
    return list(filter(lambda x: x[0] == id, clubs))[0]


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


def duff_generator(min, max):
    """ Generate a duff shot of a given length of at least 1 yard
    """
    return np.int(generator(min, max) * 0.45) + 1


def random_hole(par_three, par_four, par_five):
    """ Select a hole within the constraints of the config
    """
    rv = np.random.randint(3)

    if rv == 0 and par_three[1] < par_three[0]:
        return (rv, ((par_three[0], par_three[1] + 1), par_four, par_five))
    elif rv == 1 and par_four[1] < par_four[0]:
        return (rv, (par_three, (par_four[0], par_four[1] + 1), par_five))
    elif rv == 2 and par_five[1] < par_five[0]:
        return (rv, (par_three, par_four, (par_five[0], par_five[1] + 1)))
    else:
        # Recursively function will return once a holes been selected
        return random_hole(par_three, par_four, par_five)


def shot_generator(config, clubs, remaining_shot, hole_type, remaining_dist, duff):
    """ Shot generator
    """
    accum = 0
    accum_shot = 0
    index = remaining_shot - config[hole_type][7] if duff else remaining_shot

    for ind in np.arange(index):
        if duff:
            club = generator(config[hole_type][8], 24)
            shot_distance = duff_generator(0, find_club_id(clubs, club)[2])
        else:
            club = generator(config[hole_type][9 + ind]
                             [0], config[hole_type][9 + ind][1])
            shot_distance = generator(find_club_id(clubs, club)[
                                      1], find_club_id(clubs, club)[2])

        accum += shot_distance
        accum_shot += 1
        # print("Club: ", club, "Distance", shot_distance)

    return (accum, accum_shot)


def generate_hole(config, clubs, criteria, putt_config):
    """ Generate a hole output
    """
    # Select a hole to generate
    selection = random_hole(criteria[0], criteria[1], criteria[2])
    criteria = selection[1]
    hole_type = selection[0]

    # Determine the number of shots to be taken
    shot_count = generator(config[hole_type][1], config[hole_type][2])

    # Determine the distance
    total_distance = 0
    dist = generator(config[hole_type][3], config[hole_type][4])
    n_putt = fetch_putt(config[hole_type][6], shot_count)

    #print(config[hole_type][5], " Shots:", shot_count,"Distance:", dist, "Putts:", n_putt)

    # Generator putt lengths
    putt_lengths = [putt_length(putt_config[p][1]) for p in np.arange(n_putt)]
    putt_lengths.sort(reverse=True)

    # Shots remaining
    remaining_shot = shot_count - n_putt

    # Duff shot setup
    if remaining_shot - config[hole_type][7] > 0:
        (accum, accum_shot) = shot_generator(
            config, clubs, remaining_shot, hole_type, 0, True)

        # Remaining distances and shots
        dist -= accum
        remaining_shot -= accum_shot
        total_distance += accum

    # Take the remaining shots
    (accum, accum_shot) = shot_generator(
        config, clubs, remaining_shot, hole_type, dist, False)

    # Remaining distances and shots
    total_distance += accum

    return (criteria, shot_count, total_distance)


def generate_course(config, clubs, putt_config):
    """ Generate hole
    """
    # Determine the number of holes to generate
    shot_count = 0
    total_distance = 0
    num_hole = np.sum([item[0] for item in config])

    # Selection crieteria
    criteria = ((config[0][0], 0), (config[1][0], 0), (config[2][0], 0))

    # Generate a hole via a loop
    for _ in np.arange(num_hole):

        # Run the generate hole function
        (s, sc, td) = generate_hole(config, clubs, criteria, putt_config)
        criteria = s
        shot_count += sc
        total_distance += td

    # Print final shot count
    print("Final shot count:: {}".format(shot_count))
    print("Final shot distance:: {}".format(total_distance))


if __name__ == "__main__":
    for _ in np.arange(1):
        generate_course(HOLE_CONFIG, GOLF_CLUBS, PUTT_LENGTH)
        print("")
