#include <iostream>
#include <vector>
#include <algorithm>

#include <utils.h>

constexpr int day = 2;
constexpr int year = 2024;

bool valid(int i1, int i2, int direction) {
  int diff = i2 - i1;
  return std::abs(diff) <= 3 && std::abs(diff) > 0 && (diff > 0 ? 1 : -1) == direction;
}

bool validReport(const std::vector<int> &report) {
  if (!(report[1] - report[0])) return false;
  int direction = (report[1] - report[0]) > 0 ? 1 : -1;

  auto i2 = std::next(report.begin());
  for (auto i1 = report.begin(); valid(*i1, *i2, direction) && i2 != report.end(); ++i1, ++i2);
  return i2 == report.end();
}

bool validReport2(const std::vector<int> &report) {
  if (validReport(report)) return true;
  for (int i = 0; i < report.size(); ++i) {
    auto report2 = report;
    report2.erase(report2.begin() + i);
    if (validReport(report2)) return true;
  }
  return false;
}

long long solve1(const std::vector<std::vector<int>> &reports) {
  return std::ranges::count_if(reports, validReport);
}

long long solve2(const std::vector<std::vector<int>> &reports) {
  return std::ranges::count_if(reports, validReport2);
}

int main() {
  const std::vector<std::vector<int>> input = aoc::readNestedInts(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
