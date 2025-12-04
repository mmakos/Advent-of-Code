#include <iostream>
#include <point.h>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 4;
constexpr int year = 2025;

std::unordered_set<aoc::Point, aoc::PointHash>
parseInput(const aoc::input &input) {
  std::unordered_set<aoc::Point, aoc::PointHash> points;
  for (int y = 0; y < input.size(); ++y) {
    for (int x = 0; x < input[0].size(); ++x) {
      const char c = input[y][x];
      if (c == '@')
        points.insert({x, y});
    }
  }
  return points;
}

bool isAccessible(
    const aoc::Point &point,
    const std::unordered_set<aoc::Point, aoc::PointHash> &points) {
  int adjacent = 0;
  for (int x = point.x - 1; x <= point.x + 1; ++x) {
    for (int y = point.y - 1; y <= point.y + 1; ++y) {
      if ((x != point.x || y != point.y) && points.contains({x, y})) {
        ++adjacent;
        if (adjacent >= 4)
          return false;
      }
    }
  }
  return true;
}

std::unordered_set<aoc::Point, aoc::PointHash> removeRolls(const std::unordered_set<aoc::Point, aoc::PointHash> &points) {
  std::unordered_set<aoc::Point, aoc::PointHash> newPoints;
  for (const auto &point : points) {
    if (!isAccessible(point, points)) {
      newPoints.insert(point);
    }
  }
  return newPoints;
}

int solve1(const std::vector<std::string> &input) {
  const auto points = parseInput(input);
  int accessible = 0;
  for (const auto &point : points) {
    if (isAccessible(point, points)) {
      ++accessible;
    }
  }
  return accessible;
}

uint64_t solve2(const std::vector<std::string> &input) {
  const auto startPoints = parseInput(input);
  auto points = startPoints;
  uint64_t removed = 0;
  while (true) {
    const auto newPoints = removeRolls(points);
    if (newPoints.size() == points.size()) return removed;
    removed += points.size() - newPoints.size();
    points = newPoints;
  }
}

int main() {
  const std::vector<std::string> input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
