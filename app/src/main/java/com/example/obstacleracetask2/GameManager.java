package com.example.obstacleracetask2;

import java.util.Arrays;
import java.util.Random;

public class GameManager {
    public static final int MAX_LIVES = 3,COLUMNS = 5,ROWS = 5;
    public boolean[] lifes;
    public boolean[][] activeIos, activeGifts;
    private int lives = MAX_LIVES, AndroidIndex;
    boolean isHit, finish,gift;

    public GameManager() {
        AndroidIndex =2;
        activeIos =new boolean[ROWS][COLUMNS];
        activeGifts=new boolean[ROWS][COLUMNS];
        initLives();
    }
    public void initLives(){
        lifes=new boolean[MAX_LIVES];
        Arrays.fill(lifes, true);
    }
    public void reduceLives() {
        lives--;
    }
    public  int getROWS() {
        return ROWS;
    }
    public  int getCOLUMNS() {
        return COLUMNS;
    }
    public int getRandom(){
        Random rand = new Random();
        return rand.nextInt(COLUMNS);
    }
    public boolean isActive(int row,int col) {
        return activeIos[row][col];
    }
    public boolean isGiftActive(int row,int col) {
        return activeGifts[row][col];
    }
    public void updateGame() {
        for (int i = getROWS()-1; i >=0;i--){
            for (int j = 0; j <getCOLUMNS();j++){
                if( isActive(i,j) && i == getROWS()-1) {
                    activeIos[i][j] = false;

                    if (j == AndroidIndex) {
                        isHit = true;
                        reduceLives();
                        lifes[lives] = false;
                        if (lives == 0)
                            finish = true;
                    }
                }
                else if(i != getROWS()-1){
                    activeIos[i+1][j]= activeIos[i][j];
                }
            }
        }
    }
    public void updateGifts() {
        for (int i = getROWS()-1; i >=0;i--){
            for (int j = 0; j <getCOLUMNS();j++){
                if( isGiftActive(i,j) && i == getROWS()-1) {
                    activeGifts[i][j] = false;
                    if (j == AndroidIndex) {
                        gift=true;
                    }
                }
                else if(i != getROWS()-1){
                    activeGifts[i+1][j]=activeGifts[i][j];
                }
            }
        }
    }
    public void updateNew(){
        int col =getRandom();
        for(int i = 0;i<getCOLUMNS();i++)
            activeIos[0][i] = col == i;
        int colForGift = getRandom();
        while(col == colForGift){
            colForGift=getRandom();
        }
        for(int i = 0;i<getCOLUMNS();i++)
            activeGifts[0][i] = colForGift == i;
    }
    public void update(){
        updateGame();
        updateGifts();
        updateNew();
    }
    public void setHit(boolean hit) {
        isHit = hit;
    }
    public boolean[] getLifes() {
        return lifes;
    }
    public boolean isFinish() {
        return finish;
    }
    public void setAndroidIndex(int androidIndex) {
        this.AndroidIndex = androidIndex;
    }
    public int getAndroidIndex() {
        return AndroidIndex;
    }
    public void setGiftHit(boolean hit) {
        gift = hit;
    }
    public boolean isGift() {
        return gift;
    }
}
