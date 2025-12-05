#include <iostream>
#include <utils.h>

constexpr int day = 1;
constexpr int year = 2015;

std::pair<int, int> solve(const std::string &input) {
  int sum = 0;
  int firstNeg = 0;
  for (int i = 0; i < input.size(); ++i) {
    sum += input[i] == '(' ? 1 : -1;
    if (firstNeg == 0 && sum < 0) {
      firstNeg = i + 1;
    }
  }
  return {sum, firstNeg};
}

int main() {
  uint32_t n;

  printf("%u", 0xffffffff / 4);
  scanf("%u", &n);
  printf("%u", n * (int32_t) sizeof(uint32_t));

  // const std::string input = aoc::readInput<std::string>(year, day)[0];
  // const auto [solution1, solution2] = solve(input);

  // std::cout << "Advent of code " << year << ", day " << day << std::endl;
  // std::cout << "TASK 1: " << solution1 << std::endl;
  // std::cout << "TASK 2: " << solution2 << std::endl;

  // uint64_t n, v, PoC = 0;
  // int64_t k;
  // printf("%llu\n", sizeof(uint64_t));
  // printf("PoC at %llu\n", (uintptr_t) &PoC);
  // printf("n: ");
  // scanf("%llu", &n);
  // uint64_t* tab = reinterpret_cast<uint64_t*>(malloc(n * 8));
  // if (tab == 0) return 1;
  //
  // printf("Success: (%llu)\n", (uintptr_t) tab);
  // printf("%lld\n", ((intptr_t) &PoC - (intptr_t) tab) / (int64_t)sizeof(uint64_t));
  //
  // printf("tab[k]=v; k,v: ");
  // scanf("%lld", &k);
  // scanf("%llu", &v);
  //
  // printf("K  = %lld\n", k);
  // printf("TAB + K: %llu\n", (uintptr_t)(tab) + k * sizeof(uint64_t));
  // printf("TAB[K]: %llu\n", tab[k]);
  //
  // printf("POC: %llu\n", PoC);
  // if (k < n) tab[k] = v;
  // else puts("k >= n - FAIL");
  // if (PoC == 1337)
  //   puts("Victory");
  // int a[12];
  // unsigned int poc = 0;
  // std::cout << &poc << std::endl;
  // std::cout << a << std::endl;
  //
  // a[-1] = 1234;
  //
  // std::cout << poc << std::endl;
}
