package com.samy.maven.plugin;

/**
 * User: Samy Badjoudj
 * This small plugin check if you miss some translations in atlernatives languages, compare to a main one.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.util.*;


@Mojo(name = "check-translation")
public class MavenTransationChecker extends AbstractMojo {

    private static final String SEPARATOR = "=";
    private static final String MISSING_TRANSLATION_HAVE_BEEN_FOUND = "Some missing translations have been detected";
    private static final String NO_MISSING_TRANSLATION_HAVE_BEEN_FOUND = "Congratulations no missing translation found";
    private final static String FILE_NOT_FOUND = "Can't find/open file ";
    private final static String MISSING_MAIN_LANGUAGE_FILE_PROPERTY = "Stop exectution, missing <mainLanguageFile property> , example :\n" +
            "               <configuration>\n" +
            "                    <mainLanguageFile>${basedir}/src/main/resources/MessagesBundle.properties</mainLanguageFile>\n" +
            "                    <files>\n" +
            "                        <file>${basedir}/src/main/resources/MessagesBundle_de_DE.properties</file>\n" +
            "                        <file>${basedir}/src/main/resources/MessagesBundle_de_US.properties</file>\n" +
            "                        <file>${basedir}/src/main/resources/MessagesBundle_fr_FR.properties</file>\n" +
            "                    </files>\n" +
            "                    <failOnMissingTranslationsFound>true</failOnMissingTranslationsFound>\n" +
            "                </configuration>" +
            "";


    /**
     * Main language file path
     */
    @Parameter
    private String mainLanguageFile;

    /**
     * If it is at true it will stop the maven build,
     * if it finds at least one missing translation
     */
    @Parameter
    private boolean failOnMissingTranslationsFound;

    /**
     * List of the file to be checked (keys) against the main language file
     */
    @Parameter
    private List<String> files;


    /**
     * Will be set to true if at least one file miss a property
     */
    private boolean missingTranslationFound;

    /**
     * Main file, all other keys will be check against this key properties files
     */
    private FileReader mainFile;

    /**
     * Result of missing translation key
     */
    private final Map<String, Set<String>> allMissingTranslations = new HashMap<String, Set<String>>();

    public MavenTransationChecker() {
    }

    /**
     * @throws MojoFailureException
     */
    public void execute() throws MojoFailureException {
        if (checkMainFile()) return;
        Set<String> mainTranslation = getSet(mainFile, mainLanguageFile);
        for (String file : files) {
            FileReader currentLanguageFile = getFileReader(file);
            if (currentLanguageFile != null) {
                Set<String> differences = getDifferences(mainTranslation, file, currentLanguageFile);
                allMissingTranslations.put(file, differences);
                missingTranslationFound |= differences.size() > 0;
            }
        }
        if (missingTranslationFound) {
            fail(allMissingTranslations);
        } else {
            getLog().info(NO_MISSING_TRANSLATION_HAVE_BEEN_FOUND);
        }
    }

    /**
     * @param allMissingTranslations final object with key = file, value list of missing translation
     *                               for current file
     * @throws MojoFailureException
     */
    private void fail(Map<String, Set<String>> allMissingTranslations) throws MojoFailureException {
        printFailReport(allMissingTranslations);
        if (failOnMissingTranslationsFound) {
            throw new MojoFailureException(MISSING_TRANSLATION_HAVE_BEEN_FOUND);
        }
    }

    /**
     * This method browses the result and prints it via the warn log
     *
     * @param allMissingTranslations global result object
     */
    private void printFailReport(Map<String, Set<String>> allMissingTranslations) {
        for (String fileConcerned : allMissingTranslations.keySet()) {
            StringBuffer found = new StringBuffer();
            for (String key : allMissingTranslations.get(fileConcerned)) {
                found.append("-> ").append(key).append("\n          ");
            }
            if (allMissingTranslations.get(fileConcerned).size() > 0) {
                getLog().warn("[Missing translations for " + fileConcerned + "]");
                getLog().warn(found.toString());
            }
        }
    }

    /**
     * Get the diffrence between two sets
     *
     * @param mainTransaltion     all keys of the main translation file
     * @param path                path of the file that keys will be extracted and compared with the main translation file
     * @param currentLanguageFile file that keys will be extracted and compared with the main translation file
     * @return Set of difference between mmainTranslation keys and key extracted in currentLanguageFile
     */
    private Set<String> getDifferences(Set<String> mainTransaltion, String path, FileReader currentLanguageFile) {
        Set<String> currenTranslations = getSet(currentLanguageFile, path);
        final Set<String> missingTranslation = new HashSet<String>(mainTransaltion);
        missingTranslation.removeAll(currenTranslations);
        return missingTranslation;
    }

    /**
     * @param currentLanguageFile the filereader to the file
     * @param path                to the file
     * @return create a set of all key of the path
     */
    private Set<String> getSet(FileReader currentLanguageFile, String path) {
        BufferedReader br = new BufferedReader(currentLanguageFile);
        Set<String> allKeys = new HashSet<String>();
        try {
            String line = br.readLine();
            while (line != null) {
                String key = getCurrentKeyProperty(line);
                if (key != null) {
                    allKeys.add(key);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            getLog().warn(FILE_NOT_FOUND + path);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                getLog().warn(FILE_NOT_FOUND + path);
            }
        }
        return allKeys;
    }

    /**
     * @param line to extract the key property -> key=value
     * @return the key
     */
    private String getCurrentKeyProperty(String line) {
        final String[] split;
        if (line != null) {
            split = line.split(SEPARATOR);
            return split.length > 0 ? split[0].trim() : null;
        }
        return null;
    }

    /**
     * @return if the main language file can be read (without it nothing can be done)
     */
    private boolean checkMainFile() {
        if (mainLanguageFile != null) {
            try {
                mainFile = new FileReader(mainLanguageFile);
            } catch (FileNotFoundException e) {
                getLog().warn(FILE_NOT_FOUND + mainLanguageFile);
            }
        } else {
            getLog().warn(MISSING_MAIN_LANGUAGE_FILE_PROPERTY);
            return true;
        }
        return false;
    }


    /**
     * @param path of the file to be read
     * @return the file reader or null if it can not read it
     */
    private FileReader getFileReader(String path) {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException e) {
            getLog().warn(FILE_NOT_FOUND + path);
        }
        return null;
    }




}
