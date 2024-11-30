#ifndef UTILS_H
#define UTILS_H
#include <string>
#include <vector>
#include <regex>

namespace aoc {
  std::vector<std::string> readInput(int year, int day);

  template<typename T>
  int indexOf(const std::vector<T> &vec, const T &element) {
    if (auto it = std::ranges::find(vec, element); it != vec.end()) {
      return std::distance(vec.begin(), it);
    }
    return -1;
  }

  std::string join(std::vector<std::string> const &elements, const char *delimiter = "");

  std::vector<std::string> split(const std::string &input, char delimiter);
}

#endif //UTILS_H
