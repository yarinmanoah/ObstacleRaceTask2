package com.example.obstacleracetask2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {
    private AppCompatImageButton game_BTN_left, game_BTN_right;
    private ImageView[] game_IMG_player;
    private ImageView[][] game_IMG_ios, game_IMG_good;
    private ShapeableImageView[] game_IMG_hearts;
    private AppCompatImageView space_IMG_background;
    private TextView game_TXT_Score, game_TXT_Odometer;
    public static int STEP = 1, score = 0, Odometer = 0, DELAY = 2000;
    final Handler handler = new Handler();
    private GameManager gameManager;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean sensorMode = false, slowMode = false, fastMode = false;
    public static final String SENSOR_MODE = "SENSOR_MODE", SLOW_MODE = "SLOW_MODE", FAST_MODE = "FAST_MODE", background = "https://www.pngall.com/wp-content/uploads/2016/07/Space-PNG-HD.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameUtils.hideSystemUI(this);
        findView();
        GameUtils.setBackground(this, space_IMG_background, background);

        gameManager = new GameManager();
        sensorMode = getIntent().getExtras().getBoolean(SENSOR_MODE);
        slowMode = getIntent().getExtras().getBoolean(SLOW_MODE);
        fastMode = getIntent().getExtras().getBoolean(FAST_MODE);

        if (sensorMode)
            moveAndroidBySensors();
        else {
            if (fastMode) {
                DELAY = 500;
            }
            if (slowMode) {
                DELAY = 2500;
            }
            moveCarByButtons();
        }
        startTimer();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        if (sensorMode) {
            stop();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        startTimer();
        if (sensorMode) {
            start();
        }
    }
    private void findView() {
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        space_IMG_background = findViewById(R.id.space_IMG_background);
        game_TXT_Score = findViewById(R.id.game_TXT_score);
        game_TXT_Odometer = findViewById(R.id.game_TXT_odometer);

        initGoodArr();
        initIosArr();
        initHeartArr();
        initAndroidArr();
    }
    private void initAndroidArr() {
        game_IMG_player = new ImageView[]{
                findViewById(R.id.game_IMG_Player1),
                findViewById(R.id.game_IMG_Player2),
                findViewById(R.id.game_IMG_Player3),
                findViewById(R.id.game_IMG_Player4),
                findViewById(R.id.game_IMG_Player5),
        };
    }
    private void initHeartArr() {
        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart3),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart1),
        };
    }
    private void initIosArr() {
        game_IMG_ios = new ImageView[][]{
                {findViewById(R.id.game_IMG_Ios1),
                        findViewById(R.id.game_IMG_Ios2),
                        findViewById(R.id.game_IMG_Ios3),
                        findViewById(R.id.game_IMG_Ios4),
                        findViewById(R.id.game_IMG_Ios5)},
                {findViewById(R.id.game_IMG_Ios6),
                        findViewById(R.id.game_IMG_Ios7),
                        findViewById(R.id.game_IMG_Ios8),
                        findViewById(R.id.game_IMG_Ios9),
                        findViewById(R.id.game_IMG_Ios10)},
                {findViewById(R.id.game_IMG_Ios11),
                        findViewById(R.id.game_IMG_Ios12),
                        findViewById(R.id.game_IMG_Ios13),
                        findViewById(R.id.game_IMG_Ios14),
                        findViewById(R.id.game_IMG_Ios15)},
                {findViewById(R.id.game_IMG_Ios16),
                        findViewById(R.id.game_IMG_Ios17),
                        findViewById(R.id.game_IMG_Ios18),
                        findViewById(R.id.game_IMG_Ios19),
                        findViewById(R.id.game_IMG_Ios20)},
                {findViewById(R.id.game_IMG_Ios21),
                        findViewById(R.id.game_IMG_Ios22),
                        findViewById(R.id.game_IMG_Ios23),
                        findViewById(R.id.game_IMG_Ios24),
                        findViewById(R.id.game_IMG_Ios25)}
        };
    }
    private void initGoodArr() {
        game_IMG_good = new ImageView[][]{
                {findViewById(R.id.game_IMG_gift1),
                        findViewById(R.id.game_IMG_gift2),
                        findViewById(R.id.game_IMG_gift3),
                        findViewById(R.id.game_IMG_gift4),
                        findViewById(R.id.game_IMG_gift5)},
                {findViewById(R.id.game_IMG_gift6),
                        findViewById(R.id.game_IMG_gift7),
                        findViewById(R.id.game_IMG_gift8),
                        findViewById(R.id.game_IMG_gift9),
                        findViewById(R.id.game_IMG_gift10)},
                {findViewById(R.id.game_IMG_gift11),
                        findViewById(R.id.game_IMG_gift12),
                        findViewById(R.id.game_IMG_gift13),
                        findViewById(R.id.game_IMG_gift14),
                        findViewById(R.id.game_IMG_gift15)},
                {findViewById(R.id.game_IMG_gift16),
                        findViewById(R.id.game_IMG_gift17),
                        findViewById(R.id.game_IMG_gift18),
                        findViewById(R.id.game_IMG_gift19),
                        findViewById(R.id.game_IMG_gift20)},
                {findViewById(R.id.game_IMG_gift21),
                        findViewById(R.id.game_IMG_gift22),
                        findViewById(R.id.game_IMG_gift23),
                        findViewById(R.id.game_IMG_gift24),
                        findViewById(R.id.game_IMG_gift25)}
        };
    }
    private void refreshHearts() {
        boolean[] lifes = gameManager.getLifes();
        for (int i = 0; i < lifes.length; i++) {
            if (lifes[i])
                game_IMG_hearts[i].setVisibility(View.VISIBLE);
            else
                game_IMG_hearts[i].setVisibility(View.INVISIBLE);
        }
    }
    private void refreshIosUI() {
        for (int i = 0; i < gameManager.getROWS(); i++) {
            for (int j = 0; j < gameManager.getCOLUMNS(); j++) {
                if (gameManager.isActive(i, j))
                    game_IMG_ios[i][j].setVisibility(View.VISIBLE);
                else game_IMG_ios[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }
    private void refreshGoodUI() {
        for (int i = 0; i < gameManager.getROWS(); i++) {
            for (int j = 0; j < gameManager.getCOLUMNS(); j++) {
                if (gameManager.isGiftActive(i, j)) {
                    game_IMG_good[i][j].setVisibility(View.VISIBLE);
                } else {
                    game_IMG_good[i][j].setVisibility(View.INVISIBLE);
                }
            }
        }
    }
    private void refreshUI() {
        gameManager.update();
        if (gameManager.isFinish()) {
            GameUtils.vibrate(this);
            refreshHearts();
            GameUtils.toast(this, "GAME OVER");
            Intent gameOverIntent = new Intent(MainActivity.this, GameOverActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("score", score);
            gameOverIntent.putExtras(bundle);
            startActivity(gameOverIntent);
            this.finish();
            stopTimer();
            finish();
        } else {
            if (gameManager.isHit) {
                refreshHearts();
                SoundService sound = new SoundService(getBaseContext());
                sound.makeSound();
                GameUtils.toast(this, "HIT!");
                GameUtils.vibrate(this);
                gameManager.setHit(false);
            } else if (gameManager.isGift()) {
                score += 100;
                game_TXT_Score.setText("Score: " + score);
                GameUtils.toast(this, "GOOD! +100");
                GameUtils.vibrate(this);
                gameManager.setGiftHit(false);
            }
        }
        refreshGoodUI();
        refreshIosUI();
    }
    Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(this, DELAY);
            System.out.println(DELAY);
            Odometer += STEP;
            game_TXT_Odometer.setText("Odometer: " + Odometer);
            refreshUI();
        }
    };
    private void startTimer() {
        handler.postDelayed(runnable, DELAY);
        if (sensorMode) {
            start();
        }
    }
    private void stopTimer() {
        handler.removeCallbacks(runnable);
        if (sensorMode) {
            stop();
        }
    }
    //// BUTTON MODE ///
    private void moveCarByButtons() {
        game_BTN_left.setOnClickListener(v -> {
            if (gameManager.getAndroidIndex() != 0) {
                game_IMG_player[gameManager.getAndroidIndex()].setVisibility(View.INVISIBLE);
                game_IMG_player[gameManager.getAndroidIndex() - 1].setVisibility(View.VISIBLE);
                gameManager.setAndroidIndex(gameManager.getAndroidIndex() - 1);
            }
        });
        game_BTN_right.setOnClickListener(view -> {
            if (gameManager.getAndroidIndex() != gameManager.getCOLUMNS() - 1) {
                game_IMG_player[gameManager.getAndroidIndex()].setVisibility(View.INVISIBLE);
                game_IMG_player[gameManager.getAndroidIndex() + 1].setVisibility(View.VISIBLE);
                gameManager.setAndroidIndex(gameManager.getAndroidIndex() + 1);
            }
        });
    }

    /// SENSOR MODE ///
    private void moveAndroidBySensors() {
        game_BTN_right.setVisibility(View.INVISIBLE);
        game_BTN_left.setVisibility(View.INVISIBLE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            moveAndroidBySensors(sensorEvent.values[0]);

            // BONUS//
            // Handling tilt for speed adjustment
            float y = sensorEvent.values[1]; // Y-axis value for forward/backward tilt
            changeSpeedByTilt(y);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    public void start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }
    private void moveAndroidBySensors(float x) {
        game_IMG_player[gameManager.getAndroidIndex()].setVisibility(View.INVISIBLE);
        if (x < -4) {
            gameManager.setAndroidIndex(4);
        } else if (-3.5 < x && x < -1.5) {
            gameManager.setAndroidIndex(3);
        } else if (-1 < x && x < 1) {
            gameManager.setAndroidIndex(2);
        } else if (1.5 < x && x < 3.5) {
            gameManager.setAndroidIndex(1);
        } else if (4 < x) {
            gameManager.setAndroidIndex(0);
        }
        game_IMG_player[gameManager.getAndroidIndex()].setVisibility(View.VISIBLE);
    }
    private void changeSpeedByTilt(float y) {
        // Define thresholds for tilt sensitivity
        final float FORWARD_TILT_THRESHOLD = 2.0f; // Adjust these values based on desired sensitivity
        final float BACKWARD_TILT_THRESHOLD = -2.0f;
        // Speed up if tilted forwardDELAY
        if (y > FORWARD_TILT_THRESHOLD) {
            DELAY = Math.min(DELAY + 100, 2500); // Increase delay to slow down, with a maximum delay limit
        }
        // Slow down if tilted backward
        else if (y < BACKWARD_TILT_THRESHOLD) {
            DELAY = Math.max(DELAY - 100, 500); // Decrease delay to speed up, with a minimum delay limit
        }
    }
}


