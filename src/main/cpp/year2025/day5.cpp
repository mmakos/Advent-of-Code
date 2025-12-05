#include <iostream>
#include <vector>

#include <range.h>
#include <utils.h>

constexpr int day = 5;
constexpr int year = 2025;

std::pair<std::vector<aoc::Range<>>, std::vector<uint64_t>>
parseInput(const aoc::input &input) {
  std::vector<aoc::Range<>> ranges;
  std::vector<uint64_t> ids;
  auto it = input.begin();
  while (!it->empty()) {
    const auto &range = aoc::split(*it++, std::regex("-"));
    ranges.emplace_back(std::stoull(range[0]), std::stoull(range[1]));
  }
  while (++it != input.end()) {
    ids.push_back(std::stoull(*it));
  }
  return std::make_pair(ranges, ids);
}

int solve1(const aoc::input &input) {
  const auto [ranges, ids] = parseInput(input);
  int fresh = 0;
  for (const auto id : ids) {
    for (const auto &range : ranges) {
      if (range.contains(id)) {
        ++fresh;
        break;
      }
    }
  }
  return fresh;
}

uint64_t solve2(const aoc::input &input) {
  const auto [ranges, _] = parseInput(input);
  const auto outRanges = mergeRanges(ranges);
  uint64_t sum = 0;
  for (const auto &range : outRanges) {
    sum += range.size();
  }
  return sum;
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
