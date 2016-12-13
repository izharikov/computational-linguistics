package com.dictionary.nlp;

import com.db.entity.DictWord;
import com.db.entity.PosTag;
import com.db.factory.DictFactory;
import com.db.utils.DictionaryUtils;
import com.db.utils.PosUtilsDB;
import com.dictionary.tags.WordTagUtils;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Ihar Zharykau
 */
@Configuration
public class PosUtilsImpl implements WordTagUtils {

    public static final POSTaggerME TAGGER;
    public static Tokenizer TOKENIZER;
    public static SentenceDetectorME SDETECTOR;

    static {
        POSModel model = new POSModelLoader()
                .load(new File("en-pos-maxent.bin"));
        TAGGER = new POSTaggerME(model);
        try {
            InputStream is = new FileInputStream("en-token.bin");

            TokenizerModel tokModel = new TokenizerModel(is);

            TOKENIZER = new TokenizerME(tokModel);
            is = new FileInputStream("en-sent.bin");
            SentenceModel sentModel = new SentenceModel(is);
            SDETECTOR = new SentenceDetectorME(sentModel);
        } catch (IOException e) {

        }
    }


    @Override
    public void computeAndSetTags() {
        Collection<DictWord> words = new DictFactory().getAllWords();
        for (DictWord dw : words) {
            String[] tags = TAGGER.tag(new String[]{dw.getWord()});
            for (String tag : tags) {
                PosTag posTag = PosUtilsDB.byName(tag);
                if (posTag != null) {
                    dw.getPosTags().add(posTag);
                }
            }
        }
        DictionaryUtils.saveAll(words);
    }

    private static final String FORMAT_STRING = "<span data-toggle=\"tooltip\" title=\"{0}\">{1}</span>";

    public void annotateForStatistic(String input, AnnotationResponse response) {
        List<String> sentenceList = tokenizeToSentences(input);
        response.setCountOfSentences(sentenceList.size());
        for (String sentence : sentenceList) {
            processSentence(sentence, response);
        }
    }

    public List<String> tokenizeToSentences(String input) {
        return Arrays.asList(SDETECTOR.sentDetect(input));
    }

    public void processSentence(String sentence, AnnotationResponse response) {
        List<String> sentenceParts = Arrays.asList(TOKENIZER.tokenize(sentence));
        List<String> words = filterWords(sentenceParts);
        response.addCountOfWords(words.size());
        List<String> tags = TAGGER.tag(words);
        response.appendToHTMLOutput(generateHtmlOutput(sentence, words, tags) + " ");
        for (int i = 0; i < tags.size(); i++) {
            String word = words.get(i).toLowerCase();
            response.addWordAndPos(word, tags.get(i));
            response.addWord(word);
            response.addPosToFreq(tags.get(i));
            response.addPos(tags.get(i));
        }
    }

    public String generateHtmlOutput(String sentence, List<String> words, List<String> tags) {
        String tempSentence = sentence;
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            int index = tempSentence.indexOf(word);
            strb.append(tempSentence.substring(0, index));
            strb.append(MessageFormat.format(FORMAT_STRING, tags.get(i), word));
            tempSentence = tempSentence.substring(index + word.length());
        }
        strb.append(tempSentence);
        return strb.toString();
    }

    public List<String> filterWords(List<String> stringList) {
        List<String> result = new ArrayList<>();
        for (String string : stringList) {
            if (!string.matches(".*\\p{Punct}") && !string.matches("\\p{Punct}.*")) {
                result.add(string);
            }
        }
        return result;
    }

    public static void main(String... args) throws IOException, ClassNotFoundException{
        AnnotationResponse response = new AnnotationResponse();
        PosUtilsImpl pos = new PosUtilsImpl();
        pos.annotateForStatistic("My name is Igor. What is your name?", response);
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(NLPService.getStoreFileName(0)));
        os.writeObject(response);
        os.close();
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(NLPService.getStoreFileName(0)));
        AnnotationResponse resp = (AnnotationResponse) is.readObject();
        is.close();
        resp.changePosForWord("your", "NN", "PRP$", 6);
        os = new ObjectOutputStream(new FileOutputStream(NLPService.getStoreFileName(0)));
        os.writeObject(resp);
        is = new ObjectInputStream(new FileInputStream(NLPService.getStoreFileName(0)));
        System.out.println(is.readObject());
    }
}
