#include "utils.h"

#include <format>
#include <fstream>

namespace aoc {
    std::vector<std::string> readInput(const int year, const int day) {
        const auto filename = std::format("input/year{}/day{}.txt", year, day);
        std::vector<std::string> lines;
        std::ifstream file(filename);

        if (!file.is_open()) {
            throw std::runtime_error(std::format(
                "Could not open input file for day {} year {}: {}", day, year, filename));
        }

        std::string line;
        while (std::getline(file, line)) {
            lines.push_back(line);
        }

        file.close();
        return lines;
    }
}
