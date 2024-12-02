cmake_minimum_required(VERSION 3.27)
project(advent_of_code)

set(CMAKE_CXX_STANDARD 26)

set(SOURCES src/main/cpp/utils/utils.cpp)
set(SOURCE_DIR src/main/cpp)

set(YEARS 2023 2024)
set(DAYS 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25)

foreach (YEAR IN LISTS YEARS)
    foreach (DAY IN LISTS DAYS)
        set(TASK_EXECUTABLE "year${YEAR}day${DAY}")
        set(EXECUTABLE_SRC ${SOURCE_DIR}/year${YEAR}/day${DAY}.cpp)

        if (EXISTS ${CMAKE_CURRENT_SOURCE_DIR}/${EXECUTABLE_SRC})
            add_executable(${TASK_EXECUTABLE} ${EXECUTABLE_SRC} ${SOURCES})

            target_include_directories(${TASK_EXECUTABLE} PRIVATE src/main/cpp/utils)

            add_custom_command(
                    TARGET ${TASK_EXECUTABLE} POST_BUILD
                    COMMAND ${CMAKE_COMMAND} -E copy_directory
                    ${CMAKE_SOURCE_DIR}/input
                    ${CMAKE_BINARY_DIR}/input
            )
        endif ()
    endforeach ()
endforeach ()
