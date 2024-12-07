#include <iostream>
#include <vector>
#include <cmath>

#include <utils.h>

constexpr int day = 7;
constexpr int year = 2024;

unsigned long long sum(const std::vector<int> &numbers, int operators, const int base) {
  unsigned long long sum = numbers[0];
  int oper = operators % base;
  for (int i = 0; i < numbers.size() - 1; ++i) {
    if (oper == 0) {
      sum *= numbers[i + 1];
    } else if (oper == 1) {
      sum += numbers[i + 1];
    } else {
      sum *= std::pow(10, static_cast<int>(std::log10(numbers[i + 1]) + 1));
      sum += numbers[i + 1];
    }
    operators /= base;
    oper = operators % base;
  }
  return sum;
}

bool canSolve(const unsigned long long number, const std::vector<int> &numbers, const int base) {
  const int iterations = std::pow(base, numbers.size() - 1);
  for (int i = 0; i < iterations; ++i) {
    if (sum(numbers, i, base) == number) return true;
  }
  return false;
}

std::pair<unsigned long long, unsigned long long> solve(const aoc::input &input) {
  unsigned long long sum2 = 0;
  unsigned long long sum3 = 0;
  for (auto &line: input) {
    auto split = aoc::split(line, std::regex(": "));
    auto split2 = aoc::split(split[1], std::regex(" "));
    std::vector<int> numbers;
    for (auto &s: split2) {
      numbers.push_back(std::stoi(s));
    }

    if (const unsigned long long number = std::stoull(split[0]); canSolve(number, numbers, 2)) {
      sum2 += number;
    } else if (canSolve(number, numbers, 3)) {
      sum3 += number;
    }
  }

  return std::make_pair(sum2, sum2 + sum3);
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  auto [solution1, solution2] = solve(input);
  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
