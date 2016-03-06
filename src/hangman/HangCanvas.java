/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import java.awt.*;
import static java.awt.Font.PLAIN;

/**
 *
 * @author meisb
 */
public class HangCanvas extends Canvas{
    
    private int gallowsX = 460;
    private int gallowsY = 40;
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
    private Font hangfont = new Font("Courier New", PLAIN, 28);
    private Font smallerfont = new Font("Courier New", PLAIN, 20);
    private static int numWins = 0;
    private static int numLosses = 0;
    
    public HangCanvas(){
        super();
    }

    public int getGallowsX() {
        return gallowsX;
    }

    public void setGallowsX(int gallowsX) {
        this.gallowsX = gallowsX;
    }

    public int getGallowsY() {
        return gallowsY;
    }

    public void setGallowsY(int gallowsY) {
        this.gallowsY = gallowsY;
    }

    public boolean isMakeHead() {
        return makeHead;
    }

    public void setMakeHead(boolean makeHead) {
        this.makeHead = makeHead;
    }
    public boolean isMakeTorso() {
        return makeTorso;
    }

    public void setMakeTorso(boolean makeTorso) {
        this.makeTorso = makeTorso;
    }

    public boolean isMakeArmL() {
        return makeArmL;
    }

    public void setMakeArmL(boolean makeArmL) {
        this.makeArmL = makeArmL;
    }

    public boolean isMakeArmR() {
        return makeArmR;
    }

    public void setMakeArmR(boolean makeArmR) {
        this.makeArmR = makeArmR;
    }

    public boolean isMakeLegL() {
        return makeLegL;
    }

    public void setMakeLegL(boolean makeLegL) {
        this.makeLegL = makeLegL;
    }

    public boolean isMakeLegR() {
        return makeLegR;
    }

    public void setMakeLegR(boolean makeLegR) {
        this.makeLegR = makeLegR;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getWordLength() {
        return wordLength;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public int[] getAllCorrectLetters() {
        return correctLetters;
    }

    public void setAllCorrectLetters(int[] correctLetters) {
        this.correctLetters = correctLetters;
    }
    
    public boolean isLetterCorrect(int i) {
        return (correctLetters[i] == 1);
    }

    public void setCorrectLetter(int i) {
        this.correctLetters[i] = 1;
    }

    public String[] getWrongGuesses() {
        return wrongGuesses;
    }

    public void setWrongGuess(int i, String wrongGuess) {
        this.wrongGuesses[i] = wrongGuess;
    }

    public static void addWin() {
        HangCanvas.numWins++;
    }

    public static void addLoss() {
        HangCanvas.numLosses++;
    }
    
    
    @Override
    public void paint (Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());        
        drawGallows(g, this.gallowsX, this.gallowsY);
        drawDashes(g);
        drawLetters(g);
        drawWrongGuesses(g);
//        drawScoreboard(g);
//      ^ only call this if implementing playAgain logic (bonus)
        
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
    
    private void drawHead(Graphics g, int x, int y, int half){
        g.setColor(Color.WHITE);
        g.drawOval(x-half, y+half, half*2, half*2);
    }
    private void drawTorso(Graphics g, int x, int y){
        g.setColor(Color.WHITE);
        g.drawLine(x, y+60, x, y+130);
    }
    private void drawArmL(Graphics g, int x, int y){
        g.setColor(Color.WHITE);
        g.drawLine(x, y+65, x-40, y+90);
    }
    private void drawArmR(Graphics g, int x, int y){
        g.setColor(Color.WHITE);
        g.drawLine(x, y+65, x+40, y+90);
    }
    private void drawLegL(Graphics g, int x, int y){
        g.setColor(Color.WHITE);
        g.drawLine(x, y+130, x-30, y+180);
    }
    private void drawLegR(Graphics g, int x, int y){
        g.setColor(Color.WHITE);
        g.drawLine(x, y+130, x+30, y+180);
    }
    
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
    
    private void drawLetters(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(this.hangfont);
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
    
    private void drawWrongGuesses(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(this.smallerfont);
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
    
    private void drawScoreboard(Graphics g){
        g.setFont(this.smallerfont);
        g.drawString("W", this.gallowsX-90, this.gallowsY+15);
        g.drawString("L", this.gallowsX-60, this.gallowsY+15);
        g.drawString(String.valueOf(numWins), this.gallowsX-90, this.gallowsY+38);
        g.drawString(String.valueOf(numLosses), this.gallowsX-60, this.gallowsY+38);
        g.drawLine(this.gallowsX-100, this.gallowsY+20, this.gallowsX-40, this.gallowsY+20);
        g.drawLine(this.gallowsX-70, this.gallowsY, this.gallowsX-70, this.gallowsY+40);
    }
  
}
