#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 13;
constexpr int year = 2024;

int parseInt(const std::string &input) {
  return std::stoi(input);
}

int64_t solve(const int64_t x, const int64_t y, const int64_t ax, const int64_t ay, const int64_t bx, const int64_t by) {
  const int64_t aNumerator = x * by - y * bx;
  const int64_t aDenominator = ax * by - ay * bx;
  if (aNumerator % aDenominator == 0) {
    const int64_t a = aNumerator / aDenominator;
    const int64_t bNominator = x - a * ax;
    if (bNominator % bx == 0) {
      return a * 3 + bNominator / bx;
    }
  }
  return 0;
}

std::pair<int64_t, int64_t> solve12(const aoc::input &input) {
  int64_t sum1 = 0;
  int64_t sum2 = 0;
  for (int64_t i = 0; i < input.size() - 2; i += 4) {
    const auto a = aoc::findAll<int>(input[i], std::regex("\\d+"), &parseInt);
    const auto b = aoc::findAll<int>(input[i + 1], std::regex("\\d+"), &parseInt);
    const auto xy = aoc::findAll<int>(input[i + 2], std::regex("\\d+"), &parseInt);
    sum1 += solve(xy[0], xy[1], a[0], a[1], b[0], b[1]);
    sum2 += solve(xy[0] + 10000000000000LL, xy[1] + 10000000000000LL, a[0], a[1], b[0], b[1]);
  }
  return {sum1, sum2};
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  const auto [solution1, solution2] = solve12(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
