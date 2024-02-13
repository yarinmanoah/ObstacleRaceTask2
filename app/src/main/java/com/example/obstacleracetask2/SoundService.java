package com.example.obstacleracetask2;

import android.content.Context;
import android.media.SoundPool;

public class SoundService {
    private SoundPool soundPool;
    private int mySound;
    public SoundService(Context context) {
        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .build();
        mySound = soundPool.load(context, R.raw.crash, 1);
    }
    public void makeSound() {
           soundPool.play(mySound,1,1,1,0,1);
    }
}
