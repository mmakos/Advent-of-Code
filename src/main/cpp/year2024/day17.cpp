#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 17;
constexpr int year = 2024;

// ============= SOLUTIONS OPTIMIZED FOR MY PARTICULAR INPUT ==============
uint64_t solve1(uint64_t a) {
  uint64_t result = 0;
  for (; a != 0; a /= 8) {
    const uint64_t eval = (a & 7 ^ 1 ^ 5 ^ a / (1 << (a & 7 ^ 1))) & 7;
    result = result << 3 | eval;
  }
  return result;
}

uint64_t solve2(const int i = 15, const uint64_t a = 0) {
  for (int j = 0; j < 8; ++j) {
    constexpr uint64_t expected = 02411751540035530; // our program (al 3-bits written into single int)
    const uint64_t newA = a * 8 + j;
    const uint64_t result = solve1(newA);
    if (result == expected) return newA;
    if (result == (expected & (1ULL << 3 * (16 - i)) - 1)) {
      const uint64_t nextResult = solve2(i - 1, newA);
      if (nextResult != 0) return nextResult;
    }
  }
  return 0;
}

std::string asOutput(const uint64_t result) {
  bool first = true;
  std::stringstream s;
  for (int i = 60; i >= 0; i -= 3) {
    int j = result >> i & 7;
    if (first && j == 0) continue;
    if (!first) s << ',';
    s << j;
    first = false;
  }
  return s.str();
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << asOutput(solve1(64196994)) << std::endl;
  std::cout << "TASK 2: " << solve2() << std::endl;
}
