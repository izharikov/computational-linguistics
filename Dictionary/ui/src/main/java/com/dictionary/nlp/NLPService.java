package com.dictionary.nlp;

import com.db.entity.AnnotatedEntity;
import com.db.entity.DictWord;
import com.db.entity.PosTag;
import com.dictionary.repository.AnnotateStore;
import com.dictionary.repository.DictionaryRepository;
import com.dictionary.repository.PosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ihar Zharykau
 */
@Configuration
public class NLPService {
    private static final String REPORT_PATH = "/home/igor/university/3rd/comp-ling/Dictionary/ui/annotation/report/";
    private static final String STORE_PATH = "/home/igor/university/3rd/comp-ling/Dictionary/ui/annotation/files/";

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Autowired
    private AnnotateStore annotateStore;

    @Autowired
    private PosRepository posRepository;

    public void updateOneWord(String word, String newPos, String oldPos, int no, AnnotationResponse response) {
        response.changePosForWord(word, newPos, oldPos,no);
    }

    public void saveAnnotatedResponse(AnnotationResponse response, String name, String text) {
        int id = addResponseToRepository(name);
        writeResponseToFile(response, id);
        processResponseToDictionary(response);
        saveTextToFile(text, id);
    }

    public AnnotationResponse lastResponse() {
        AnnotationResponse response = new AnnotationResponse();
        List<AnnotatedEntity> entities = annotateStore.findAll();
        AnnotatedEntity e = entities.get(entities.size() - 1);
        return byEntity(e);
    }

    public AnnotationResponse byEntity(AnnotatedEntity e){
        AnnotationResponse response = new AnnotationResponse();
        String reportPath = getReportPath(e.getId());
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(reportPath));
            Object obj = is.readObject();
            if (obj instanceof AnnotationResponse) {
                response = (AnnotationResponse) obj;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        response.setPosTags(getCache());
        return response;
    }

    public AnnotatedEntity last(){
        List<AnnotatedEntity> entities = annotateStore.findAll();
        return entities.get(entities.size() - 1);
    }

    public static String getStoreFileName(int id) {
        return STORE_PATH + id + ".txt";
    }

    public static String getReportPath(int id) {
        return REPORT_PATH + id + ".bin";
    }

    public int addResponseToRepository(String fileName) {
        AnnotatedEntity ent = new AnnotatedEntity();
        ent.setSourceFileName(fileName);
        return annotateStore.save(ent).getId();
    }

    public void writeResponseToFile(AnnotationResponse response, int id) {
        String responsePath = getReportPath(id);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(responsePath));
            os.writeObject(response);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processResponseToDictionary(AnnotationResponse response) {
        loadCache();
        Map<String, Integer> wordFrequency = response.getWordFrequency();
        Map<String, Set<String>> wordsAndPos = wordAndPosFromReponse(response);
        List<DictWord> words = new LinkedList<>();
        for (Map.Entry<String, Integer> wordEntry : wordFrequency.entrySet()) {
            DictWord dw = new DictWord();
            dw.setWord(wordEntry.getKey());
            dw.setWordCount(wordFrequency.get(wordEntry.getKey()));
            dw.setPosTags(posByNames(wordsAndPos.get(wordEntry.getKey())));
            if (dw.getPosTags() == null) {
                dw.setPosTags(new HashSet<>());
            }
            dw = modifyIfExists(dw);
            dictionaryRepository.save(dw);
            words.add(dw);
        }
        dictionaryRepository.flush();
    }

    private Map<String, Set<String>> wordAndPosFromReponse(AnnotationResponse response) {
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : response.getWordAndPos().entrySet()) {
            result.put(entry.getKey(), entry.getValue().keySet());
        }
        return result;
    }

    private DictWord modifyIfExists(DictWord dw) {
        DictWord old = dictionaryRepository.findByWord(dw.getWord());
        if (old != null) {
            old.getPosTags().addAll(dw.getPosTags());
            old.setWordCount(old.getWordCount() + dw.getWordCount());
            return old;
        }
        return dw;
    }

    private static Map<String, PosTag> cache = new HashMap<>();

    private void loadCache(){
        List<PosTag> tags = posRepository.findAll();
        for (PosTag posTag : tags) {
            cache.put(posTag.getName(), posTag);
        }
    }

    private PosTag get(String tag) {
        if (cache.size() == 0) {
            List<PosTag> tags = posRepository.findAll();
            for (PosTag posTag : tags) {
                cache.put(posTag.getName(), posTag);
            }
        }
        return cache.get(tag);
    }

    private Map<String, PosTag> getCache() {
        if (cache == null || cache.size() == 0) {
            List<PosTag> tags = posRepository.findAll();
            for (PosTag posTag : tags) {
                cache.put(posTag.getName(), posTag);
            }
        }
        return cache;
    }

    private Set<PosTag> posByNames(Set<String> names) {
        Set<PosTag> posTags = new HashSet<>();
        for (String name : names) {
            posTags.add(get(name));
        }
        return posTags;
    }

    private void saveTextToFile(String text, int id) {
        String storeFile = getStoreFileName(id);
        try {
            PrintWriter pw = new PrintWriter(storeFile);
            pw.write(text);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
