package com.dictionary.utils;

/**
 * @author Ihar Zharykau
 */
public class BaseLOB {
//    private static final String[] VB_END = {"ed", "ing"};
//    private static final String[] ADJ_END = {"er", "est"};
//    private static final String[] NOUN_END = {"s", "es", "ing", "er"};
//
//    private static final PosTag LOB_N = PosUtilsDB.byName("N");
//    private static final PosTag LOB_VB = PosUtilsDB.byName("VB");
//    private static final PosTag LOB_ADJ = PosUtilsDB.byName("J");
//    private static final PosTag LOB_ADVERB = PosUtilsDB.byName("R");
//
//    private final DictFactory DF = new DictFactory();
//
//    public void invokeBaseSet() {
//        Collection<DictWord> allWords = DictFactory.cacheMap.values();
//        for (DictWord dictWord : allWords) {
//            check(dictWord, NOUN_END, LOB_N);
//            check(dictWord, VB_END, LOB_VB);
//            check(dictWord, ADJ_END, LOB_ADJ);
//            checkAdverb(dictWord);
//            checkVerb(dictWord);
//            checkNoun(dictWord);
//            if (dictWord.getLobCodes().isEmpty()) {
//                System.out.println("Word " + dictWord.getWord() + " has no LOB code");
//            }
//
//        }
//    }
//
//    private void checkNoun(DictWord dictWord) {
//        if (dictWord.getWord().endsWith("ing") || dictWord.getWord().endsWith("er")) {
//            dictWord.getLobCodes().add(LOB_N);
//        }
//    }
//
//    private void checkVerb(DictWord dictWord) {
//        String str = dictWord.getWord();
//        if (str.endsWith("e")) {
//            DictWord dw = DictionaryUtils.getWordByName(str + "d");
//            if (dw != null) {
//                dictWord.getLobCodes().add(LOB_VB);
//                dw.getLobCodes().add(LOB_VB);
//            }
//
//        } else if (str.length() > 2) {
//            char c = str.charAt(str.length() - 1);
//            char d = str.charAt(str.length() - 2);
//            if ( isVowel(d) && isConsanant(c)){
//                DictWord dw = DictionaryUtils.getWordByName(str + c + "ed");
//                if (dw != null) {
//                    dictWord.getLobCodes().add(LOB_VB);
//                    dw.getLobCodes().add(LOB_VB);
//                }
//            }
//        }
//    }

//    public static boolean isVowel(char c) {
//        String vowels = "aeiouAEIOU";
//        return vowels.contains("" + c);
//    }
//
//    public static boolean isConsanant(char c) {
//        return !isVowel(c);
//    }
//
//    private void checkAdverb(DictWord word) {
//        if (word.getWord().endsWith("ly")) {
//            word.getLobCodes().add(LOB_ADVERB);
//        }
//    }
//
//    private boolean check(DictWord word, String[] baseSuffix, PosTag lobCode) {
//        String str = word.getWord();
//        for (String nounEnd : baseSuffix) {
//            if (str.endsWith(nounEnd)) {
//                int length = str.length() - nounEnd.length();
//                length = length < 0 ? 0 : length;
//                DictWord dictWord = DictionaryUtils.getWordByName(str.substring(0, length));
//                if (dictWord != null) {
//                    dictWord.getLobCodes().add(lobCode);
//                    word.getLobCodes().add(lobCode);
//                    return true;
//                }
//            } else {
//                DictWord dictWord = DictionaryUtils.getWordByName(str + nounEnd);
//                if (dictWord != null) {
//                    dictWord.getLobCodes().add(lobCode);
//                    word.getLobCodes().add(lobCode);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private DictWord getWord(String name) {
//        return DictFactory.cacheMap.get(name);
//    }
}
