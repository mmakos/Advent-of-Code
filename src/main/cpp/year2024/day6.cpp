#include <iostream>
#include <vector>
#include <unordered_set>

#include <utils.h>
#include <unordered_map>

constexpr int day = 6;
constexpr int year = 2024;

struct Point {
  int x;
  int y;

  bool operator==(const Point &other) const {
    return x == other.x && y == other.y;
  }

  void rotate() {
    int temp = x;
    x = -y;
    y = temp;
  }

  void add(Point &p) {
    x += p.x;
    y += p.y;
  }

  void sub(Point &p) {
    x -= p.x;
    y -= p.y;
  }

  [[nodiscard]] bool inBounds(int width, int height) const {
    return x >= 0 && y >= 0 && x < width && y < height;
  }
};

struct PointHash {
  std::size_t operator()(const Point &p) const {
    return std::hash<int>()(p.x) ^ (std::hash<int>()(p.y) << 1);
  }
};

std::pair<std::unordered_set<Point, PointHash>, Point> parseInput(const aoc::input &input) {
  std::unordered_set<Point, PointHash> points;

  Point startPosition(0, 0);
  for (int y = 0; y < input.size(); ++y) {
    for (int x = 0; x < input[0].size(); ++x) {
      if (input[y][x] == '#') points.insert(Point(x, y));
      else if (input[y][x] == '^') startPosition = Point(x, y);
    }
  }
  return std::make_pair(points, startPosition);
}

std::unordered_map<Point, int, PointHash> solve(const std::unordered_set<Point, PointHash> &points, Point pos, int width, int height) {
  Point move(0, -1);
  int dir = 1;

  std::unordered_map<Point, int, PointHash> visited = {{pos, dir}};

  while (true) {
    pos.add(move);
    if (!pos.inBounds(width, height)) break;
    auto it = visited.find(pos);
    if (it != visited.end() && (it->second & dir)) {
      return {};
    }
    if (points.contains(pos)) {
      pos.sub(move);
      move.rotate();
      dir <<= 1;
      if (dir > 0b1000) dir = 1;
    } else {
      visited[pos] |= dir;
    }
  }

  return visited;
}

std::pair<int, int> solve12(const aoc::input &input) {
  int width = (int) input[0].size();
  int height = (int) input.size();
  auto [points, pos] = parseInput(input);
  auto solution1 = solve(points, pos, width, height);

  auto obstaclePositions = solution1;
  obstaclePositions.erase(pos);
  int solution2 = 0;
  for (auto &p: obstaclePositions) {
    points.insert(p.first);
    if (solve(points, pos, width, height).empty()) ++solution2;
    points.erase(p.first);
  }
  return std::make_pair((int) solution1.size(), solution2);
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  auto [solution1, solution2] = solve12(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
