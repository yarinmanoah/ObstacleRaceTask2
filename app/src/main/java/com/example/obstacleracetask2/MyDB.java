package com.example.obstacleracetask2;

import java.util.ArrayList;

public class MyDB {

    private ArrayList<Score> scores;
    public MyDB() { }

    public ArrayList<Score> getRecords() {
        if(scores == null){
            scores = new ArrayList<>();
        }
        return scores;
    }

}
