#include <cmath>
#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 3;
constexpr int year = 2025;

uint64_t joltage(const std::string &batteries, const int count) {
  uint64_t jol = 0;
  int max = -1;
  int maxIdx = 0;
  for (int i = 1; i <= count; ++i) {
    for (int j = maxIdx; j < batteries.size() - (count - i); ++j) {
      if (batteries[j] - '0' > max) {
        max = batteries[j] - '0';
        maxIdx = j + 1;
      }
    }
    jol += max * static_cast<uint64_t>(std::pow(10, count - i));
    max = -1;
  }
  return jol;
}

uint64_t solve(const aoc::input &input, const int count) {
  uint64_t j = 0;
  for (const auto &batteries : input) {
    j += joltage(batteries, count);
  }
  return j;
}

uint64_t solve1(const aoc::input &input) {
  return solve(input, 2);
}

uint64_t solve2(const aoc::input &input) {
  return solve(input, 12);
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
