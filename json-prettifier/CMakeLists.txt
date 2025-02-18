#[[
    This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
]]

cmake_minimum_required(VERSION 3.0)
project(json-prettifier)

set (JSON_PRETTY_NAME \"json-prettifier\")
set (JSON_PRETTY_VERSION_MAJOR 1)
set (JSON_PRETTY_VERSION_MINOR 5)
set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS}")
set(SOURCE_FILES json-prettifier.c)


configure_file (
        "${PROJECT_SOURCE_DIR}/json-prettifier.h.in"
        "${PROJECT_BINARY_DIR}/json-prettifier.h"
)


include_directories(/usr/include/)
include_directories(/usr/include/json-glib-1.0/)
include_directories(/usr/include/glib-2.0/)
include_directories(/usr/lib/x86_64-linux-gnu/glib-2.0/include)
link_directories(/usr/lib/x86_64-linux-gnu/)
add_executable(json-prettifier ${SOURCE_FILES})
install(TARGETS json-prettifier DESTINATION bin)
target_link_libraries(json-prettifier  json-glib-1.0 pthread gio-2.0 gobject-2.0 glib-2.0)
