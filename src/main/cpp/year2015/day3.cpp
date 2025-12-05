#include <iostream>
#include <point.h>
#include <unordered_map>
#include <unordered_set>

#include <utils.h>

constexpr int day = 3;
constexpr int year = 2015;

const std::unordered_map<char, aoc::Point<>> dirs = {{'^', {0, 1}}, {'>', {1, 0}}, {'v', {0, -1}}, {'<', {-1, 0}}};

std::pair<int, int> solve(const std::string_view &input) {
  std::unordered_set<aoc::Point<>, aoc::PointHash<>> houses1;
  std::unordered_set<aoc::Point<>, aoc::PointHash<>> houses2;
  aoc::Point<> santa1{0, 0};
  aoc::Point<> santa2{0, 0};
  aoc::Point<> robo2{0, 0};
  houses1.insert(santa1);
  houses2.insert(santa2);

  for (auto it = input.begin(); it != input.end(); it += 2) {
    santa1 += dirs.at(*it);
    houses1.insert(santa1);
    santa1 += dirs.at(*(it + 1));
    houses1.insert(santa1);
    santa2 += dirs.at(*it);
    houses2.insert(santa2);
    robo2 += dirs.at(*(it + 1));
    houses2.insert(robo2);
  }
  return {houses1.size(), houses2.size()};
}

int main() {
  const std::string input = aoc::readInput<std::string>(year, day)[0];
  const auto [solution1, solution2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
