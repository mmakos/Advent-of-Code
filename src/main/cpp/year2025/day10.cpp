#include <iostream>
#include <queue>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 10;
constexpr int year = 2025;

namespace aoc {
  struct Machine {
    const uint16_t indicator;
    const std::vector<std::vector<int>> wiringSchematics;
    const std::vector<int> joltageRequirements;
  };

  struct VectorHash {
    std::size_t operator()(const std::vector<int> &v) const {
      std::size_t h = 0;
      for (const int x : v) {
        h ^= std::hash<int>()(x) + 0x9e3779b9 + (h << 6) + (h >> 2);
      }
      return h;
    }
  };
}

aoc::Machine parseInput(const std::string &input) {
  const auto &split = aoc::split(input, std::regex(" "));
  uint16_t indicator = 0;
  std::vector<std::vector<int>> wiringSchematics;
  std::vector<int> joltageRequirements;
  for (int i = 1; i < split[0].size() - 1; ++i) {
    if (split[0][i] == '#') indicator |= 1 << i - 1;
  }
  for (int i = 1; i < split.size() - 1; ++i) {
    std::vector<int> schematic;
    for (int j = 1; j < split[i].size() - 1; j += 2) schematic.push_back(split[i][j] - '0');
    wiringSchematics.push_back(schematic);
  }
  const auto &last = split[split.size() - 1];
  const auto &lastSplit = aoc::split(last.substr(1, last.size() - 1), std::regex(","));
  for (const auto &r : lastSplit) joltageRequirements.push_back(std::stoi(r));

  return {indicator, wiringSchematics, joltageRequirements};
}

uint16_t applySchematic(const uint16_t indicator, const std::vector<int> &schematic) {
  auto newIndicator = indicator;
  for (const auto i : schematic) {
    newIndicator ^= 1 << i;
  }
  return newIndicator;
}

std::vector<int> applyJoltage(const std::vector<int> &joltage, const std::vector<int> &schematic) {
  auto newJoltage = joltage;
  for (const auto i : schematic) {
    --newJoltage[i];
  }
  return newJoltage;
}

int solveMachine(const aoc::Machine &machine) {
  std::queue<std::pair<uint16_t, int>> queue;
  std::unordered_set<uint16_t> visited;
  queue.emplace(machine.indicator, 0);

  while (!queue.empty()) {
    const auto [indicator, presses] = queue.front();
    if (indicator == 0) return presses;

    for (const auto &schematic : machine.wiringSchematics) {
      const uint16_t newIndicator = applySchematic(indicator, schematic);
      if (!visited.contains(newIndicator)) {
        visited.insert(newIndicator);
        queue.emplace(newIndicator, presses + 1);
      }
    }
    queue.pop();
  }
  throw new std::runtime_error("No solution");
}

int solveMachine2(const aoc::Machine &machine) {
  std::queue<std::pair<std::vector<int>, int>> queue;
  std::unordered_set<std::vector<int>, aoc::VectorHash> visited;
  queue.emplace(machine.joltageRequirements, 0);

  while (!queue.empty()) {
    const auto [joltage, presses] = queue.front();
    if (std::ranges::all_of(joltage, [](const int j) { return j == 0; })) return presses;

    for (const auto &schematic : machine.wiringSchematics) {
      const std::vector<int> newJoltage = applyJoltage(joltage, schematic);
      if (!visited.contains(newJoltage) && std::ranges::all_of(newJoltage, [](const int j) { return j >= 0; })) {
        visited.insert(newJoltage);
        queue.emplace(newJoltage, presses + 1);
      }
    }
    queue.pop();
  }
  throw new std::runtime_error("No solution");
}

int solve1(const aoc::input &input) {
  int result = 0;
  for (const auto &line : input) {
    result += solveMachine(parseInput(line));
  }
  return result;
}

int solve2(const aoc::input &input) {
  int result = 0;
  for (const auto &line : input) {
    result += solveMachine2(parseInput(line));
    std::cout << result << std::endl;
  }
  return result;
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
