package com.jizhang.mjtzn.jizhang;

import java.util.LinkedList;
import java.util.List;

public class RecognitionResultBean {

    @Override
    public String toString() {
        return "num:" + words_result_num+
                " words_result:" + words_result;
    }

    private long log_id;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public LinkedList<String> getWords_result() {
        LinkedList<String> res=new LinkedList<>();
        for(int i=0;i<words_result.size();i++){
            res.add(words_result.get(i).toString());
        }
        return res;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        @Override
        public String toString() {
            return words;
        }

        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
