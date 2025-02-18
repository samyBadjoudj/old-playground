import ctypes
import pathlib
from ctypes import *
import json


def get_book_distance_values(distances):
    return [distance["value"] for distance in distances]


def get_book_distance_titles(distances):
    return [distance["book"]["title"] for distance in distances]


def get_data_from_cpp_lib(catalog_file_name="catalog.json"):
    libname = pathlib.Path().absolute() / "libml-reco-books.so"
    cpp_lib = ctypes.CDLL(libname)
    cpp_lib.getAllRecoData.restype = c_char_p
    cpp_lib.getAllRecoData.argtypes = [c_char_p]
    cpp_result = cpp_lib.getAllRecoData(catalog_file_name.encode("utf-8"))
    return json.loads(cast(cpp_result, c_char_p).value.decode("utf-8").strip())


def get_high_variance_categories_3d(data):
    books = data["booksHighVariances"]
    labels = [cat['name'] for cat in books[0]["categories"]]
    coordinates = {
        "x": {"name": labels[0], "values": []},
        "y": {"name": labels[1], "values": []},
        "z": {"name": labels[2], "values": []},
        "mapping": {labels[0]: "x", labels[1]: "y", labels[2]: "z"},
        "books": []}
    for book in books:
        coordinates["books"].append(book["title"])
        for cat in book["categories"]:
            mapped_label = coordinates["mapping"][cat["name"]]
            coordinates[mapped_label]["values"].append(cat["rate"])
    return coordinates


def get_centroid_feature_coordinates(data,x_label,y_label, z_label):
    book = data["centroid"]
    coordinates = {
        "x": 0,
        "y": 0,
        "z": 0,
        "mapping": {x_label: "x", y_label: "y", z_label: "z"},}
    for cat in book["categories"]:
        if cat["name"] in coordinates["mapping"]:
            coordinates[coordinates["mapping"][cat["name"]]] = cat["rate"]
    return coordinates
