#include <iostream>
#include <point.h>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 14;
constexpr int year = 2024;

constexpr aoc::Point dimension = {101, 103};

std::vector<std::pair<aoc::Point, aoc::Point>> parseInput(const aoc::input &input) {
  std::vector<std::pair<aoc::Point, aoc::Point>> robots;
  for (const auto &line: input) {
    const std::vector<int> params = aoc::findAll<int>(line, std::regex("-?\\d+"), &aoc::parseInt);
    robots.push_back({{params[0], params[1]}, {params[2], params[3]}});
  }
  return robots;
}

aoc::Point getEndPosition(const aoc::Point &start, const aoc::Point &motion, const int seconds = 100) {
  return ((start + motion * seconds) % dimension + dimension) % dimension;
}

int getQuadrant(const aoc::Point &start, const aoc::Point &motion) {
  const aoc::Point end = getEndPosition(start, motion);
  if (end.x == dimension.x / 2 || end.y == dimension.y / 2) return 0;
  if (end.x < dimension.x / 2) return end.y < dimension.y / 2 ? 1 : 2;
  return end.y < dimension.y / 2 ? 3 : 4;
}

int solve1(const std::vector<std::pair<aoc::Point, aoc::Point>> &robots) {
  int result[] = {0, 0, 0, 0, 0};
  for (const auto &[position, motion]: robots) {
    ++result[getQuadrant(position, motion)];
  }
  return result[1] * result[2] * result[3] * result[4];
}

int solve2(const std::vector<std::pair<aoc::Point, aoc::Point>> &robots) {
  for (int i = 1; ; ++i) {
    std::unordered_set<aoc::Point, aoc::PointHash> positions;
    for (const auto &[position, motion]: robots) {
      const aoc::Point endPosition = getEndPosition(position, motion, i);
      if (!positions.insert(endPosition).second) break;
    }
    if (positions.size() == robots.size()) return i;
  }
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  const std::vector<std::pair<aoc::Point, aoc::Point>> robots = parseInput(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(robots) << std::endl;
  std::cout << "TASK 2: " << solve2(robots) << std::endl;
}
