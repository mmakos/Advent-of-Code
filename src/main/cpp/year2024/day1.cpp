#include <iostream>
#include <vector>

#include <ranges>
#include <utils.h>
#include <algorithm>

constexpr int day = 1;
constexpr int year = 2024;

std::pair<std::vector<int>, std::vector<int>> parseInput(const std::vector<std::string> &input) {
  std::vector<int> first;
  std::vector<int> second;
  const std::regex re(R"(\s+)");
  for (const auto &s : input) {
    auto split = aoc::split(s, re);
    first.push_back(std::stoi(split[0]));
    second.push_back(std::stoi(split[1]));
  }
  return std::make_pair(first, second);
}


int solve1(const std::vector<std::string> &input) {
  auto [first, second] = parseInput(input);
  std::ranges::sort(first);
  std::ranges::sort(second);
  int sum = 0;
  for (const auto [a, b] : std::views::zip(first, second)) {
    sum += std::abs(a - b);
  }
  return sum;
}

long long solve2(const std::vector<std::string> &input) {
  auto [first, second] = parseInput(input);

  long long sum = 0;
  for (const auto a : first) {
    sum += a * std::ranges::count(second, a);
  }
  return sum;
}

int main() {
  const std::vector<std::string> input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
