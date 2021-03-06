/*
 * Runs games of Hangman using the HangCanvas and WordList classes.
 */
package hangman;

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
     * Main method creates a canvas and frame, then runs games until
     * the user is done playing.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args){
     
        // Create a canvas
        HangCanvas window = new HangCanvas();        
        
        // Set up the frame
        JFrame frame = prepareFrame(window);
        
        // Run games until quit is selected
        do{
            playGame(window, frame);            
        }while(window.isKeepPlaying());
        System.exit(0);
        
    }
    
    /**
     * Creates the word list and selects a random word.
     *
     * @return guessWord - the secret word to be guessed.
     */
    public static String prepareWord(){
        // Default list here is a modified version of the English Open Word List 
        // (http://dreamsteep.com/projects/the-english-open-word-list.html)
        // Consists of lower-case words between 5 and 10 characters long
        // Any newline-separated list of words should work, but this program has been designed to handle
        // words no longer than 10 characters.
        URL listFile = Hangman.class.getResource("/wordlist.txt");
        WordList dict = new WordList(listFile);
        return dict.selectWord();
    }
    
    /**
     * Sets up the JFrame and puts the canvas in it.
     *
     * @param canvas - canvas to put in the frame.
     * @return frame - JFrame containing the canvas.
     */
    public static JFrame prepareFrame(HangCanvas canvas){
        JFrame frame = new JFrame("Hangman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(630, 340);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        return frame;
    }
    public static String getInput(Scanner scan){
        String inputFull = scan.nextLine();
            // Make sure the player didn't just hit "enter"
            if (!inputFull.equals("")){
                // If there is valid input, just grab the first character.
                return String.valueOf(inputFull.charAt(0));
            }else{
                // Otherwise, arbitrary value assigned to help avoid errors.
                return "01";
            }
    }
    
    /**
     * Main game logic is all here. This method will run a game of Hangman
     * and then ask if the user wants to play again.
     *
     * @param window - the canvas containing graphics and game state information.
     * @param frame - the frame containing the canvas.
     */
    public static void playGame(HangCanvas window, JFrame frame){
        // Create scanner for input
        Scanner scan = new Scanner(System.in);
        
        // Initial prompt will come up at the same time as the start/rules screen
        System.out.println("Do you want to play a game? ('p' to play, 'q' to quit)");        

        String input = getInput(scan);
        // If we get anything other than p/P, just exit.
        if (!input.toLowerCase().equals("p")){
            System.out.println("OK. Goodbye!");
            System.exit(0);
        // We got a "p", so start the game
        }else{
            // Tell the canvas we're going past the startup screen
            window.setGameStarted(true);
            // Give the canvas a random word and its length
            String guessWord = prepareWord();
            window.setWord(guessWord);
            window.setWordLength(guessWord.length());
            // Set up an array of 0s equal to the word length
            window.setAllCorrectLetters(new int[guessWord.length()]);
            // Refresh the display
            window.repaint();
        }
        
            

        // Main game logic starts here
        // Loop the letter-guessing part until player guesses wrong too many times (or wins, which will break out).
        while(window.failCount < window.getMAX_WRONG()){
            // Prompt for and get input of (hopefully) a letter
            System.out.println("Pick a letter (a-z):");
            input = getInput(scan);
            // If the letter entered is not in the secret word...
            if (!window.getWord().contains(input)){
                // "01" is the value returned by getInput if the user just hit "enter" and gave us an empty string.
                if(!input.equals("01")){
                    // If the guess was wrong and not empty, put it in the wrongGuesses array for display
                    window.setWrongGuess(window.failCount, input);
                }
                // Guess was wrong, so increment the fail counter.
                window.failCount++;
           
                // Array of strings to be printed at each wrong guess
                String[] nopes = {"Nope! Here's your head! ",
                                  "Nope! Here's your body! ",
                                  "Nope! Here's your arm! ",
                                  "Nope! Here's your other arm! ",
                                  "Nope! Here's your leg! One more chance! ",
                                  "Last leg! Game over! "};
                
                // Repaint the window (HangCanvas will handle which parts to draw)
                window.repaint();
                // Print the appropriate string
                System.out.print(nopes[window.failCount-1]);
                // If we get to 6 failed guesses, that's a loss. Show the word and add a loss.
                if(window.failCount == 6){
                    window.addLoss();
                }

            }else{
                // If we got here, the entered letter is in the word!
                int i;
                // Only want to print a message for the correctly guessed letter once,
                // regardless how often it appears in the word.
                boolean alreadyPrinted = false;
                // Find the letter every time it appears
                for (i=0; i<window.getWord().length(); i++){
                    String checkLetter = String.valueOf(window.getWord().charAt(i));
                    if (input.equals(checkLetter)){
                        // Set the corresponding position in the correctLetters array to 1
                        // That position in the word can now be drawn
                        window.setCorrectLetter(i);
                        // Check if we printed a message about this letter already
                        if(!alreadyPrinted){
                            System.out.println("Correctly guessed \"" + checkLetter + "\"! ");
                            alreadyPrinted = true;
                        }
                        window.repaint();
                    }                    
                }
                // Now assume the player won for a second...
                boolean didWin = true;
                int[] correctLetters = window.getAllCorrectLetters();
                // ... then check all the letters to see if they've been correctly guessed
                for (i=0; i<correctLetters.length; i++){
                    if (correctLetters[i]==0){
                        // If we found something that has not been correctly guessed, the player didn't win after all.
                        didWin = false;
                        break;
                    }
                }
                // If we made it here with everything correctly guessed, the player has won!
                if (didWin){
                    System.out.print("You win! ");
                    window.addWin();
                    break;
                }
            }
        }
        // GG. Rematch?
        System.out.println("Play again? (y/n)");
        input = getInput(scan);
        // If not "y", we're done here.
        if (!input.toLowerCase().equals("y")){
            System.out.println("OK. Goodbye!");
            System.exit(0);
        }else{
            // Got a "y"? Reset the game state and go again.
            window.resetState();
            window.setKeepPlaying(true);
        }
    }
}
