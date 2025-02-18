import mlrecobooks.data_extractor as mld

data_from_cpp = mld.get_data_from_cpp_lib()
import mlrecobooks.plotter as mlp

mlp.plot_distances(data_from_cpp)

mlp.plot_all_books_scatter(data_from_cpp)
