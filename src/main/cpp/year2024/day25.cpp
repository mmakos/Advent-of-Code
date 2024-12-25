#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 25;
constexpr int year = 2024;

std::array<int, 5> parseKeyLock(const std::vector<std::string> &keyLock) {
  std::array result{-1, -1, -1, -1, -1};
  for (const auto &row: keyLock) {
    for (int i = 0; i < 5; ++i) {
      if (row[i] == '#') ++result[i];
    }
  }
  return result;
}

std::pair<std::vector<std::array<int, 5>>, std::vector<std::array<int, 5>>> parseInput(const aoc::input &input) {
  std::vector<std::vector<std::string>> keyLocks = {{}};
  for (const auto &line: input) {
    if (line.empty()) keyLocks.emplace_back();
    else keyLocks[keyLocks.size() - 1].push_back(line);
  }

  std::vector<std::array<int, 5>> keys;
  std::vector<std::array<int, 5>> locks;
  for (const auto &keyLock: keyLocks) {
    if (keyLock[0][0] == '#') locks.push_back(parseKeyLock(keyLock));
    else keys.push_back(parseKeyLock(keyLock));
  }

  return {locks, keys};
}

bool fits(const std::array<int, 5> &lock, const std::array<int, 5> &key) {
  for (int i = 0; i < 5; ++i) {
    if (lock[i] + key[i] > 5) return false;
  }
  return true;
}

int solve1(const aoc::input &input) {
  const auto &[locks, keys] = parseInput(input);
  int fit = 0;
  for (const auto &lock : locks) {
    for (const auto &key : keys) {
      if (fits(lock, key)) ++fit;
    }
  }

  return fit;
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
}
