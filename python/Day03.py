def first_star(binary_numbers):
    transposed = list(zip(*binary_numbers))
    gamma = ""

    for bits in transposed:
        num_of_1 = sum([1 for bit in bits if bit == '1'])
        gamma = gamma + ("1" if 2 * num_of_1 > len(bits) else "0")

    gamma = int(gamma, 2)
    epsilon = gamma ^ 0b111111111111
    
    return gamma * epsilon

my_input = open('resources/day03.txt', 'r').read()
binary_numbers = my_input.split("\n")

print(first_star(binary_numbers))