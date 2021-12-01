def first_star(numbers):
    return sum(1 for (x, y) in list(zip(numbers, numbers[1:])) if y > x)


def second_star(numbers):
    sliding_windows = list(zip(numbers, numbers[1:], numbers[2:]))
    sums = [x + y + z for (x, y, z) in sliding_windows]
    return first_star(sums)


my_input = open('resources/day01.txt', 'r').read()
numbers = list(map(int, my_input.split("\n")))

print(first_star(numbers))
print(second_star(numbers))