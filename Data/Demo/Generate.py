import pandas as pd
import numpy as np

# Course configuration
HOLE_CONFIG = [
    (6, 2, 6, 100, 230, "Par 3", 3),
    (6, 3, 7, 285, 475, "Par 4", 4),
    (6, 3, 8, 380, 579, "Par 5", 5)
]

# Golf club configuration
GOLF_CLUBS = [
    (25, 0, 30),  # PUTTER
    (24, 30, 60),  # WEDGE_LW
    (23, 40, 89),  # WEDGE_SW
    (22, 55, 110),  # WEDGE_GW
    (21, 70, 130),  # WEDGE_PW
    (20, 110, 145),  # IRON9
    (19, 125, 160),  # IRON8
    (18, 140, 175),  # IRON7
    (17, 155, 190),  # IRON6
    (16, 170, 205),  # IRON5
    (15, 185, 220),  # IRON4
    (4, 200, 260),  # WOOD5
    (1, 225, 330)  # Driver
]


def bounded_random_generator(min, max):
    """ Bounder random selection to Integer
    """
    return np.int(np.random.uniform(min, max))


def fetch_putt(par, shot_count):
    """ Calculate the number of putts
    """
    if shot_count > par:
        return 3
    elif shot_count == par:
        return 2
    else:
        return 1


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


def generate_hole(hole_config, selection_criteria):
    """ Generate a hole output
    """
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
          shot_count, "Distance:", distance, "Putts:", fetch_putt(HOLE_CONFIG[hole_selection][6], shot_count))

    return (selection_criteria, 1)


def generate_course(hole_config, golf_clubs):
    """ Generate hole
    """
    # Determine the number of holes to generate
    hole_count = np.sum([item[0] for item in hole_config])

    # Selection crieteria
    selection_criteria = (
        (hole_config[0][0], 0), (hole_config[1][0], 0), (hole_config[2][0], 0))

    # Generate a hole via a loop
    for each_hole in np.arange(hole_count):
        
        # Run the generate hole function
        hole_data = generate_hole(hole_config, selection_criteria)

        # Update the selection criteria
        selection_criteria = hole_data[0]


if __name__ == "__main__":
    generate_course(HOLE_CONFIG, GOLF_CLUBS)
