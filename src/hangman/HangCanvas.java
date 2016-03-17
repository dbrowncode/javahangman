/*
 * Provides Canvas object for Hangman game, including many fields
 * related to the game state and methods to handle drawing the graphics
 */
package hangman;

import java.awt.*;
import static java.awt.Font.PLAIN;

/**
 *
 * @author Dustin Brown
 * @since March 5, 2016
 */
public class HangCanvas extends Canvas{
    
    // gameStarted will be true when we get past the startup screen
    private boolean gameStarted = false;
    
    // X and Y values for the top-left of the gallows; a lot of the drawing positioning is based on this
    private final int gallowsX = 460;
    private final int gallowsY = 40;
    
    // Maximum number of wrong guesses before a game loss occurs
    private final int MAX_WRONG = 6;
    
    // Public counter of the current number of wrong guesses
    public int failCount = 0;
    
    // How long is the word?
    private int wordLength = 0;
    
    // What is the word? Should always get set elsewhere, arbitrary default here.
    private String word = "a";
    
    // Integer array to represent positions of correctly guessed letters.
    // Initially all 0s; when a letter is correctly guessed, each position it appears in will be set to 1.
    private int[] correctLetters;
    
    // Array to hold the incorrect guesses for display
    private String[] wrongGuesses = new String[this.MAX_WRONG];
    
    // Selection of fonts for the display
    private final Font hangFont = new Font("Courier New", PLAIN, 28);
    private final Font smallerFont = new Font("Courier New", PLAIN, 20);
    private final Font smallestFont = new Font("Courier New", PLAIN, 16);
    
    // Session score tracking and repeat playing options
    private static int numWins = 0;
    private static int numLosses = 0;
    private boolean keepPlaying = false;
    
    /**
     * Default constructor, just does what the parent Canvas would do
     */
    public HangCanvas(){
        super();
    }

    /**
     * Reset main game state variables.
     * Necessary for the "play again" functionality.
     */
    public void resetState(){
        this.gameStarted = false;
        this.wordLength = 0;
        this.word = "a";
        this.wrongGuesses = new String[this.MAX_WRONG];
        this.keepPlaying = false;
        this.failCount = 0;
        this.repaint();
    }

    /**
     * Tell the canvas if the game has started (user selected "play").
     * 
     * @param gameStarted - set to true if we should be past the start screen.
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }  

    /**
     * Return true if the game has started (past start screen).
     *
     * @return isGameStarted - true if we are past the start screen
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Set the word to be guessed in this game.
     *
     * @param word - the secret word.
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Get the word to be guessed
     *
     * @return word - the word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Tell the canvas how long the word is.
     *
     * @param wordLength - length of the secret word.
     */
    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    /**
     * Get the maximum wrong guesses.
     *
     * @return MAX_WRONG - maximum number of incorrect guesses.
     */
    public int getMAX_WRONG() {
        return MAX_WRONG;
    }

    /**
     * Get the array of which letter positions have been correctly guessed.
     *
     * @return correctLetters - array of 0s and 1s, where 1s
     * correspond to positions of correct guesses in the word.
     */
    public int[] getAllCorrectLetters() {
        return correctLetters;
    }

    /**
     * Tell the canvas which letter positions have been correctly guessed.
     *
     * @param correctLetters - array of 0s and 1s, where 1s
     * correspond to positions of correct guesses in the word. 
     */
    public void setAllCorrectLetters(int[] correctLetters) {
        this.correctLetters = correctLetters;
    }

    /**
     * Set an element of the correctLetters array to 1,
     * indicating that the letter at that position in the word
     * has been correctly guessed.
     *
     * @param i - index corresponding to position of correct guess.
     */
    public void setCorrectLetter(int i) {
        this.correctLetters[i] = 1;
    }

    /**
     * Add a letter to the wrongGuesses array, to be displayed
     * under "Incorrect Guesses" during gameplay.
     *
     * @param i - index into the array.
     * @param wrongGuess - letter to add.
     */
    public void setWrongGuess(int i, String wrongGuess) {
        this.wrongGuesses[i] = wrongGuess;
    }

    /**
     * Add 1 to the win column for the scoreboard.
     */
    public static void addWin() {
        HangCanvas.numWins++;
    }

