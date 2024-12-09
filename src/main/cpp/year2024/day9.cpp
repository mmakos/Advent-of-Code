#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 9;
constexpr int year = 2024;

std::vector<uint8_t> parseInput(const aoc::input &input) {
  const std::string &in = input[0];
  std::vector<uint8_t> out;
  for (const auto c: in) out.push_back(c - '0');
  return out;
}

uint64_t solve1(const aoc::input &inputStr) {
  auto input = parseInput(inputStr);
  uint64_t sum = 0;

  for (int inputIdx = 0, outputIdx = 0; inputIdx < input.size(); ++inputIdx) {
    for (int i = 0; i < input[inputIdx]; ++i) {
      sum += inputIdx / 2 * outputIdx++;
    }
    ++inputIdx;
    for (int i = 0; i < input[inputIdx] && inputIdx < input.size() - 2; ++i) {
      if (input[input.size() - 1] <= 0) input.erase(input.end() - 2, input.end());
      --input[input.size() - 1];
      sum += input.size() / 2 * outputIdx++;
    }
  }

  return sum;
}

uint64_t solve2(const aoc::input &inputStr) {
  auto input = parseInput(inputStr);
  std::vector<uint32_t> indexes;
  for (int i = 0, j = 0; i < input.size(); ++i) {
    indexes.push_back(j);
    j += input[i];
  }
  uint64_t sum = 0;

  for (auto lastIdx = input.size() - 1; lastIdx > 0; lastIdx -= 2) {
    const auto last = input[lastIdx];
    for (int i = 1; i < lastIdx; i += 2) {
      if (input[i] >= last) {
        input[i] -= last;
        input[lastIdx] = 0;
        const uint64_t startIdx = indexes[i];
        indexes[i] += last;
        sum += (last * (startIdx * 2 + last - 1) * lastIdx) / 4;
        break;
      }
    }
  }

  for (int i = 0; i < input.size(); i += 2) {
    const auto value = input[i];
    const uint64_t idx = indexes[i];
    sum += (value * (idx * 2 + value - 1) * i) / 4;
  }

  return sum;
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
