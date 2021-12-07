from statistics import median, mean

def first_star(crabs_positions):
    nearest = median(crabs_positions)
    return sum([abs(nearest - position) for position in crabs_positions])

def second_star(crabs_positions):
    nearest = int(mean(crabs_positions))
    fuel_cost = [sum(range(1, abs(nearest - position) + 1)) for position in crabs_positions]
    return sum(fuel_cost)


my_input = open('resources/day07.txt', 'r').read()
numbers = list(map(int, my_input.split(",")))

print(first_star(numbers))
print(second_star(numbers))