    /**
     * Add 1 to the loss column for the scoreboard.
     */
    public static void addLoss() {
        HangCanvas.numLosses++;
    }

    /**
     * @return numWins - number of wins so far
     */
    public static int getNumWins() {
        return numWins;
    }

    /**
     * @return numLosses - number of losses so far
     */
    public static int getNumLosses() {
        return numLosses;
    }

    /**
     * Find out whether to start another game.
     *
     * @return keepPlaying - will be true if the user wanted to play again.
     */
    public boolean isKeepPlaying() {
        return keepPlaying;
    }

    /**
     * Tell the canvas whether the user wants to play again.
     *
     * @param keepPlaying - true if another game should start.
     */
    public void setKeepPlaying(boolean keepPlaying) {
        this.keepPlaying = keepPlaying;
    }
    
    /**
     * Performs all drawing operations on the canvas.
     *
     * @param g - Graphics object.
     */
    @Override
    public void paint (Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        drawGallows(g, this.gallowsX, this.gallowsY);
        if(!this.gameStarted){
            drawStartScreen(g);
            drawScoreboard(g);
        } else {
            drawDashes(g);
            drawLetters(g);
            drawWrongGuesses(g);
            drawScoreboard(g);
            
            // Switch intentionally will fall through each state if the failCount is high enough
            switch(this.failCount){
                case 6:
                    drawLegR(g, this.gallowsX, this.gallowsY);
                    // At this point the game has been lost; set all letters to correct so we can easily print them.
                    for (int i = 0; i<this.wordLength; i++){
                        setCorrectLetter(i);
                    }
                    drawLetters(g);
                case 5:
                    drawLegL(g, this.gallowsX, this.gallowsY);
                case 4:
                    drawArmR(g, this.gallowsX, this.gallowsY);
                case 3:
                    drawArmL(g, this.gallowsX, this.gallowsY);
                case 2:
                    drawTorso(g, this.gallowsX, this.gallowsY);
                case 1:
                    drawHead(g, this.gallowsX, this.gallowsY, 20);
                default:
                    break;
            }
        }
    }
    
    /**
     * Draws the startup screen on the canvas.
     *
     * @param g - Graphics object.
     */
    private void drawStartScreen(Graphics g){
        g.setFont(this.hangFont);
        int halfHead = 20;
        drawHead(g, this.gallowsX, this.gallowsY, halfHead);
        drawTorso(g, this.gallowsX, this.gallowsY);
        drawArmL(g, this.gallowsX, this.gallowsY);
        drawArmR(g, this.gallowsX, this.gallowsY);
        drawLegL(g, this.gallowsX, this.gallowsY);
        drawLegR(g, this.gallowsX, this.gallowsY);        
        String message = "Play/Quit?";
        int dashWidth = 28;
        int gap = 10;
        int startX = 25;
        int startY = 260;
        int i;
        for (i=0; i<message.length(); i++){            
            startX += (dashWidth + gap) * i;        
            g.drawString(String.valueOf(message.charAt(i)), startX, startY);
            startX = 25;         
        }
        g.setFont(this.smallerFont);
        g.drawString("Welcome to Hangman", 25, this.gallowsY+20);
        g.setFont(this.smallestFont);
        g.drawString("Rules of the game:", 25, this.gallowsY+50);
        g.drawString("Guess the secret word, one letter", 25, this.gallowsY+70);
        g.drawString("at a time. Bad guesses will add", 25, this.gallowsY+90);
        g.drawString("body parts to the gallows!", 25, this.gallowsY+110);
        g.drawString("Words consist of 5-10 lowercase", 25, this.gallowsY+130);
        g.drawString("letters from a-z.", 25, this.gallowsY+150);
    }
    
    /**
     * Draws the gallows.
     *
     * @param g - Graphics object.
     * @param x - top left x position.
     * @param y - top left y position.
     */
    private void drawGallows (Graphics g, int x, int y){
        // Top line
        g.drawLine(x, y, x+100, y);
        // Main pole
        g.drawLine(x+100, y, x+100, y+200);
        // Noose thing?
        g.drawLine(x, y, x, y+20);
        // Base
        g.drawRect(x-40, y+200, 160, 30);
    }
    
