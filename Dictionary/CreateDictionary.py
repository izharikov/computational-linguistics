import re
import operator


def read_file(file_name):
    with open(file_name) as f:
        return f.readlines()


delimiters = ".", " ", "!", "?", ",", ":", '"', "'", ";", "\t", "\n", "�", "(", ")"
regex = "|".join(map(re.escape, delimiters))


def add_words_to_dictionary(list_of_lines, dictionary):
    for line in list_of_lines:
        words = re.split(regex, line)
        for word in words:
            if len(word) > 0 and "&" not in word and "#" not in word and "$" not in word:
                word = word.lower()
                if word in dictionary:
                    dictionary[word] = dictionary[word] + 1;
                else:
                    dictionary[word] = 1;


def write_list_to_file(list, file_name):
    file = open(file_name, "w")
    for key in list:
        file.write("%s\n" % str(key))
    file.close()


dictionary = {}
lines = read_file('Rand Ayn. Atlas Shrugged - royallib.ru.txt')
add_words_to_dictionary(lines, dictionary)
lines = read_file('Tolkien J.. The Lord of the Rings - royallib.ru.txt')
add_words_to_dictionary(lines, dictionary)

sorted_by_count = sorted(dictionary.items(), key=operator.itemgetter(1), reverse=True)
write_list_to_file(sorted_by_count, 'count_sorting.txt')

sorted_alphabet = sorted(dictionary.items(), key=operator.itemgetter(0))
write_list_to_file(sorted_alphabet, 'alphabet_sorting.txt')