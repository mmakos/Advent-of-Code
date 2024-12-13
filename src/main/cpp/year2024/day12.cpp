#include <iostream>
#include <point.h>
#include <queue>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 12;
constexpr int year = 2024;

using Points = std::unordered_map<aoc::Point, uint8_t, aoc::PointHash>;
// All 8 directions from top - clockwise
const std::vector<aoc::Point> dirs = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

std::unordered_map<aoc::Point, uint8_t, aoc::PointHash> parseInput(const aoc::input &input) {
  std::unordered_map<aoc::Point, uint8_t, aoc::PointHash> points;

  for (int y = 0; y < input.size(); ++y) {
    for (int x = 0; x < input[0].size(); ++x) {
      points[{x, y}] = input[y][x];
    }
  }
  return points;
}

std::tuple<int, int, int> getPerimeterAndArea(std::unordered_map<aoc::Point, uint8_t, aoc::PointHash> &points, const aoc::Point &startPoint, const uint8_t veg) {
  int perimeter = 0;
  int sides = 0;
  int area = 0;

  std::unordered_set<aoc::Point, aoc::PointHash> visited;
  std::queue<aoc::Point> queue;
  queue.push(startPoint);
  visited.insert(startPoint);

  while (!queue.empty()) {
    const auto &point = queue.front();
    queue.pop();
    ++area;
    std::vector<bool> adjacentPlants;
    for (int i = 0; i < 8; ++i) {
      const auto it = points.find(point + dirs[i]);
      adjacentPlants.push_back(it != points.end() && it->second == veg);
    }

    for (int i = 0; i < 8; i += 2) {
      if ((!adjacentPlants[i] && !adjacentPlants[(i + 2) % 8]) || (adjacentPlants[i] && !adjacentPlants[i + 1] && adjacentPlants[(i + 2) % 8])) {
        ++sides;
      }
      if (!adjacentPlants[i]) ++perimeter;
      else if (const auto newPoint = point + dirs[i]; !visited.contains(newPoint)) {
        queue.push(point + dirs[i]);
        visited.insert(point + dirs[i]);
      }
    }
  }
  for (const auto &p: visited) {
    points.erase(p);
  }

  return {perimeter, sides, area};
}

std::pair<int, int> solve(Points &points) {
  int sum1 = 0;
  int sum2 = 0;
  while (!points.empty()) {
    const auto &[point, veg] = *points.begin();
    const auto [p, s, a] = getPerimeterAndArea(points, point, veg);
    sum1 += p * a;
    sum2 += s * a;
  }

  return {sum1, sum2};
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  auto points = parseInput(input);
  auto [solution1, solution2] = solve(points);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
