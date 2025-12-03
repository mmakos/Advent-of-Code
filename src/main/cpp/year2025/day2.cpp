#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 2;
constexpr int year = 2025;

bool isInvalidId(const std::string &id, const int splits) {
  if (id.size() % splits != 0)
    return false;
  const auto splitLen = id.size() / splits;
  for (int i = 0; i < splits - 1; ++i) {
    if (id.substr(i * splitLen, splitLen) != id.substr((i + 1) * splitLen, splitLen))
      return false;
  }
  return true;
}

uint64_t invalidIds(const uint64_t start, const uint64_t end, const int maxSplits) {
  uint64_t ids = 0;
  for (uint64_t id = start; id <= end; ++id) {
    const auto strId = std::to_string(id);
    for (int splits = 2; splits <= maxSplits && splits <= strId.size(); ++splits) {
      if (isInvalidId(std::to_string(id), splits)) {
        ids += id;
        break;
      }
    }
  }
  return ids;
}

uint64_t solve(const std::string &input, const int maxSplits) {
  uint64_t ids = 0;
  for (const auto &range : aoc::split(input, std::regex(","))) {
    const auto r = aoc::split(range, std::regex("-"));
    ids += invalidIds(std::stoull(r[0]), std::stoull(r[1]), maxSplits);
  }
  return ids;
}

uint64_t solve1(const std::string &input) {
  return solve(input, 2);
}

uint64_t solve2(const std::string &input) {
  return solve(input, 1000);
}

int main() {
  const std::string input = aoc::readInput<std::string>(year, day)[0];

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