    /**
     * Draws the head.
     *
     * @param g - Graphics object.
     * @param x - centre x position.
     * @param y - centre y position.
     * @param half - radius of the circle (half size);
     */
    private void drawHead(Graphics g, int x, int y, int half){
        g.drawOval(x-half, y+half, half*2, half*2);
    }

    /**
     * Draws the torso.
     *
     * @param g - Graphics object.
     * @param x - starting (top) x position.
     * @param y - starting (top) y position.
     */
    private void drawTorso(Graphics g, int x, int y){
        g.drawLine(x, y+60, x, y+130);
    }

    /**
     * Draws left arm.
     *
     * @param g - Graphics object.
     * @param x - starting x position.
     * @param y - starting y position.
     */
    private void drawArmL(Graphics g, int x, int y){
        g.drawLine(x, y+65, x-40, y+90);
    }

    /**
     * Draws right arm.
     *
     * @param g - Graphics object.
     * @param x - starting x position.
     * @param y - starting y position.
     */
    private void drawArmR(Graphics g, int x, int y){
        g.drawLine(x, y+65, x+40, y+90);
    }

    /**
     * Draws left leg.
     *
     * @param g - Graphics object.
     * @param x - starting x position.
     * @param y - starting y position.
     */
    private void drawLegL(Graphics g, int x, int y){
        g.drawLine(x, y+130, x-30, y+180);
    }

    /**
     * Draws right leg.
     *
     * @param g - Graphics object.
     * @param x - starting x position.
     * @param y - starting y position.
     */
    private void drawLegR(Graphics g, int x, int y){
        g.drawLine(x, y+130, x+30, y+180);
    }
    
    /**
     * Draws placeholder dashes for un-guessed letters in the word.
     *
     * @param g - Graphics object.
     */
    private void drawDashes(Graphics g){
        int dashWidth = 28;
        int gap = 10;
        int startX = 20;
        int startY = 270;
        int i;
        for (i=0; i<this.wordLength; i++ ){
            g.drawLine(startX, startY, startX+dashWidth, startY);
            startX += dashWidth + gap;
        }
    }
    
    /**
     * Draws all letters in the word that have been correctly guessed.
     *
     * @param g - Graphics object.
     */
    private void drawLetters(Graphics g){
        g.setFont(this.hangFont);
        String[] letters = new String[this.wordLength];
        int dashWidth = 28;
        int gap = 10;
        int startX = 25;
        int startY = 260;
        int i;
        for (i=0; i<this.wordLength; i++){            
            if (this.getAllCorrectLetters()[i] == 1){
                startX += (dashWidth + gap) * i;
                letters[i] = String.valueOf(this.word.charAt(i));            
                g.drawString(letters[i], startX, startY);
                startX = 25;
            }            
        }       
    }
    
    /**
     * Draws the letters that have been guessed and are not in the word.
     *
     * @param g - Graphics object.
     */
    private void drawWrongGuesses(Graphics g){
        g.setFont(this.smallerFont);
        int dashWidth = 20;
        int gap = 10;
        int startX = 25;
        int startY = 100;
        int i;
        g.drawString("Incorrect guesses:", 25, this.gallowsY+20);
        g.drawRoundRect(20, this.gallowsY, 300, 100, 15, 15);
        for (i=0; i<this.wrongGuesses.length; i++){
            if (this.wrongGuesses[i] != null){
                startX += (dashWidth + gap) * i;           
                g.drawString(this.wrongGuesses[i], startX, startY);
                startX = 25;
            }
        }      
    }
    
    /**
     * Draws the scoreboard of wins and losses for this session.
     *
     * @param g - Graphics object.
     */
    private void drawScoreboard(Graphics g){
        g.setFont(this.smallerFont);
        g.drawString("W", this.gallowsX-90, this.gallowsY+15);
        g.drawString("L", this.gallowsX-60, this.gallowsY+15);
        g.drawString(String.valueOf(numWins), this.gallowsX-90, this.gallowsY+38);
        g.drawString(String.valueOf(numLosses), this.gallowsX-60, this.gallowsY+38);
        g.drawLine(this.gallowsX-100, this.gallowsY+20, this.gallowsX-40, this.gallowsY+20);
        g.drawLine(this.gallowsX-70, this.gallowsY, this.gallowsX-70, this.gallowsY+40);
    }
  
}
