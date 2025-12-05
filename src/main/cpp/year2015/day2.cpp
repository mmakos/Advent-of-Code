#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 2;
constexpr int year = 2015;

int calcPaper(const std::vector<int> &box) {
  return 2 * (box[0] * box[1] + box[1] * box[2] + box[2] * box[0]) + box[0] * box[1];
}

int calcRibbon(const std::vector<int> &box) {
  return box[0] * box[1] * box[2] + 2 * (box[0] + box[1]);
}

std::pair<int, int> solve(std::vector<std::vector<int>> &input) {
  int paper = 0;
  int ribbon = 0;
  for (auto &box : input) {
    std::ranges::sort(box);
    paper += calcPaper(box);
    ribbon += calcRibbon(box);
  }
  return {paper, ribbon};
}

int main() {
  std::vector<std::vector<int>> input = aoc::readNestedInts(year, day, std::regex("x"));
  const auto [solution1, solution2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
