#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 9;
constexpr int year = 2024;

int solve1(const std::vector<std::string> &input) {
  return 0; // TODO
}

int solve2(const std::vector<std::string> &input) {
  return 0; // TODO
}

int main() {
  const std::vector<std::string> input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
