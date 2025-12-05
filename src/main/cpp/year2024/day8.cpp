#include <iostream>
#include <ranges>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include <utils.h>
#include <point.h>

constexpr int day = 8;
constexpr int year = 2024;

std::unordered_map<char, std::vector<aoc::Point<>> > parseInput(const aoc::input &input) {
  std::unordered_map<char, std::vector<aoc::Point<>> > map;
  for (int y = 0; y < input.size(); ++y) {
    for (int x = 0; x < input[0].size(); ++x) {
      if (char c = input[y][x]; c != '.') map[c].push_back({x, y});
    }
  }
  return map;
}

void makeAntidotes(const aoc::Point<> &antenna1, const aoc::Point<> &antenna2,
                   std::unordered_set<aoc::Point<>, aoc::PointHash<>> &antidotes1,
                   std::unordered_set<aoc::Point<>, aoc::PointHash<>> &antidotes2,
                   const aoc::Point<> &lowerBounds, const aoc::Point<> &upperBounds) {
  const auto diff = antenna2 - antenna1;
  int i = 0;
  for (auto antidote = antenna1; antidote >= lowerBounds && antidote < upperBounds; antidote -= diff, ++i) {
    if (i == 1) antidotes1.insert(antidote);
    antidotes2.insert(antidote);
  }
  i = 0;
  for (auto antidote = antenna2; antidote >= lowerBounds && antidote < upperBounds; antidote += diff, ++i) {
    if (i == 1) antidotes1.insert(antidote);
    antidotes2.insert(antidote);
  }
}

std::pair<int, int> solve(const aoc::input &input) {
  constexpr aoc::Point<> lowerBounds = {0, 0};
  const aoc::Point<> upperBounds = {static_cast<int>(input[0].size()), static_cast<int>(input.size())};
  const auto antennas = parseInput(input);
  std::unordered_set<aoc::Point<>, aoc::PointHash<>> antidotes1;
  std::unordered_set<aoc::Point<>, aoc::PointHash<>> antidotes2;

  for (auto &ant: std::views::values(antennas)) {
    for (int i = 0; i < ant.size() - 1; ++i) {
      for (int j = i + 1; j < ant.size(); ++j) {
        makeAntidotes(ant[i], ant[j], antidotes1, antidotes2, lowerBounds, upperBounds);
      }
    }
  }
  return {antidotes1.size(), antidotes2.size()};
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  auto [solution1, solution2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
