#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 4;
constexpr int year = 2024;


bool isXmasRow(const aoc::input &input, const int row, const int col) {
  const auto sub = input[row].substr(col, 4);
  return sub == "XMAS" || sub == "SAMX";
}

bool isXmasCol(const aoc::input &input, const int row, const int col) {
  return (input[row][col] == 'X' && input[row + 1][col] == 'M' && input[row + 2][col] == 'A' && input[row + 3][col] == 'S') ||
         (input[row][col] == 'S' && input[row + 1][col] == 'A' && input[row + 2][col] == 'M' && input[row + 3][col] == 'X');
}

bool isXmasRightSlant(const aoc::input &input, const int row, const int col) {
  return (input[row][col] == 'X' && input[row + 1][col + 1] == 'M' && input[row + 2][col + 2] == 'A' && input[row + 3][col + 3] == 'S') ||
         (input[row][col] == 'S' && input[row + 1][col + 1] == 'A' && input[row + 2][col + 2] == 'M' && input[row + 3][col + 3] == 'X');
}

bool isXmasLeftSlant(const aoc::input &input, const int row, const int col) {
  return (input[row][col + 3] == 'X' && input[row + 1][col + 2] == 'M' && input[row + 2][col + 1] == 'A' && input[row + 3][col] == 'S') ||
         (input[row][col + 3] == 'S' && input[row + 1][col + 2] == 'A' && input[row + 2][col + 1] == 'M' && input[row + 3][col] == 'X');
}

bool isXmasX(const aoc::input &input, const int row, const int col) {
  return input[row + 1][col + 1] == 'A' &&
         ((input[row][col] == 'M' && input[row + 2][col + 2] == 'S') || (input[row][col] == 'S' && input[row + 2][col + 2] == 'M')) &&
         ((input[row][col + 2] == 'M' && input[row + 2][col] == 'S') || (input[row][col + 2] == 'S' && input[row + 2][col] == 'M'));
}

int solve1(const aoc::input &input) {
  const int rows = static_cast<int>(input.size());
  const int cols = static_cast<int>(input[0].size());
  int sum = 0;
  for (int row = 0; row < input.size(); ++row) {
    for (int col = 0; col < input[0].size(); ++col) {
      if (col <= cols - 4 && isXmasRow(input, row, col)) ++sum;
      if (row <= rows - 4 && isXmasCol(input, row, col)) ++sum;
      if (row <= rows - 4 && col <= cols - 4) {
        if (isXmasLeftSlant(input, row, col)) ++sum;
        if (isXmasRightSlant(input, row, col)) ++sum;
      }
    }
  }
  return sum;
}

int solve2(const aoc::input &input) {
  int sum = 0;
  for (int row = 0; row <= input.size() - 3; ++row) {
    for (int col = 0; col <= input[0].size() - 3; ++col) {
      if (isXmasX(input, row, col)) ++sum;
    }
  }
  return sum;
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
