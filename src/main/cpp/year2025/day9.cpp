#include <iostream>
#include <point.h>
#include <vector>

#include <utils.h>

constexpr int day = 9;
constexpr int year = 2025;

typedef aoc::Point<int64_t> Point;
typedef std::pair<Point, Point> Line;

int64_t distance(const Point &point1, const Point &point2) {
  return (std::abs(point1.x - point2.x) + 1ull) * (std::abs(point1.y - point2.y) + 1ull);
}

bool containsLine(const Point &point1, const Point &point2, const Line &line) {
  const int64_t minX = std::min(point1.x, point2.x);
  const int64_t maxX = std::max(point1.x, point2.x);
  const int64_t minY = std::min(point1.y, point2.y);
  const int64_t maxY = std::max(point1.y, point2.y);

  const auto &[a, b] = line;

  if (a.x == b.x) {
    if (!(a.x > minX && a.x < maxX)) return false;
    return std::min(static_cast<int64_t>(std::max(a.y, b.y)), maxY) > std::max(static_cast<int64_t>(std::min(a.y, b.y)), minY);
  }
  if (!(a.y > minY && a.y < maxY)) return false;
  return std::min(static_cast<int64_t>(std::max(a.x, b.x)), maxX) > std::max(static_cast<int64_t>(std::min(a.x, b.x)), minX);
}

bool containsPoint(const Point &point1, const Point &point2, const Point &point) {
  return point.x > std::min(point1.x, point2.x) &&
    point.x < std::max(point1.x, point2.x) &&
    point.y > std::min(point1.y, point2.y) &&
    point.y < std::max(point1.y, point2.y);
}

uint64_t solve1(const std::vector<std::vector<int>> &input) {
  std::vector<Point> points;
  for (const auto &row : input) {
    points.emplace_back(row[0], row[1]);
  }

  std::vector<std::tuple<Point, Point, int64_t>> pairs;

  for (int i = 0; i < points.size(); ++i) {
    for (int j = i + 1; j < points.size(); ++j) {
      pairs.emplace_back(points[i], points[j], distance(points[i], points[j]));
    }
  }

  return std::get<2>(std::ranges::max(pairs, [](const auto &p1, const auto &p2) { return std::get<2>(p1) < std::get<2>(p2); }));
}

int solve2(const std::vector<std::vector<int>> &input) {
  std::vector<Point> points;
  for (const auto &row : input) {
    points.emplace_back(row[0], row[1]);
  }
  std::vector<Line> lines;
  for (int i = 0; i <= points.size(); ++i) {
    lines.emplace_back(points[i], points[(i + 1) % points.size()]);
  }

  std::vector<std::tuple<Point, Point, int64_t>> pairs;

  for (int i = 0; i < points.size(); ++i) {
    for (int j = i + 1; j < points.size(); ++j) {
      pairs.emplace_back(points[i], points[j], distance(points[i], points[j]));
    }
  }
  std::ranges::sort(pairs, [](const auto &p1, const auto &p2) { return std::get<2>(p1) > std::get<2>(p2); });

  for (const auto &pair : pairs) {
    bool broken = false;
    for (const auto &line : lines) {
      if (containsLine(std::get<0>(pair), std::get<1>(pair), line)) {
        broken = true;
        break;
      }
    }
    if (!broken) return std::get<2>(pair);
  }
}

int main() {
  const std::vector<std::vector<int>> input = aoc::readNestedInts(year, day, std::regex(","));

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
