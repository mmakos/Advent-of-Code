#include "utils.h"

#include <algorithm>
#include <format>
#include <fstream>
#include <iterator>
#include <sstream>

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

  std::string join(std::vector<std::string> const &elements, const char *const delimiter) {
    std::ostringstream os;
    auto b = begin(elements);
    const auto e = end(elements);

    if (b != e) {
      std::copy(b, prev(e), std::ostream_iterator<std::string>(os, delimiter));
      b = prev(e);
    }
    if (b != e) {
      os << *b;
    }

    return os.str();
  }

  std::vector<std::string> split(const std::string &input, char delimiter) {
    std::vector<std::string> output;
    for (auto cur = begin(input), beg = cur; ; ++cur) {
      if (cur == end(input) || *cur == delimiter || !*cur) {
        output.emplace(output.end(), beg, cur);
        if (cur == end(input) || !*cur)
          break;
        beg = next(cur);
      }
    }
    return output;
  }
}
