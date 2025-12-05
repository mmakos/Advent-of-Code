#ifndef POINT_H
#define POINT_H
#include <bits/fs_path.h>

namespace aoc {
template <typename T = int> struct Point {
  int x;
  int y;

  bool operator==(const Point &) const = default;

  void rotateClockwise() {
    const int temp = x;
    x = -y;
    y = temp;
  }

  friend Point operator-(const Point &first, const Point &second) {
    return {first.x - second.x, first.y - second.y};
  }

  friend Point operator+(const Point &first, const Point &second) {
    return {first.x + second.x, first.y + second.y};
  }

  friend Point operator*(const Point &first, const Point &second) {
    return {first.x * second.x, first.y * second.y};
  }

  friend Point operator*(const Point &first, const T mul) {
    return {first.x * mul, first.y * mul};
  }

  friend Point operator%(const Point &first, const Point &second) {
    return {first.x % second.x, first.y % second.y};
  }

  void operator+=(const Point &other) {
    x += other.x;
    y += other.y;
  }

  void operator-=(const Point &other) {
    x -= other.x;
    y -= other.y;
  }

  void operator%=(const Point &other) {
    x %= other.x;
    y %= other.y;
  }

  bool operator>=(const Point &other) const {
    return x >= other.x && y >= other.y;
  }

  bool operator>(const Point &other) const {
    return x > other.x && y > other.y;
  }

  bool operator<=(const Point &other) const {
    return x <= other.x && y <= other.y;
  }

  bool operator<(const Point &other) const {
    return x < other.x && y < other.y;
  }

  friend std::size_t hash_value(const Point &obj) {
    std::size_t seed = 0x386BB2E6;
    seed ^= (seed << 6) + (seed >> 2) + 0x6CC63F50 +
            static_cast<std::size_t>(obj.x);
    seed ^= (seed << 6) + (seed >> 2) + 0x273FF9B2 +
            static_cast<std::size_t>(obj.y);
    return seed;
  }
};

template <typename T = int> struct PointHash {
  std::size_t operator()(const Point<T> &p) const { return hash_value(p); }
};
} // namespace aoc

#endif // POINT_H
