#ifndef RANGE_H
#define RANGE_H

#include <algorithm>
#include <cstdint>

namespace aoc {
template <typename T = uint64_t> struct Range {
  T begin;
  T end;

  bool operator==(const Range &) const = default;

  void operator+=(const Range &other) {
    begin = std::min(begin, other.begin);
    end = std::max(end, other.end);
  }

  bool overlaps(const Range &other) const {
    return (other.end >= begin && other.end <= end) ||
           (other.begin >= begin && other.begin <= end);
  }

  bool contains(const T value) const { return value >= begin && value <= end; }

  T size() const { return end - begin + 1; }

  friend std::size_t hash_value(const Range &obj) {
    std::size_t seed = 0x386BB2E6;
    seed ^= (seed << 6) + (seed >> 2) + 0x6CC63F50 +
            static_cast<std::size_t>(obj.begin);
    seed ^= (seed << 6) + (seed >> 2) + 0x273FF9B2 +
            static_cast<std::size_t>(obj.end);
    return seed;
  }
};

template <typename T = uint64_t> struct RangeHash {
  std::size_t operator()(const Range<T> &range) const {
    return hash_value(range);
  }
};

template <typename T>
std::vector<Range<T>> mergeRanges(const std::vector<Range<T>> &ranges) {
  if (ranges.empty())
    return {};

  auto sortedRanges = ranges;
  std::sort(
      sortedRanges.begin(), sortedRanges.end(),
      [](const Range<T> &a, const Range<T> &b) { return a.begin < b.begin; });
  std::vector<Range<T>> merged;
  merged.push_back(sortedRanges[0]);

  for (size_t i = 1; i < sortedRanges.size(); ++i) {
    Range<T> &last = merged.back();
    const Range<T> &curr = sortedRanges[i];

    if (last.overlaps(curr))
      last += curr;
    else
      merged.push_back(curr);
  }
  return merged;
}
} // namespace aoc

#endif // RANGE_H
