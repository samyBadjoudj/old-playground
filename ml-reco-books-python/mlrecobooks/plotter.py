import matplotlib.pyplot as plt
import numpy as np
import mlrecobooks.data_extractor as ml


def plot_distances(data):
    distances_ = data["distances"] # sorted(data["distances"], key=lambda d: d["value"])
    plt.yticks(np.arange(0, distances_[-1]["value"] + 2, step=0.5))
    plt.bar(ml.get_book_distance_titles(distances_), ml.get_book_distance_values(distances_), align="center")
    plt.show()


def plot_all_books_scatter(data):
    print(data)
    data_to_plot = ml.get_high_variance_categories_3d(data)
    x_label = data_to_plot["x"]["name"]
    y_label = data_to_plot["y"]["name"]
    z_label = data_to_plot["z"]["name"]
    centroid_to_plot = ml.get_centroid_feature_coordinates(data, x_label, y_label, z_label)
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.set_zlabel(z_label)
    for i, (x, y, z) in enumerate(
            zip(data_to_plot["x"]["values"], data_to_plot["y"]["values"], data_to_plot["z"]["values"])):
        ax.text(x, y, z, data_to_plot["books"][i])

    scat_books = ax.scatter(data_to_plot["x"]["values"], data_to_plot["y"]["values"], data_to_plot["z"]["values"],
                            c='r',
                            marker='o')
    ax.text(centroid_to_plot["x"], centroid_to_plot["y"], centroid_to_plot["z"], "CENTROID")
    centroid = ax.scatter(centroid_to_plot["x"], centroid_to_plot["y"], centroid_to_plot["z"], color="blue", marker="^")
    ax.legend((scat_books, centroid), ("books", "centroid"))
    plt.gcf().text(0.02, 0.5, "Centroid composition:\n-" + "\n-".join(data["favoritesBooks"]), fontsize=8)
    plt.show()
