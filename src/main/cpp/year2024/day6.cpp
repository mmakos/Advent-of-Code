#include <iostream>
#include <vector>
#include <unordered_set>

#include <utils.h>
#include <point.h>
#include <ranges>
#include <unordered_map>

constexpr int day = 6;
constexpr int year = 2024;

std::pair<std::unordered_set<aoc::Point, aoc::PointHash>, aoc::Point> parseInput(const aoc::input &input) {
  std::unordered_set<aoc::Point, aoc::PointHash> points;

  aoc::Point startPosition{0, 0};
  for (int y = 0; y < input.size(); ++y) {
    for (int x = 0; x < input[0].size(); ++x) {
      if (input[y][x] == '#') points.insert({x, y});
      else if (input[y][x] == '^') startPosition = {x, y};
    }
  }
  return {points, startPosition};
}

std::unordered_map<aoc::Point, int, aoc::PointHash> solve(const std::unordered_set<aoc::Point, aoc::PointHash> &points,
                                                          aoc::Point pos, const aoc::Point &upperBounds) {
  constexpr aoc::Point lowerBounds{0, 0};
  aoc::Point move{0, -1};
  int dir = 1;

  std::unordered_map<aoc::Point, int, aoc::PointHash> visited = {{pos, dir}};

  while (true) {
    pos += move;
    if (!(pos >= lowerBounds && pos < upperBounds)) break;
    if (auto it = visited.find(pos); it != visited.end() && (it->second & dir)) {
      return {};
    }
    if (points.contains(pos)) {
      pos -= move;
      move.rotateClockwise();
      dir <<= 1;
      if (dir > 0b1000) dir = 1;
    } else {
      visited[pos] |= dir;
    }
  }

  return visited;
}

std::pair<int, int> solve12(const aoc::input &input) {
  const aoc::Point upperBounds{static_cast<int>(input[0].size()), static_cast<int>(input.size())};
  auto [points, pos] = parseInput(input);
  const auto solution1 = solve(points, pos, upperBounds);

  auto obstaclePositions = solution1;
  obstaclePositions.erase(pos);
  int solution2 = 0;
  for (auto &p: std::views::keys(obstaclePositions)) {
    points.insert(p);
    if (solve(points, pos, upperBounds).empty()) ++solution2;
    points.erase(p);
  }
  return {static_cast<int>(solution1.size()), solution2};
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  auto [solution1, solution2] = solve12(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
