#include <iostream>
#include <numeric>
#include <regex>
#include <vector>

#include <utils.h>

constexpr int day = 1;
constexpr int year = 2023;

const std::vector<std::string> numbers = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

int strToNumber(const std::string &str) {
  if (isdigit(str[0])) {
    return stoi(str);
  }
  return aoc::indexOf<std::string>(numbers, str) + 1;
}

std::pair<std::string, std::string> getFirstAndLastMatch(const std::string &str, const std::regex &regex) {
  std::smatch first;
  std::regex_search(str, first, regex);
  std::smatch last;

  for (auto i = std::prev(str.end()); !std::regex_search(i, str.end(), last, regex); --i);

  return std::make_pair(first.str(), last.str());
}

int solveSingleStr(const std::string &str, const std::regex &regex) {
  const auto [first, second] = getFirstAndLastMatch(str, regex);
  return strToNumber(first) * 10 + strToNumber(second);
}

int solve(const std::vector<std::string> &input, const std::regex &regex) {
  return std::transform_reduce(input.begin(), input.end(), 0,
                               std::plus(),
                               [&regex](const std::string &str) { return solveSingleStr(str, regex); });
}

int solve1(const std::vector<std::string> &input) {
  return solve(input, std::regex(R"(\d)"));
}

int solve2(const std::vector<std::string> &input) {
  return solve(input, std::regex(R"(\d|)" + aoc::join(numbers, "|")));
}

int main() {
  const std::vector<std::string> input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
