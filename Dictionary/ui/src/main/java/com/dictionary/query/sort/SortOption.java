package com.dictionary.query.sort;

/**
 * @author Ihar Zharykau
 */
public enum SortOption {
    WORD_ASC, WORD_DESC, COUNT_ASC, COUNT_DESC, NONE;

    @Override
    public String toString() {
        switch (this) {
            case WORD_ASC:
                return " order by word asc";
            case WORD_DESC:
                return " order by word desc";
            case COUNT_ASC:
                return " order by wordCount asc";
            case COUNT_DESC:
                return " order by wordCount desc";
        }
        return "";
    }

    public static SortOption byString(String string){
        switch (string){
            case "wordASC":
                return WORD_ASC;
            case "wordDESC":
                return WORD_DESC;
            case "countASC":
                return COUNT_ASC;
            case "countDESC":
                return COUNT_DESC;
        }
        return NONE;
    }
}
