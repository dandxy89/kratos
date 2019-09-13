import pandas as pd
import numpy as np

# Course configuration
NUMBER_PAR3 = (6, 2, 6, 100, 230, "Par 3")
NUMBER_PAR4 = (6, 3, 7, 285, 475, "Par 4")
NUMBER_PAR5 = (6, 3, 8, 380, 579, "Par 5")

HOLE_CONFIG = [
    NUMBER_PAR3,
    NUMBER_PAR4,
    NUMBER_PAR5
]

# Golf clubs
DRIVER = (1, 225, 330)
WOOD5 = (4, 200, 260)

IRON4 = (15, 185, 220)
IRON5 = (16, 170, 205)
IRON6 = (17, 155, 190)
IRON7 = (18, 140, 175)
IRON8 = (19, 125, 160)
IRON9 = (20, 110, 145)

WEDGE_PW = (21, 70, 130)
WEDGE_GW = (22, 55, 110)
WEDGE_SW = (23, 40, 89)
WEDGE_LW = (24, 30, 60)

PUTTER = (25, 0, 30)

GOLF_CLUBS = [
    PUTTER,
    WEDGE_LW,
    WEDGE_SW,
    WEDGE_GW,
    WEDGE_PW,
    IRON9,
    IRON8,
    IRON7,
    IRON6,
    IRON5,
    IRON4,
    WOOD5,
    DRIVER
]


def bounded_random_generator(min, max):
    """ Bounder random selection to Integer
    """
    return np.int(np.random.uniform(min, max))


def random_hole(par_three, par_four, par_five):
    """ Create a hole if within constraints
    """
    random_value = np.random.randint(3)

    if random_value == 0 and par_three[1] < par_three[0]:
        return (random_value, ((par_three[0], par_three[1] + 1), par_four, par_five))
    elif random_value == 1 and par_four[1] < par_four[0]:
        return (random_value, (par_three, (par_four[0], par_four[1] + 1), par_five))
    elif random_value == 2 and par_five[1] < par_five[0]:
        return (random_value, (par_three, par_four, (par_five[0], par_five[1] + 1)))
    else:
        return random_hole(par_three, par_four, par_five)


def generate_hole(hole_config, golf_clubs):
    """ Generate hole
    """
    # Determine the number of holes to generate
    hole_count = np.sum([item[0] for item in hole_config])

    # Selection crieteria
    selection_criteria = (
        (hole_config[0][0], 0), (hole_config[1][0], 0), (hole_config[2][0], 0))

    # Generate a hole with a loop
    for each_hole in np.arange(hole_count):

        # Select a hole to generate
        selection = random_hole(
            selection_criteria[0], selection_criteria[1], selection_criteria[2])

        # Updated selection criteria
        selection_criteria = selection[1]

        # Hole to generate for
        hole_selection = selection[0]

        # Determine the number of shots to be taken
        shot_count = bounded_random_generator(
            HOLE_CONFIG[hole_selection][1], HOLE_CONFIG[hole_selection][2])

        # Determine the distance
        distance = bounded_random_generator(
            HOLE_CONFIG[hole_selection][3], HOLE_CONFIG[hole_selection][4])

        print(hole_config[hole_selection][5], " Shots:",
              shot_count, "Distance:", distance)


generate_hole(HOLE_CONFIG, GOLF_CLUBS)
