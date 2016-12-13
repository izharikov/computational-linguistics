package com.dictionary.nlp;

import com.db.entity.PosTag;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.*;

/**
 * @author Ihar Zharykau
 */
public class AnnotationResponse implements Serializable {
    private StringBuilder htmlOutput = new StringBuilder();
    private Map<String, Integer> posFrequency;
    private Map<String, Integer> posPairFrequency;
    private Map<String, Integer> wordFrequency;
    private Map<String, Map<String, Integer>> wordAndPos = new HashMap<>();
    private List<String> poses = new ArrayList<>();
    private int countOfSentences = 0;
    private int countOfWords = 0;

    public AnnotationResponse() {
        posFrequency = new HashMap<>();
        posPairFrequency = new HashMap<>();
        wordFrequency = new HashMap<>();
    }

    public void appendToHTMLOutput(String appendString) {
        htmlOutput.append(appendString);
    }

    public void addPosToFreq(String pos) {
        Integer oldValue = 0;
        if (posFrequency.containsKey(pos)) {
            oldValue = posFrequency.get(pos);
        }
        posFrequency.put(pos, oldValue + 1);
    }

    public void addPos(String posOne) {
        poses.add(posOne);
    }

    public void addWord(String word) {
        Integer oldValue = 0;
        if (wordFrequency.containsKey(word)) {
            oldValue = wordFrequency.get(word);
        }
        wordFrequency.put(word, oldValue + 1);
    }

    public String getHtmlOutput() {
        return htmlOutput.toString();
    }

    public Map<String, Integer> getPosFrequency() {
        return posFrequency;
    }

    public Map<String, Integer> getPosPairFrequency() {
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < poses.size() - 1; i++) {
            String pair = poses.get(i) + "_" + poses.get(i + 1);
            int value = 1;
            if (result.containsKey(pair)) {
                value = result.get(pair) + 1;
            }
            result.put(pair, value);
        }
        return result;
    }

    public int getCountOfWords() {
        return countOfWords;
    }

    public void setCountOfWords(int countOfWords) {
        this.countOfWords = countOfWords;
    }

    public int getCountOfSentences() {
        return countOfSentences;
    }

    public void setCountOfSentences(int countOfSentences) {
        this.countOfSentences = countOfSentences;
    }

    public void addCountOfWords(int count) {
        countOfWords += count;
    }

    public Map<String, Integer> getWordFrequency() {
        return wordFrequency;
    }

    public Map<String, Map<String, Integer>> getWordAndPos() {
        return wordAndPos;
    }

    public void addWordAndPos(String word, String pos) {
        if (!wordAndPos.containsKey(word)) {
            wordAndPos.put(word, new HashMap<>());
        }
        Map<String, Integer> poses = wordAndPos.get(word);
        Integer oldValue = 0;
        if (poses.containsKey(pos)) {
            oldValue = poses.get(pos);
        }
        poses.put(pos, oldValue + 1);
    }

    private Map<String, PosTag> posTags;

    public Map<String, PosTag> getPosTags() {
        return posTags;
    }

    public void setPosTags(Map<String, PosTag> posTags) {
        this.posTags = posTags;
    }

    public void changePosForWord(String word, String pos, String oldPos, int wordNo) {
        // increase for new pos and decrease oldPos
        word = word.toLowerCase();
        Map<String, Integer> wordStat = wordAndPos.get(word);
        if (wordStat == null) {
            wordStat = new HashMap<>();
        }
        int value = 0;
        if (wordStat.containsKey(oldPos)) {
            value = wordStat.get(oldPos) - 1;
        }
        wordStat.put(oldPos, value);
        if (wordStat.get(oldPos) == 0) {
            wordStat.remove(oldPos);
        }
        value = 1;
        if (wordStat.containsKey(pos)) {
            value = wordStat.get(pos) + 1;
        }
        wordStat.put(pos, value);
        // decrease pos freq
        Map<String, Integer> posFreq = getPosFrequency();
        posFreq.put(oldPos, posFreq.get(oldPos) - 1);
        value = 1;
        if (posFreq.containsKey(pos)) {
            value = posFreq.get(pos) + 1;
        }
        posFreq.put(pos, value);
        poses.set(wordNo, pos);
        String[] splited = getHtmlOutput().split("span>");
        splited[wordNo] = splited[wordNo].replace(oldPos, pos);
        StringBuilder strb = new StringBuilder();
        for(String str : splited){
            String toAppend = str;
            if ( toAppend.endsWith("</")){
                toAppend += "span>";
            }
            strb.append(toAppend);
        }
        htmlOutput = strb;
    }

    @Override
    public String toString() {
        return "AnnotationResponse{" +
                "htmlOutput=" + htmlOutput +
                ", \nposFrequency=" + posFrequency +
                ", \nposPairFrequency=" + getPosPairFrequency() +
                ", \nwordFrequency=" + wordFrequency +
                ", \nwordAndPos=" + wordAndPos +
                ", \ncountOfSentences=" + countOfSentences +
                ", \ncountOfWords=" + countOfWords +
                '}';
    }
}
