package com.samy.stemmer.french;

import com.samy.stemmer.french.utils.DataUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Samy Badjoudj
 * TODO refactor to avoid creating string even if the GC garbadge them quickly
 */
public class FrenchStemmerImpl implements FrenchStemmer {

    private static final String PATTERN_VOWELS_STRING = "[aeiouyâàëéêèïîôûù]";
    private static final String PATTERN_VOWELS_2_STRING = PATTERN_VOWELS_STRING + PATTERN_VOWELS_STRING;
    private static final String PATTERN_UI_VOWELS_STRING = PATTERN_VOWELS_STRING + "[ui]" + PATTERN_VOWELS_STRING;
    private static final String PATTERN_WITH_Y_STRING = PATTERN_VOWELS_STRING + "[+y]";
    private static final String PATTERN_WITH_Y_ATER_STRING = "[+y]" + PATTERN_VOWELS_STRING;
    private static final String PATTERN_QU_STRING = "qu";
    private static final String PATTERN_REGION_STRING = PATTERN_VOWELS_STRING + "[^aeiouyâàëéêèïîôûù]";
    private static final String PATTERN_NON_VOWELS_ISSEMENT_STRING = "[^aeiouyâàëéêèïîôûù]issement[s]?";
    private static final Pattern PATTERN_VOWELS_2 = Pattern.compile(PATTERN_VOWELS_2_STRING);
    private static final Pattern PATTERN_UI_VOWELS = Pattern.compile(PATTERN_UI_VOWELS_STRING);
    private static final Pattern PATTERN_WITH_Y = Pattern.compile(PATTERN_WITH_Y_STRING);
    private static final Pattern PATTERN_WITH_Y_AFTER = Pattern.compile(PATTERN_WITH_Y_ATER_STRING);
    private static final Pattern PATTERN_WITH_QU = Pattern.compile(PATTERN_QU_STRING);
    private static final Pattern PATTERN_VOWELS = Pattern.compile(PATTERN_VOWELS_STRING);
    private static final Pattern PATTERN_NON_VOWELS = Pattern.compile(PATTERN_NON_VOWELS_ISSEMENT_STRING);
    private static final Pattern PATTERN_REGION = Pattern.compile(PATTERN_REGION_STRING);


