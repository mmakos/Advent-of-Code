#ifndef UTILS_H
#define UTILS_H

#include <string>
#include <vector>
#include <regex>
#include <format>
#include <fstream>

namespace aoc {
  typedef std::vector<std::string> input;

  template<typename T>
  std::vector<T> readInput(int year, int day, const std::function<T(const std::string &)> &valueProvider = [](auto str) { return str; }) {
    const auto filename = std::format("input/year{}/day{}.txt", year, day);
    std::vector<T> lines;
    std::ifstream file(filename);

    if (!file.is_open()) {
      throw std::runtime_error(std::format(
          "Could not open input file for day {} year {}: {}", day, year, filename));
    }

    std::string line;
    while (std::getline(file, line)) {
      lines.push_back(valueProvider(line));
    }

    file.close();
    return lines;
  }

  std::vector<int> readInts(int year, int day);

  std::vector<std::vector<int>> readNestedInts(int year, int day, const std::regex &split = std::regex(" "));

  template<typename T>
  int indexOf(const std::vector<T> &vec, const T &element) {
    if (auto it = std::ranges::find(vec, element); it != vec.end()) {
      return std::distance(vec.begin(), it);
    }
    return -1;
  }

  template<typename T>
  bool contains(const std::vector<T> &vec, const T &element) {
    return std::ranges::find(vec, element) != vec.end();
  }

  std::string join(std::vector<std::string> const &elements, const char *delimiter = "");

  std::vector<std::string> split(const std::string &input, const std::regex &regex);
}

#endif //UTILS_H
