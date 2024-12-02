#include "utils.h"

#include <algorithm>
#include <iterator>
#include <sstream>

namespace aoc {
  std::vector<int> readInts(int year, int day) {
    return readInput<int>(year, day, [](auto str) { return std::stoi(str); });
  }

  std::vector<std::vector<int>> readNestedInts(int year, int day, const std::regex &split) {
    return readInput<std::vector<int>>(year, day, [split](auto str) {
      const std::vector<std::string> strings = aoc::split(str, split);
      std::vector<int> ints(strings.size());
      std::transform(strings.begin(), strings.end(), ints.begin(), [](auto str) { return std::stoi(str); });
      return ints;
    });
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


  std::vector<std::string> split(const std::string &input, const std::regex &regex) {
    std::vector<std::string> output;
    std::sregex_token_iterator it(input.begin(), input.end(), regex, -1);
    for (const std::sregex_token_iterator end; it != end; ++it) {
      output.push_back(*it);
    }
    return output;
  }
}
