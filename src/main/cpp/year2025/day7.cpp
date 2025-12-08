#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 7;
constexpr int year = 2025;

std::vector<std::unordered_set<int>> parseInput(const aoc::input &input) {
  const auto start = input[0].find_first_of('S');
  std::vector<std::unordered_set<int>> levels;

  for (int i = 2; i < input.size(); i += 2) {
    levels.emplace_back();
    const auto &line = input[i];
    for (int j = 0; j < line.size(); ++j) {
      if (line[j] == '^') {
        levels.rbegin()->insert(j - static_cast<int>(start));
      }
    }
  }
  return levels;
}

int solve1(const aoc::input &input) {
  const auto &levels = parseInput(input);
  std::unordered_set beams = {0};
  int splits = 0;
  for (const auto &splitters : levels) {
    std::unordered_set<int> newBeams;
    for (const auto beam : beams) {
      if (splitters.contains(beam)) {
        newBeams.insert(beam - 1);
        newBeams.insert(beam + 1);
        ++splits;
      } else {
        newBeams.insert(beam);
      }
    }
    beams = std::move(newBeams);
  }
  return splits;
}

uint64_t timelinesFromLevel(const std::vector<std::unordered_set<int>> &levels, const int level, const int position,
                            std::unordered_map<uint64_t, uint64_t> &cache) {
  if (level >= levels.size()) return 1;
  const uint64_t cacheKey = static_cast<uint64_t>(level) << 32 | static_cast<uint32_t>(position);
  if (const auto it = cache.find(cacheKey); it != cache.end()) {
    return it->second;
  }

  const auto &splitters = levels[level];
  uint64_t result;
  if (splitters.contains(position)) {
    result = timelinesFromLevel(levels, level + 1, position + 1, cache) + timelinesFromLevel(levels, level + 1, position - 1, cache);
  } else {
    result = timelinesFromLevel(levels, level + 1, position, cache);
  }
  cache[cacheKey] = result;
  return result;
}

uint64_t solve2(const aoc::input &input) {
  const auto &levels = parseInput(input);
  std::unordered_map<uint64_t, uint64_t> cache;
  return timelinesFromLevel(levels, 0, 0, cache);
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
