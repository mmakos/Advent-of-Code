#include <iostream>
#include <point.h>
#include <unordered_set>
#include <vector>

#include <utils.h>

namespace aoc {
  struct Point;
}

constexpr int day = 20;
constexpr int year = 2024;
constexpr std::array<aoc::Point, 4> dirs = {{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}};

std::vector<aoc::Point> parsePath(const aoc::input &input) {
  std::unordered_set<aoc::Point, aoc::PointHash> points;
  aoc::Point start = {0, 0};
  aoc::Point end = {0, 0};
  for (int y = 0; y < input.size(); ++y) {
    for (int x = 0; x < input[0].size(); ++x) {
      const char c = input[y][x];
      if (c == 'S') start = {x, y};
      else if (c != '#') {
        points.insert({x, y});
        if (c == 'E') end = {x, y};
      }
    }
  }

  std::vector out = {start};
  aoc::Point position = start;
  while (position != end) {
    for (int i = 0; i < 4; ++i) {
      const auto next = position + dirs[i];
      if (points.contains(next)) {
        out.push_back(next);
        points.erase(next);
        position = next;
        break;
      }
    }
  }

  return out;
}

std::pair<int, int> solve(const aoc::input &input) {
  const auto points = parsePath(input);
  int task1 = 0;
  int task2 = 0;
  for (int i = 0; i < points.size(); ++i) {
    for (int j = i + 100; j < points.size(); ++j) {
      const auto [sx, sy] = points[i];
      const auto [ex, ey] = points[j];
      const int distance = std::abs(ex - sx) + std::abs(ey - sy);
      if (j - i - distance >= 100) {
        if (distance == 2) ++task1;
        if (distance <= 20) ++task2;
      }
    }
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
