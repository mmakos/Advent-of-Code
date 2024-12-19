#include <iostream>
#include <unordered_map>
#include <vector>

#include <utils.h>

constexpr int day = 19;
constexpr int year = 2024;

std::unordered_map<std::string, uint64_t> cache;

uint64_t countArrangements(const std::string &pattern, const std::vector<std::string> &towels) {
  if (pattern.empty()) return 1;
  const auto c = cache.find(pattern);
  if (c != cache.end()) return c->second;

  uint64_t arrangements = 0;
  for (auto it = towels.begin(); it != towels.end(); ++it) {
    if (pattern.starts_with(*it)) {
      arrangements += countArrangements(pattern.substr(it->size()), towels);
    }
  }
  cache[pattern] = arrangements;
  return arrangements;
}

std::pair<uint64_t, uint64_t> solve(const aoc::input &input) {
  const std::vector<std::string> towels = aoc::split(input[0], std::regex(", "));
  uint64_t task1 = 0;
  uint64_t task2 = 0;
  for (auto it = input.begin() + 2; it != input.end(); ++it) {
    const uint64_t arrangements = countArrangements(*it, towels);
    if (arrangements) ++task1;
    task2 += arrangements;
  }
  return {task1, task2};
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  const auto [solution1, solution2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
