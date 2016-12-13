import re
import operator
import sys

DICT_START = "<table>"
DICT_END = "</table>"
ITEM_START="<tr>"
ITEM_END="</tr>"
WORD_START = "<td>"
WORD_END = "</td>"
COUNT_START="<td>"
COUNT_END="</td>"


delimiters = ".", " ", "!", "?", ",", ":", '"', "'", ";", "\t", "\n", "(", ")", "-"
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
        # file.write("DROP TABLE DICTIONARY_TBL;\n")
        # file.write("CREATE TABLE DICTIONARY_TBL (id int(7) UNSIGNED AUTO_INCREMENT PRIMARY KEY, word varchar(32) not null, word_count int (10) not null);" + "\n");

        #file.write(DICT_START + "\n")
        for key, value in dict.items():
            file.write("INSERT INTO DICTIONARY_TBL (word, word_count) VALUES ");
            file.write("(\"")
            file.write(key +"\",")
            file.write(" " + str(value))
            file.write(")")
            file.write(" ON DUPLICATE KEY UPDATE word_count = word_count + VALUES(word_count);\n")
        #file.write(DICT_END + "\n")
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
        print(file_name)
l = len(file_names)

print("l: ", l);

while i < l:
    lines = d.read_file(file_names[i - 1])
    d.add_words_to_dictionary(lines, dictionary)
    i = i + 1
d.write_dict_to_file(dictionary,file_names[i - 1])

#   Rand Ayn. Atlas Shrugged - royallib.ru.txt
#   Rand Ayn. Atlas Shrugged - royallib.ru.txt
