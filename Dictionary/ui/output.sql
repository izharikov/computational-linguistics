INSERT INTO DICTIONARY_TBL (word, word_count) VALUES ("grapple", 1) ON DUPLICATE KEY UPDATE word_count = word_count + VALUES(word_count);
INSERT INTO DICTIONARY_TBL (word, word_count) VALUES ("apple", 3) ON DUPLICATE KEY UPDATE word_count = word_count + VALUES(word_count);
