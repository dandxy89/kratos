#!/usr/bin/env python
# -*- coding: utf-8 -*-
""" development/JSON2Parquet
"""
import logging
import multiprocessing
import os
import re
import warnings
from functools import partial
from math import ceil

import numpy as np
import pandas as pd

warnings.filterwarnings("ignore")

if "setup.py" not in os.listdir():
    os.chdir("../")


def location_mapping(row: dict) -> int:
    """
        # 1 = > TeeBox
        # 2 = > Fairway
        # 3 = > Rough
        # 4 = > Bunker
        # 5 = > Recovery
        # 6 = > OnTheGreen
        # 7 = > Hazard
        # 8 = > OutOfBounds
        # 9 = > LostBall
        # 10 = > UnplayableLie
        # 11 = > OneShotPenalty
        # _ = > TwoShotPenalty

    """
    if row["from"] == "OFW":
        return 2
    elif row["from"] == "OTB":
        return 1
    elif row["from"] == "OGR":
        return 6
    elif row["from"] == "ORO":
        return 3
    elif row["from"] == "OIR":
        return 5
    elif row["from"] == "OGS":
        return 4
    elif row["from"] == "OST":
        return 4
    elif row["from"] == "OUK":
        return 11
    elif row["from"] == "OTH":
        return 7
    elif row["from"] == "ONA":
        return 3
    elif row["from"] == "OWA":
        return 5
    elif row["from"] == "OGB":
        return 3
    else:
        return 1000


def process_document(input_df: pd.DataFrame, filter_value: tuple) -> pd.DataFrame:
    """
    """
    df = input_df.xs(filter_value)

    df[["x1", "y1", "z1"]] = df[["x", "y", "z"]].shift(1)
    df[["shotText_"]] = df[["shotText"]].shift(1)

    df.loc[:, "_gameId"] = None
    df.loc[:, "_hole"] = df["hole"]
    df.loc[:, "_shot"] = df["n"]
    df.loc[:, "_par"] = None
    df.loc[:, "_distance"] = df.apply(lambda row: handler(row), axis=1)
    df.loc[:, "_location"] = df.apply(lambda row: location_mapping(row), axis=1)
    df.loc[:, "_club"] = None

    return df[["_gameId", "_hole", "_shot", "_par", "_distance", "_location", "_club"]]


def get_shot_one(text: str) -> int:
    """
    """
    rg = re.compile(".*?\\d+.*?(\\d+)", re.IGNORECASE | re.DOTALL)
    m = rg.search(text)
    if m:
        return int(m.group(1))
    else:
        return 0


def distance_three_d(row: dict) -> float:
    """
    """
    return np.round(
        np.sqrt(
            np.power(float(row["x"]) - float(row["x1"]), 2)
            + np.power(float(row["y"]) - float(row["y1"]), 2)
            + np.power(float(row["z"]) - float(row["z1"]), 2)
        )
        * 0.33333333
    )


def handler(row: dict) -> float:
    """
    """
    if "in the hole" in row["shotText"]:
        return 0
    elif int(row["n"]) == 1:
        return get_shot_one(row["shotText"])
    else:
        return distance_three_d(row)


def run_pool_map(func, data):
    """ Runs a function with a list of tasks in multiprocessing mode

        :param func:
        :param data:
        :returns:

    """
    process_amount = ceil(multiprocessing.cpu_count() - 1)
    function_name = func.func.__name__ if hasattr(func, "func") else func.__name___
    logging.info(
        f"Initialising Pool for function: {function_name}, Process count: {process_amount}"
    )
    with multiprocessing.Pool(processes=process_amount) as pool:
        output = pool.map(func, data)

    return output


if __name__ == "__main__":
    shots_df = pd.read_parquet("shot_storage.parquet")
    print(f"Shot DF shape: {shots_df.shape}")
    print(f"DF columnsL {shots_df.columns}")

    # Sort values ['tournament_id', 'year', 'player_id', 'round', 'hole']
    shots_df.sort_values(
        by=["tournament_id", "year", "player_id", "round", "hole"], inplace=True
    )

    shots_df = shots_df[shots_df["year"].apply(lambda x: int(x)) >= 2016]

    shots_df.index = pd.MultiIndex.from_frame(
        shots_df[["tournament_id", "year", "player_id", "round", "hole"]]
    )

    unique_combinations = np.array(shots_df.index.unique())
    print(
        f"Shot DF shape: shots={shots_df.shape[0]} - holes={unique_combinations.shape[0]}"
    )

    process_document_partial = partial(process_document, shots_df)
    res = run_pool_map(process_document_partial, unique_combinations.tolist())

    output_df = pd.concat(res, axis=0)
    output_df.to_parquet(
        "prepared_shots.parquet", engine="fastparquet", compression="gzip", index=False
    )
