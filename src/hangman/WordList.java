/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

/**
 *
 * @author meisb
 */
public class WordList {
    
    private URL path;
    private int numLines;

    public WordList(URL filepath) {
        this.path = filepath;
    } 

    public URL getPath() {
        return path;
    }

    public void setPath(URL filepath) {
        this.path = filepath;
    }

    public int getNumLines() {
        return numLines;
    }

    public void setNumLines(int num) {
        this.numLines = num;
    }
    
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
   
    public int countLines() throws IOException {        
        BufferedReader br = new BufferedReader(new InputStreamReader(path.openStream()));

        int lineCount = 0;
        while (br.readLine() != null){
            lineCount++;
        }

        br.close();        
        return lineCount;
    }
    
    public String selectWord() throws IOException {
        String[] words = getWords();
        Random rng = new Random();
        int i = rng.nextInt(words.length);
        return words[i];
    }
}
