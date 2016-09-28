import re
import operator
import sys

DICT_START = "<dictionary>"
DICT_END = "</dictionary>"
ITEM_START="<item>"
ITEM_END="</item>"
WORD_START = "<word>"
WORD_END = "</word>"
COUNT_START="<count>"
COUNT_END="</count>"


delimiters = ".", " ", "!", "?", ",", ":", '"', "'", ";", "\t", "\n", "�", "(", ")", "-"
regex = "|".join(map(re.escape, delimiters))


class Dictionary:

    def read_file(self, file_name):
        with open(file_name) as f:
            return f.readlines()


    def add_words_to_dictionary(self, list_of_lines, dictionary):
        for line in list_of_lines:
            words = re.split(regex, line)
            for word in words:
                if len(word) > 0 and "&" not in word and "#" not in word and "$" not in word:
                    word = word.lower()
                    if word in dictionary:
                        dictionary[word] = dictionary[word] + 1;
                    else:
                        if re.match("[a-zA-Z]+", word):
                            dictionary[word] = 1;

    def write_list_to_file(self, list, file_name):
        file = open(file_name, "w")
        file.write(DICT_START + "\n")
        for key in list:
            file.write("\t" + ITEM_START + "\n")
            file.write("\t\t" + WORD_START + key[0] + WORD_END + "\n")
            file.write("\t\t" + COUNT_START + str(key[1]) + COUNT_END + "\n")
            file.write("\t" + ITEM_END + "\n")
        file.write(DICT_END + "\n")
        file.close()

    def write_dict_to_file(self, dict, file_name):
        file = open(file_name, "w")
        file.write(DICT_START + "\n")
        for key, value in dict.items():
            file.write("\t" + ITEM_START + "\n")
            file.write("\t\t" + WORD_START + key + WORD_END + "\n")
            file.write("\t\t" + COUNT_START + str(value) + COUNT_END + "\n")
            file.write("\t" + ITEM_END + "\n")
        file.write(DICT_END + "\n")
        file.close()

    def read_dict_from_file(self, file_name):
        d = {}
        with open(file_name) as f:
            for line in f:
                (key, val) = line.split()
                d[key] = val
        return d


dictionary = {}
d = Dictionary()
l = len(sys.argv)
print(l)
i = 1
file_names = []
for j in range(l):
    file_name = ""
    if (sys.argv[j].startswith("'")):
        while not sys.argv[j].endswith("'"):
            file_name = file_name + sys.argv[j] + " ";
            j = j + 1
        file_name = file_name + sys.argv[j];
        file_name = file_name[1:-1]
        file_names.append(file_name)
l = len(file_names)
while i < l:
    lines = d.read_file(file_names[i - 1])
    d.add_words_to_dictionary(lines, dictionary)
    i = i + 1
d.write_dict_to_file(dictionary,file_names[i - 1])

#   Rand Ayn. Atlas Shrugged - royallib.ru.txt
#   Rand Ayn. Atlas Shrugged - royallib.ru.txt