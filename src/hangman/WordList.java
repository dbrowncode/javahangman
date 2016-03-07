/*
 * Allows hangman game to get a list of words from an external file to use as its dictionary.
 * Designed to work with a newline-separated list of words.
 */
package hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

/**
 *
 * @author Dustin Brown
 * @since March 5, 2016
 */
public class WordList {
    
    private URL path;
    private int numLines;

    /**
     * Constructor taking a path to a file.
     *
     * @param filepath - path to file.
     */
    public WordList(URL filepath) {
        this.path = filepath;
    } 

    /**
     * Get the path of the file used by the WordList.
     *
     * @return path - path to file.
     */
    public URL getPath() {
        return path;
    }

    /**
     * Set the path.
     *
     * @param filepath - path to file.
     */
    public void setPath(URL filepath) {
        this.path = filepath;
    }

    /**
     * Get the number of lines in the file.
     *
     * @return numLines - number of lines.
     */
    public int getNumLines() {
        return numLines;
    }

    /**
     * Set the number of lines expected in the file.
     *
     * @param num - number of lines.
     */
    public void setNumLines(int num) {
        this.numLines = num;
    }
    
    /**
     * Get an array of all the words (lines) in the file.
     *
     * @return words - array of words.
     * @throws IOException
     */
    public String[] getWords() throws IOException{
        this.numLines = countLines();
        String[] words = new String[this.numLines];
        
        BufferedReader read = new BufferedReader(new InputStreamReader(path.openStream()));
        int i;
        for (i=0; i<this.numLines; i++) {
            words[i] = read.readLine();
        }
        
        read.close();
        return words;
    }
   
    /**
     * Find out how many lines/words are in the file.
     *
     * @return lineCount - number of lines.
     * @throws IOException
     */
    public int countLines() throws IOException {        
        BufferedReader br = new BufferedReader(new InputStreamReader(path.openStream()));

        int lineCount = 0;
        while (br.readLine() != null){
            lineCount++;
        }

        br.close();        
        return lineCount;
    }
    
    /**
     * Get a random word from the array of words.
     *
     * @return words[i] - random word.
     * @throws IOException
     */
    public String selectWord() throws IOException {
        String[] words = getWords();
        Random rng = new Random();
        int i = rng.nextInt(words.length);
        return words[i];
    }
}
