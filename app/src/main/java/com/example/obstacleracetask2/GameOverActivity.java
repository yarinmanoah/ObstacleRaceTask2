package com.example.obstacleracetask2;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import java.util.Comparator;

public class GameOverActivity extends AppCompatActivity {
        private AppCompatImageView space_IMG_Background;
        private TextView gameOver_LBL_result;
        private EditText gameOver_LBL_name;
        private MaterialButton gameOver_BTN_saveRecord;
        private MaterialButton gameOver_BTN_back;

        private GPSTracker gpsService;

        private MyDB myDB;

        private String playerName;
        private int score;
        private final String background = "https://www.pngall.com/wp-content/uploads/2016/07/Space-PNG-HD.png";




    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game_over);

            GameUtils.hideSystemUI(this);

            score = getIntent().getExtras().getInt("score");

            findView();
            GameUtils.setBackground(this, space_IMG_Background, background);

            gameOver_LBL_result.setText("" + score);
            initView();

        }

        private void initView() {
            gameOver_BTN_back.setOnClickListener(view -> finish());
            gameOver_BTN_saveRecord.setOnClickListener(view -> {
                playerName = gameOver_LBL_name.getText().toString();

                gpsService = new GPSTracker(GameOverActivity.this);
                if (gpsService.canGetLocation()) {
                    gpsService.getLocationMutableLiveData()
                            .observe(this, new Observer<Location>() {
                                @Override
                                public void onChanged(Location location) {
                                    if(location != null) {
                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                        // * End of Location Service
                                        gameOver_LBL_name.setVisibility(View.INVISIBLE);
                                        gameOver_BTN_saveRecord.setVisibility(View.INVISIBLE);
                                        saveRecord(playerName, score, longitude, latitude);
                                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                                        gpsService.removeLocationObservers(GameOverActivity.this);
                                    }
                                }
                            });
                } else {
                    gpsService.showSettingsAlert();
                }
            });
        }

        private void findView() {

            gameOver_LBL_result = findViewById(R.id.gameOver_LBL_result);
            gameOver_LBL_name = findViewById(R.id.gameOver_LBL_name);
            space_IMG_Background = findViewById(R.id.space_IMG_background);
            gameOver_BTN_saveRecord = findViewById(R.id.gameOver_BTN_saveRecord);
            gameOver_BTN_back = findViewById(R.id.gameOver_BTN_back);


        }

        private void saveRecord(String player_name, int score, double longitude, double latitude) {

            String js = MSPV3.getMe().getString("MY_DB", "");
            myDB = new Gson().fromJson(js, MyDB.class);

            myDB.getRecords().add(new Score()
                    .setName(player_name)
                    .setScore(score)
                    .setLat(latitude)
                    .setLon(longitude)
            );

            myDB.getRecords().sort(new SortByScore());
            String json = new Gson().toJson(myDB);
            MSPV3.getMe().putString("MY_DB", json);
        }
    }
    class SortByScore implements Comparator<Score> {
        @Override
        public int compare(Score rec1, Score rec2) {

            return rec2.getScore() - rec1.getScore();
        }
    }

