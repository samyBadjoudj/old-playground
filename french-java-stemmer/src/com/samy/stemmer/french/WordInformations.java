package com.samy.stemmer.french;

/**
 * Created by Samy Badjoudj
 */
public class WordInformations {

    private String word;
    private String newWord;
    private String regionSecond = "";
    private String regionfirst = "";
    private String region = "";


    public WordInformations(String word) {
        assert word != null;
        this.word = word.toLowerCase();
        this.newWord = word;
    }

    public String getRegionSecond() {
        return regionSecond;
    }

    public void setRegionSecond(String regionSecond) {
        this.regionSecond = regionSecond;
    }

    public String getRegionfirst() {
        return regionfirst;
    }

    public void setRegionfirst(String regionfirst) {
        this.regionfirst = regionfirst;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNewWord() {
        return newWord;
    }

    public void setNewWord(String newWord) {
        this.newWord = newWord;
    }

    public String getWord() {
        return this.word ;
    }


}