    @Override
    public String getStemmedWord(String word) {
        word = vowelsUpperCase(word);
        WordInformations wordInformations = findRegions(word);
        boolean hasStepRemovedSuffixe;
        boolean specialSufficesFound;
        boolean doSecondStepA;
        boolean doSecondStepB = false;
        boolean doThirdStep = false;
        boolean doFourthStep = true;

        hasStepRemovedSuffixe = replaceSuffixesWithInRegion2(wordInformations, DataUtils.SUFFIXES_ISMES, DataUtils.BLANK_STRING);
        hasStepRemovedSuffixe |= removeOrRemoveSuffixesAti(wordInformations);
        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(wordInformations, DataUtils.SUFFIXES_LOG, DataUtils.SUFFIXE_LOG);
        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(wordInformations, DataUtils.SUFFIXES_TIONS, DataUtils.SUFFIXE_U);
        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(wordInformations, DataUtils.SUFFIXES_ENCE, DataUtils.SUFFIXE_ENT);
        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesEments(wordInformations);
        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesIte(wordInformations);
        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesIv(wordInformations);
        hasStepRemovedSuffixe |= replaceOrRemoveBaicSuffixes(wordInformations);
        specialSufficesFound = removeOrReplaceSpecialSuffixes(wordInformations);

        doSecondStepA = specialSufficesFound | !hasStepRemovedSuffixe;

        if (doSecondStepA) {
            doSecondStepB = !replaceSuffixesWithInPrecededWithConsonant(wordInformations, DataUtils.SUFFIXES_CONJ_I);
        }
        if (doSecondStepB) {
            if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_IONS)) {
                wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_IONS, DataUtils.BLANK_STRING));
                doThirdStep = true;
            }
            doThirdStep |= replaceSuffixesWithInRegion(wordInformations, DataUtils.SUFFIXES_CONJ_E);
            doThirdStep |= replaceSuffixesPrecededEWithInWordRegion(wordInformations, DataUtils.SUFFIXES_CONJ_A);


        }
        if (doThirdStep) {
            doFourthStep = doFourthStep(wordInformations);
        }

        if (doFourthStep) {
            fourthStep(wordInformations);
        }
        finalStep(wordInformations);


        return wordInformations.getNewWord();

    }

    private boolean doFourthStep(WordInformations wordInformations) {
        boolean suffixeRemains = true;
        if (wordInformations.getNewWord().endsWith("Y")) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 1) + "i");
            suffixeRemains = false;
        } else if (wordInformations.getNewWord().endsWith("ç")) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 1) + "c");
            suffixeRemains = false;

        }
        return suffixeRemains;
    }

    private void fourthStep(WordInformations wordInformations) {
        if (wordInformations.getNewWord().endsWith("s") && !DataUtils.SUFFIXE_AIOUES.contains(wordInformations.getNewWord().charAt(wordInformations.getNewWord().length() - 2) + DataUtils.BLANK_STRING)) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 1));
        }

        if (wordInformations.getNewWord().endsWith("ion")) {

            if ("ts".contains(wordInformations.getNewWord().charAt(wordInformations.getNewWord().length() - 4) + DataUtils.BLANK_STRING)) {
                wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 4));
            } else {
                wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 3));
            }
        }

        replaceSuffixesWithInRegion2(wordInformations, DataUtils.SUFFIXES_IER, "i");
        if (wordInformations.getNewWord().endsWith("e")) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 1));
        }
    }

    private void finalStep(WordInformations wordInformations) {
        if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_ENN)
                || wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_ONN)
                || wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_ETT)
                || wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_ELL)
                || wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_EILL)
                ) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 1));
        }

        wordInformations.setNewWord(wordInformations.getNewWord().toLowerCase());
    }

    private boolean replaceOrRemoveBaicSuffixes(WordInformations wordInformations) {
        boolean hasStepRemovedSuffixe;
        hasStepRemovedSuffixe = false;
        if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_EAUX)) {
            wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_EAUX, DataUtils.SUFFIXE_EAU));
            hasStepRemovedSuffixe = true;
        }
        if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_AUX) && wordInformations.getRegionfirst().endsWith(DataUtils.SUFFIXE_AUX)) {
            wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_AUX, "al"));
            hasStepRemovedSuffixe = true;

        }
        if (wordInformations.getRegionfirst().endsWith(DataUtils.SUFFIXE_EUSES)) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 5));
            hasStepRemovedSuffixe = true;

        } else if (wordInformations.getRegionfirst().endsWith(DataUtils.SUFFIXE_EUSE)) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 4));
            hasStepRemovedSuffixe = true;

        } else if (wordInformations.getRegionfirst().endsWith(DataUtils.SUFFIXE_EUSES)) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 5) + DataUtils.SUFFIXE_EUX);
            hasStepRemovedSuffixe = true;

        } else if (wordInformations.getRegionfirst().endsWith(DataUtils.SUFFIXE_EUSE)) {
            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 4) + DataUtils.SUFFIXE_EUX);
            hasStepRemovedSuffixe = true;

        }

        Matcher matcher = PATTERN_NON_VOWELS.matcher(wordInformations.getRegionfirst());
        if (matcher.find()) {
            wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_ISSEMENTS, DataUtils.BLANK_STRING));
            wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_ISSEMENT, DataUtils.BLANK_STRING));
            hasStepRemovedSuffixe = true;
        }

        return hasStepRemovedSuffixe;
    }

    private boolean removeOrReplaceSpecialSuffixes(WordInformations wordInformations) {
        Matcher matcher;
        boolean specialSufficesFound = false;

        if (wordInformations.getRegion().endsWith(DataUtils.SUFFIXE_AMMENT)) {
            wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_AMMENT, "ant"));
            specialSufficesFound = true;
        }
        if (wordInformations.getRegion().endsWith(DataUtils.SUFFIXE_EMMENT)) {
            wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_EMMENT, DataUtils.SUFFIXE_ENT));
            specialSufficesFound = true;
        }


        matcher = PATTERN_NON_VOWELS.matcher(wordInformations.getRegion());
        if (matcher.find()) {
            wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_MENTS, DataUtils.BLANK_STRING));
            wordInformations.setNewWord(wordInformations.getNewWord().replace(DataUtils.SUFFIXE_MENT, DataUtils.BLANK_STRING));
            specialSufficesFound = true;
        }
        return specialSufficesFound;
    }

    private WordInformations findRegions(String word) {

        Matcher vowels2 = PATTERN_VOWELS_2.matcher(word);
        Matcher vowels = PATTERN_VOWELS.matcher(word);
        Matcher region = PATTERN_REGION.matcher(word);


        WordInformations wordInformations = new WordInformations(word);
        if (word.startsWith(DataUtils.PREFIX_TAP) || word.startsWith(DataUtils.PREFIX_COL) || word.startsWith(DataUtils.PREFIX_PAR)) {
            wordInformations.setRegion(word.substring(3));
        }


        if (vowels2.find()) {
            wordInformations.setRegion(word.substring(3));
        } else if (vowels.find()) {

            if (vowels.start() == 0 && vowels.find()) {
                wordInformations.setRegion(word.substring(vowels.end()));
            } else {
                wordInformations.setRegion(word.substring(vowels.end()));
            }
        }

        if (region.find()) {
            wordInformations.setRegionfirst(word.substring(region.end()));
            if (region.find()) {
                wordInformations.setRegionSecond(word.substring(region.end()));
            }

        }

        return wordInformations;
    }

    private String vowelsUpperCase(String word) {

        char[] wordArray = word.toCharArray();
        Matcher uiVowels = PATTERN_UI_VOWELS.matcher(word);
        Matcher y = PATTERN_WITH_Y.matcher(word);
        Matcher yAfter = PATTERN_WITH_Y_AFTER.matcher(word);
        Matcher qu = PATTERN_WITH_QU.matcher(word);

        while (uiVowels.find()) {
            wordArray[uiVowels.start() + 1] = Character.toUpperCase(wordArray[uiVowels.start() + 1]);
        }
        while (y.find()) {
            wordArray[y.start() + 1] = Character.toUpperCase(wordArray[y.start() + 1]);
        }
        while (yAfter.find()) {
            wordArray[yAfter.start()] = Character.toUpperCase(wordArray[yAfter.start()]);
        }

        while (qu.find()) {
            wordArray[qu.start() + 1] = Character.toUpperCase(wordArray[qu.start() + 1]);
        }
        return new String(wordArray);
    }

    private boolean replaceSuffixesWithInRegion2(WordInformations wordInformations, List<String> suffixes, String replaceWith) {
        for (String suffixe : suffixes) {
            if (wordInformations.getRegionSecond().endsWith(suffixe)) {
                wordInformations.setNewWord(wordInformations.getNewWord().replace(suffixe, replaceWith));
                return true;
            }
        }
        return false;
    }

    private boolean replaceSuffixesWithInPrecededWithConsonant(WordInformations wordInformations, List<String> suffixes) {
        for (String suffixe : suffixes) {
            if (wordInformations.getNewWord().endsWith(suffixe)
                    && !PATTERN_VOWELS_STRING.contains(wordInformations.getNewWord().charAt(wordInformations.getNewWord().length()
                    - (suffixe.length() - 1)) + DataUtils.BLANK_STRING)) {
                wordInformations.setNewWord(wordInformations.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                return true;
            }
        }
        return false;
    }

    private boolean replaceSuffixesWithInRegion(WordInformations wordInformations, List<String> suffixes) {
        for (String suffixe : suffixes) {
            if (wordInformations.getRegionSecond().endsWith(suffixe)) {
                wordInformations.setNewWord(wordInformations.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                return true;
            }
        }
        return false;
    }

    private boolean replaceSuffixesPrecededEWithInWordRegion(WordInformations wordInformations, List<String> suffixes) {
        for (String suffixe : suffixes) {
            if (wordInformations.getNewWord().endsWith(suffixe)) {
                if (wordInformations.getNewWord().charAt(wordInformations.getNewWord().length() - suffixe.length()) == 'e') {
                    wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - suffixe.length() + 1));

                } else {
                    wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - suffixe.length()));
                }
                return true;
            }
        }
        return false;
    }

    private boolean removeOrRemoveSuffixesAti(WordInformations wordInformations) {
        for (String suffixe : DataUtils.SUFFIXES_AT) {
            if (wordInformations.getRegionSecond().endsWith(suffixe)) {
                wordInformations.setNewWord(wordInformations.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_IC)) {
                    if (wordInformations.getRegionSecond().endsWith(DataUtils.SUFFIXE_IC + suffixe)) {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2));
                    } else {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2) + DataUtils.SUFFIXES_iqU);
                    }

                }
                return true;
            }
        }
        return false;
    }

    private boolean replaceOrRemoveSuffixesIv(WordInformations wordInformations) {
        for (String suffixe : DataUtils.SUFFIXES_IV) {
            if (wordInformations.getRegionSecond().endsWith(suffixe)) {
                wordInformations.setNewWord(wordInformations.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_AT)) {
                    if (wordInformations.getRegionSecond().endsWith(DataUtils.SUFFIXE_AT + suffixe)) {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2));
                    }
                    if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_IC)) {
                        if (wordInformations.getRegionSecond().endsWith(DataUtils.SUFFIXE_IC + suffixe)) {
                            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2));
                        } else {
                            wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2) + DataUtils.SUFFIXES_iqU);
                        }

                    }

                }
                return true;
            }
        }
        return false;
    }


    private boolean replaceOrRemoveSuffixesIte(WordInformations wordInformations) {
        for (String suffixe : DataUtils.SUFFIXES_IT) {
            if (wordInformations.getRegionSecond().endsWith(suffixe)) {
                wordInformations.setNewWord(wordInformations.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_ABIL)) {
                    if (wordInformations.getRegionSecond().endsWith(DataUtils.SUFFIXE_ABIL + suffixe)) {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 3));
                    } else {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 3) + DataUtils.SUFFIXE_ABL);
                    }
                } else if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_IC)) {
                    if (wordInformations.getRegionSecond().endsWith(DataUtils.SUFFIXE_IC + suffixe)) {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2));
                    } else {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2) + DataUtils.SUFFIXES_iqU);
                    }
                } else if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_IV)) {
                    if (wordInformations.getRegionSecond().endsWith(DataUtils.SUFFIXE_IV + suffixe)) {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2));
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean replaceOrRemoveSuffixesEments(WordInformations wordInformations) {
        for (String suffixe : DataUtils.SUFFIXES_EMENT) {
            if (wordInformations.getRegionSecond().endsWith(suffixe)) {
                wordInformations.setNewWord(wordInformations.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_IV)) {
                    if (wordInformations.getRegionSecond().endsWith(DataUtils.SUFFIXE_IV + suffixe)) {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 2));
                    }
                } else if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXES_EUS)) {
                    if (wordInformations.getRegionSecond().endsWith(DataUtils.SUFFIXES_EUS + suffixe)) {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 3));
                    } else if (wordInformations.getRegionfirst().contains(DataUtils.SUFFIXES_EUS)) {
                        wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 3) + DataUtils.SUFFIXES_EUX);
                    }
                } else if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_ABL + suffixe) || wordInformations.getNewWord().endsWith(DataUtils.SUFFIXES_iqU + suffixe)) {
                    wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 3));
                } else if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_IER + suffixe)) {
                    wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 3));
                } else if (wordInformations.getNewWord().endsWith(DataUtils.SUFFIXE_IERE + suffixe)) {
                    wordInformations.setNewWord(wordInformations.getNewWord().substring(0, wordInformations.getNewWord().length() - 4));
                }
                return true;
            }
        }
        return false;
    }
}
