/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 *
 * @author Dustin Brown
 * @since March 5, 2016
 */
public class Hangman {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException{

        // Get a word to be guessed
        String guessWord = prepareWord();
        
        // Create a canvas using the given word
        HangCanvas window = prepareCanvas(guessWord);
        
        // Set up the frame
        JFrame frame = prepareFrame(window);

        // Main "game" part
        Scanner scan = new Scanner(System.in);
        int failCount = 0;
        String input;
        while(failCount < 6){
            System.out.println("Pick a letter (a-z):");
            String inputFull = scan.nextLine();
            if (!inputFull.equals("")){
                input = String.valueOf(inputFull.charAt(0));
            }else{
                // Arbitrary value for error catching below
                input = "01";
            }
            if (!guessWord.contains(input)){
                // If we got an empty string, don't try putting it into the wrongGuesses array
                // But still count it as a wrong guess. If you just mash enter, you lose.
                if(!input.equals("01")){
                    window.setWrongGuess(failCount, input);
                }
                failCount++;
                switch (failCount) {
                    case 1:
                        window.setMakeHead(true);
                        window.repaint();
                        System.out.print("Nope! Here's your head! ");
                        break;
                    case 2:
                        window.setMakeTorso(true);
                        window.repaint();
                        System.out.print("Nope! Here's your body! ");
                        break;
                    case 3:
                        window.setMakeArmL(true);
                        window.repaint();
                        System.out.print("Nope! Here's your arm! ");
                        break;
                    case 4:
                        window.setMakeArmR(true);
                        window.repaint();
                        System.out.print("Nope! Here's your other arm! ");
                        break;
                    case 5:
                        window.setMakeLegL(true);
                        window.repaint();
                        System.out.print("Nope! Here's your leg! One more chance! ");
                        break;
                    case 6:
                        window.setMakeLegR(true);
                        window.repaint();
                        System.out.print("Last leg! Game over! ");
                        break;
                    default:
                        break;
                }
            }else{
                int i;
                boolean alreadyPrinted = false;
                for (i=0; i<guessWord.length(); i++){
                    String checkLetter = String.valueOf(guessWord.charAt(i));
                    if (input.equals(checkLetter)){
                        window.setCorrectLetter(i);
                        if(!alreadyPrinted){
                            System.out.println("Correctly guessed \"" + checkLetter + "\"! ");
                            alreadyPrinted = true;
                        }
                        window.repaint();
                    }                    
                }
                boolean didWin = true;
                int[] correctLetters = window.getAllCorrectLetters();
                for (i=0; i<correctLetters.length; i++){
                    if (correctLetters[i]==0){
                        didWin = false;
                        break;
                    }
                }
                if (didWin){
                    System.out.print("You win! ");
                    break;
                }
            }
        }
//        System.out.println("Play again? (y/n)");
        
    }
    
    public static String prepareWord() throws IOException{
        URL listFile = Hangman.class.getResource("/wordlist.txt");
        WordList dict = new WordList(listFile);
        String guessWord = dict.selectWord();    
        System.out.println(guessWord + " " + guessWord.length());
        return guessWord;
    }
    
    public static HangCanvas prepareCanvas(String guessWord){
        HangCanvas window = new HangCanvas();
        window.setWord(guessWord);
        window.setWordLength(guessWord.length());
        window.setAllCorrectLetters(new int[guessWord.length()]);
        return window;
    }
    
    public static JFrame prepareFrame(HangCanvas canvas){
        JFrame frame = new JFrame("Hangman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(630, 340);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        return frame;
    }
}
