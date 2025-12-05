#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 1;
constexpr int year = 2025;

int solve1(const aoc::input &input) {
  int dial = 50;
  int password = 0;
  for (const auto &line : input) {
    dial += (line[0] == 'L' ? -1 : 1) * std::stoi(line.substr(1));
    dial = (dial % 100 + 100) % 100;
    if (dial == 0) ++password;
  }
  return password;
}

int solve2(const aoc::input &input) {
  int dial = 50;
  int password = 0;
  for (const auto &line : input) {
    const int rotation = (line[0] == 'L' ? -1 : 1) * std::stoi(line.substr(1));
    if (rotation >= 0) {
      password += (dial + rotation) / 100;
    } else {
      password += (100 - dial - rotation) / 100;
      if (dial == 0) --password;
    }
    dial = ((dial + rotation) % 100 + 100) % 100;
  }
  return password;
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
