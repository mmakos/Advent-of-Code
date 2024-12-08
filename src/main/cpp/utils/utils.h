#ifndef UTILS_H
#define UTILS_H

#include <string>
#include <vector>
#include <regex>
#include <format>
#include <fstream>
#include <algorithm>
#include <iterator>
#include <sstream>

namespace aoc {
  typedef std::vector<std::string> input;

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

  inline std::string join(std::vector<std::string> const &elements, const char *const delimiter) {
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

  inline std::vector<std::string> split(const std::string &input, const std::regex &regex) {
    std::vector<std::string> output;
    std::sregex_token_iterator it(input.begin(), input.end(), regex, -1);
    for (const std::sregex_token_iterator end; it != end; ++it) {
      output.push_back(*it);
    }
    return output;
  }

  template<typename T>
  std::vector<T> findAll(const std::string &string, const std::regex &regex,
                         const std::function<T(const std::string &)> &valueProvider = [](auto &str) { return str; }) {
    std::vector<T> vector;
    const std::sregex_iterator end;
    for (auto it = std::sregex_iterator(string.begin(), string.end(), regex); it != end; ++it) {
      vector.push_back(valueProvider(it->str()));
    }
    return vector;
  }

  template<typename T>
  std::vector<T> readInput(int year, int day,
                           const std::function<T(const std::string &)> &valueProvider = [](auto &str) { return str; }) {
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

  inline std::vector<int> readInts(const int year, const int day) {
    return readInput<int>(year, day, [](auto &str) { return std::stoi(str); });
  }

  inline std::vector<std::vector<int>> readNestedInts(const int year, const int day, const std::regex &delimiter) {
    return readInput<std::vector<int>>(year, day, [delimiter](auto &str) {
      const std::vector<std::string> strings = split(str, delimiter);
      std::vector<int> ints(strings.size());
      std::transform(strings.begin(), strings.end(), ints.begin(), [](auto &s) { return std::stoi(s); });
      return ints;
    });
  }
}

#endif //UTILS_H
