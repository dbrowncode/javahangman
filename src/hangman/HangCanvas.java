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
    
    private boolean gameStarted = false;
    private final int gallowsX = 460;
    private final int gallowsY = 40;
    private boolean makeHead = false;
    private boolean makeTorso = false;
    private boolean makeArmL = false;
    private boolean makeArmR = false;
    private boolean makeLegL = false;
    private boolean makeLegR = false;
    private int wordLength = 0;
    private String word = "a";
    private int[] correctLetters;
    private String[] wrongGuesses = new String[6];
    private final Font hangFont = new Font("Courier New", PLAIN, 28);
    private final Font smallerFont = new Font("Courier New", PLAIN, 20);
    private final Font smallestFont = new Font("Courier New", PLAIN, 16);
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
        this.makeHead = false;
        this.makeTorso = false;
        this.makeArmL = false;
        this.makeArmR = false;
        this.makeLegL = false;
        this.makeLegR = false;
        this.wordLength = 0;
        this.word = "a";
        this.wrongGuesses = new String[6];
        this.keepPlaying = false;
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
     * Tell the canvas if it should draw the head.
     * 
     * @param makeHead - set to true if head should be drawn.
     */
    public void setMakeHead(boolean makeHead) {
        this.makeHead = makeHead;
    }



    /**
     * Tell the canvas if it should draw the torso.
     * 
     * @param makeTorso - set to true if torso should be drawn.
     */
    public void setMakeTorso(boolean makeTorso) {
        this.makeTorso = makeTorso;
    }


    /**
     * Tell the canvas if it should draw the left arm.
     *
     * @param makeArmL - set to true if the left arm should be drawn.
     */
    public void setMakeArmL(boolean makeArmL) {
        this.makeArmL = makeArmL;
    }



    /**
     * Tell the canvas if it should draw the right arm.
     *
     * @param makeArmR - set to true if the right arm should be drawn.
     */
    public void setMakeArmR(boolean makeArmR) {
        this.makeArmR = makeArmR;
    }



    /**
     * Tell the canvas if it should draw the left leg.
     * 
     * @param makeLegL - set to true if the left leg should be drawn.
     */
    public void setMakeLegL(boolean makeLegL) {
        this.makeLegL = makeLegL;
    }


    /**
     * Tell the canvas if it should draw the right leg.
     *
     * @param makeLegR - set to true if the right leg should be drawn.
     */
    public void setMakeLegR(boolean makeLegR) {
        this.makeLegR = makeLegR;
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
     * Tell the canvas how long the word is.
     *
     * @param wordLength - length of the secret word.
     */
    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
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
        drawGallows(g, this.gallowsX, this.gallowsY);
        if(!this.gameStarted){
            drawStartScreen(g);
            drawScoreboard(g);
        } else {
            drawDashes(g);
            drawLetters(g);
            drawWrongGuesses(g);
            drawScoreboard(g);

            if(makeHead){
                int halfHead = 20;
                drawHead(g, this.gallowsX, this.gallowsY, halfHead);
                if(makeTorso){
                    drawTorso(g, this.gallowsX, this.gallowsY);
                    if(makeArmL){
                        drawArmL(g, this.gallowsX, this.gallowsY);
                        if(makeArmR){
                            drawArmR(g, this.gallowsX, this.gallowsY);
                            if(makeLegL){
                                drawLegL(g, this.gallowsX, this.gallowsY);
                                if(makeLegR){
                                    drawLegR(g, this.gallowsX, this.gallowsY);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Draws the startup screen on the canvas.
     *
     * @param g - Graphics object.
     */
    private void drawStartScreen(Graphics g){
        g.setColor(Color.WHITE);
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
        g.drawString("Guess only letters a-z.", 25, this.gallowsY+130);
    }
    
    /**
     * Draws the gallows.
     *
     * @param g - Graphics object.
     * @param x - top left x position.
     * @param y - top left y position.
     */
    private void drawGallows (Graphics g, int x, int y){
        g.setColor(Color.WHITE);
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
        g.setColor(Color.WHITE);
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
        g.setColor(Color.WHITE);
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
        g.setColor(Color.WHITE);
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
        g.setColor(Color.WHITE);
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
        g.setColor(Color.WHITE);
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
        g.setColor(Color.WHITE);
        g.drawLine(x, y+130, x+30, y+180);
    }
    
    /**
     * Draws placeholder dashes for un-guessed letters in the word.
     *
     * @param g - Graphics object.
     */
    private void drawDashes(Graphics g){
        g.setColor(Color.WHITE);
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
        g.setColor(Color.WHITE);
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
        g.setColor(Color.WHITE);
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
