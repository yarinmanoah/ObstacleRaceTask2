package com.example.obstacleracetask2;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;

public class GameMenu extends AppCompatActivity {
    private MaterialButton menu_BTN_SlowButton;
    private MaterialButton menu_BTN_FastButton;

    private MaterialButton menu_BTN_sensor;
    private MaterialButton menu_BTN_highScores;
    private AppCompatImageView menu_IMG_background;
    private final String background = "https://www.pngall.com/wp-content/uploads/2016/07/Space-PNG-HD.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_menu);
        GameUtils.hideSystemUI(this);
        findView();
        GameUtils.setBackground(this,menu_IMG_background, background);
        clicked();

    }

    private void clicked() {
        menu_BTN_SlowButton.setOnClickListener(view -> moveToGame(false,true,false));
        menu_BTN_FastButton.setOnClickListener(view -> moveToGame(false,false,true));
        menu_BTN_sensor.setOnClickListener(view -> moveToGame(true,false,false));
        menu_BTN_highScores.setOnClickListener(view -> moveToScores());
    }

    private void findView() {
        menu_BTN_SlowButton = findViewById(R.id.menu_BTN_slowbutton);
        menu_BTN_FastButton = findViewById(R.id.menu_BTN_fastbutton);
        menu_BTN_sensor = findViewById(R.id.menu_BTN_sensor);
        menu_BTN_highScores = findViewById(R.id.menu_BTN_highScores);
        menu_IMG_background = findViewById(R.id.menu_IMG_background);

    }
    public void moveToGame(boolean sensorMode,boolean slowMode,boolean FastMode) {
        Intent gameIntent = new Intent(this, MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean(MainActivity.SENSOR_MODE, sensorMode);
        bundle.putBoolean(MainActivity.SLOW_MODE, slowMode);
        bundle.putBoolean(MainActivity.FAST_MODE, FastMode);

        gameIntent.putExtras(bundle);
        startActivity(gameIntent);
    }
    public void moveToScores() {
        Intent ScoreIntent = new Intent(this, Top10Activity.class);

        startActivity(ScoreIntent);
    }
}
