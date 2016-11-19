package com.dictionary.nlp;

import com.db.entity.DictWord;
import com.db.entity.PosTag;
import com.db.factory.DictFactory;
import com.db.utils.DictionaryUtils;
import com.db.utils.PosUtilsDB;
import com.dictionary.tags.WordTagUtils;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Collection;

/**
 * @author Ihar Zharykau
 */
@Configuration
public class PosUtilsImpl implements WordTagUtils {

    private static final POSTaggerME TAGGER;

    static{
        POSModel model = new POSModelLoader()
                .load(new File("en-pos-maxent.bin"));
        TAGGER = new POSTaggerME(model);
    }


    @Override
    public void computeAndSetTags() {
        Collection<DictWord> words = new DictFactory().getAllWords();
        for(DictWord dw : words){
            String[] tags = TAGGER.tag(new String[]{dw.getWord()});
            for (String tag : tags){
                PosTag posTag = PosUtilsDB.byName(tag);
                if ( posTag != null) {
                    dw.getPosTags().add(posTag);
                }
            }
        }
        DictionaryUtils.saveAll(words);
    }
}
