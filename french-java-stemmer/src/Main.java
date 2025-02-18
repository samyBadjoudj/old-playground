import com.samy.stemmer.french.FrenchStemmer;
import com.samy.stemmer.french.FrenchStemmerImpl;

import java.util.ArrayList;

/**
 * Created by Samy Badjoudj
 */

public class Main {

    public static void main(String[] args) {
        FrenchStemmer  french = new FrenchStemmerImpl();
        ArrayList<String> words = new ArrayList<String>(50);
        ArrayList<String> wordsToMatch = new ArrayList<String>(50);

        words.add("continu");
        words.add("continua");
        words.add("continuait");
        words.add("continuant");
        words.add("continuation");
        words.add("continue");
        words.add("continué");
        words.add("continuel");
        words.add("continuelle");
        words.add("continuellement");
        words.add("continuelles");
        words.add("continuels");
        words.add("continuer");
        words.add("continuera");
        words.add("continuerait");
        words.add("continueront");
        words.add("continuez");
        words.add("continuité");
        words.add("continuons");
        // TODO error here to debug
        //words.add("contorsions");
        words.add("contour");
        words.add("contournait");
        words.add("contournant");
        words.add("contourne");
        words.add("contours");
        words.add("contractait");
        words.add("contracté");
        words.add("contractée");
        words.add("contracter");
        words.add("contractés");
        words.add("contractions");
        words.add("contradictoirement");
        words.add("contradictoires");
        words.add("contraindre");
        words.add("contraint");
        words.add("contrainte");
        words.add("contraintes");
        words.add("contraire");
        words.add("contraires");
        words.add("contraria");
        words.add("psychologique");
        words.add("proportions");
        words.add("anticonstitutionnellement");




/*        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continuel");
        wordsToMatch.add("continuel");
        wordsToMatch.add("continuel");
        wordsToMatch.add("continuel");
        wordsToMatch.add("continuel");
        wordsToMatch.add("continu" );
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continu");
        wordsToMatch.add("continuon");
        //wordsToMatch.add("contors");
        wordsToMatch.add("contour");
        wordsToMatch.add("contourn");
        wordsToMatch.add("contourn");
        wordsToMatch.add("contourn");
        wordsToMatch.add("contour");
        wordsToMatch.add("contract");
        wordsToMatch.add("contract");
        wordsToMatch.add("contract");
        wordsToMatch.add("contract");
        wordsToMatch.add("contract");
        wordsToMatch.add("contract");
        wordsToMatch.add("contradictoir");
        wordsToMatch.add("contradictoir");
        wordsToMatch.add("contraindr");
        wordsToMatch.add("contraint");
        wordsToMatch.add("contraint");
        wordsToMatch.add("contraint");
        wordsToMatch.add("contrair");
        wordsToMatch.add("contrair");
        wordsToMatch.add("contrari");
        wordsToMatch.add("psycholog");
        wordsToMatch.add("proport");
        wordsToMatch.add("anticonstitutionnel");*/

        for (int i = 0; i < words.size(); i++) {
            System.out.println(words.get(i)+"  ====> " + french.getStemmedWord(words.get(i)));
        }


    }
}
