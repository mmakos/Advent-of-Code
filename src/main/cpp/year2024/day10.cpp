#include <iostream>
#include <point.h>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 10;
constexpr int year = 2024;

const std::vector<aoc::Point<>> dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

std::unordered_map<aoc::Point<>, int, aoc::PointHash<>> parseInput(const aoc::input &input) {
  std::unordered_map<aoc::Point<>, int, aoc::PointHash<>> map;
  for (int y = 0; y < input.size(); ++y) {
    for (int x = 0; x < input[y].size(); ++x) {
      map[{x, y}] = input[y][x] - '0';
    }
  }

  return map;
}

unsigned int score(const std::unordered_map<aoc::Point<>, int, aoc::PointHash<>> &points, const aoc::Point<> &startPoint, // NOLINT(*-no-recursion)
                   const int lastValue, std::unordered_set<aoc::Point<>, aoc::PointHash<>> &result) {
  const auto &value = points.find(startPoint);
  if (value == points.end() || (value->second - lastValue) != 1) return 0;
  if (value->second == 9) {
    result.insert(startPoint);
    return 1;
  }

  unsigned int score1 = 0;
  for (const auto &dir: dirs) {
    score1 += score(points, startPoint + dir, value->second, result);
  }

  return score1;
}

std::pair<unsigned int, unsigned int> solve(const aoc::input &input) {
  unsigned int score1 = 0;
  unsigned int score2 = 0;
  for (const auto pairs = parseInput(input); const auto &[point, value]: pairs) {
    if (value == 0) {
      std::unordered_set<aoc::Point<>, aoc::PointHash<>> result;
      score2 += score(pairs, point, -1, result);
      score1 += result.size();
    }
  }

  return {score1, score2};
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  const auto [solution1, solution2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
