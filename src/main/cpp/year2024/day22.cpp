#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 22;
constexpr int year = 2024;

uint64_t next(uint64_t secret) {
  secret = (secret << 6 ^ secret) & 0xFFFFFF;
  secret = (secret >> 5 ^ secret) & 0xFFFFFF;
  return (secret << 11 ^ secret) & 0xFFFFFF;
}

uint64_t solve1(const aoc::input &input) {
  uint64_t sum = 0;
  for (const auto &line: input) {
    uint64_t secret = std::stoull(line);
    for (int i = 0; i < 2000; ++i) {
      secret = next(secret);
    }
    sum += secret;
  }
  return sum;
}

int toInt5(const int i) {
  return i < 0 ? -i | 0x10 : i;
}

// Sequence is 4 * 5 bits (like an int5_t, from -9 to 9)
std::unordered_map<int, int> sequences(uint64_t secret) {
  std::unordered_map<int, int> map;
  int last = secret % 10;
  int sequence = 0;
  for (int i = 0; i < 2000; ++i) {
    secret = next(secret);
    const int value = secret % 10;
    const int change = value - last;
    sequence = sequence << 5 & 0xfffff | toInt5(change);
    if (i >= 3 && !map.contains(sequence)) map[sequence] = value;
    last = value;
  }
  return map;
}

int solve2(const aoc::input &input) {
  std::pmr::unordered_map<int, int> bananas;
  for (const auto &line: input) {
    for (const auto [sequence, value]: sequences(std::stoull(line))) {
      bananas[sequence] += value;
    }
  }
  return std::ranges::max_element(bananas, [](const auto &p1, auto &p2) { return p1.second < p2.second; })->second;
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
