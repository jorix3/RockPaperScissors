package fi.tamk.jorix3.kps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * KPS
 *
 * @author Jyri Virtaranta jyri.virtaranta@cs.tamk.fi
 * @version 2018.02.12
 * @since 1.8
 */
public class GameActivity extends AppCompatActivity
        implements SelectDialogFragment.SelectDialogListener,
                    GameOverDialogFragment.GameOverDialogListener{
    
    private SoundPool soundPool;
    private boolean loaded = false;
    private int soundWin;
    private int soundDraw;
    private int soundLose;
    private float volume;
    private final int MAX_ROUNDS = 10;
    private int currentRound = 0;
    private int playerChoice;
    private int computerChoice;
    private final String PREFERENCES = "MyPreferences";
    private SharedPreferences preferences;
    private SortedSet<Score> highScores;
    private int wins = 0;
    private int draws = 0;
    private int losses = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);
        preferences = getSharedPreferences(PREFERENCES,0);
        highScores = new TreeSet<>();
        
        if (BuildConfig.DEBUG) {
            currentRound = 7;
        }
        
        readHighScoresFromFile();
        loadSounds();
        showSelectDialog();
    }
    
    public void readHighScoresFromFile() {
        String data = "";
        String filename = "kpsHighScores";
        FileInputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        
        try {
            inputStream = openFileInput(filename);
            
            if (inputStream != null) {
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String nextLine;
                
                while ((nextLine = bufferedReader.readLine()) != null) {
                    data += nextLine;
                }
                
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("GameActivity", "File not found error.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("GameActivity", "Cannot read file error.");
        }
    
        if (data.contains(";;") && data.contains("::")) {
            String[] rows = data.split("::");
        
            for (String row : rows) {
                Log.d("GameActivity", "readRows: " + row);
                String[] score = row.split(";;");
                highScores.add(new Score(score[0],
                        Integer.parseInt(score[1]),
                        Integer.parseInt(score[2]),
                        Integer.parseInt(score[3])));
            }
        }
    }
    
    public void writeHighScoresToFile(String name) {
        name = name.replaceAll("[^A-Za-z0-9]", "");
        highScores.add(new Score(name, wins, draws, losses));
        String data = "";
        String filename = "kpsHighScores";
        FileOutputStream outputStream;
        
        if (highScores.size() > 5) {
            highScores.remove(highScores.last());
        }
        
        for (Score score : highScores) {
            data += score.getName() + ";;"
                    + score.getWins() + ";;"
                    + score.getDraws() + ";;"
                    + score.getLosses() + "::";
        }
        
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        Log.d("GameActivity", "writeData: " + data);
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }
    
    // not actually used.
    public void readHighScoresFromPreferences() {
        String data = preferences.getString("highScore", "");
        Log.d("GameActivity", "score: " + data);
        
        if (data.contains(";;") && data.contains("::")) {
            String[] rows = data.split("::");
            
            for (String row : rows) {
                Log.d("GameActivity", "writeRows: " + row);
                String[] score = row.split(";;");
                highScores.add(new Score(score[0],
                        Integer.parseInt(score[1]),
                        Integer.parseInt(score[2]),
                        Integer.parseInt(score[3])));
            }
        }
        
        Log.d("GameActivity", "highScores: " + highScores.toString());
    }
    
    // not actually used.
    public void writeHighScoresToPreferences(String name) {
        name = name.replaceAll("[^A-Za-z0-9]", "");
        highScores.add(new Score(name, wins, draws, losses));
        String data = "";

        if (highScores.size() > 5) {
            highScores.remove(highScores.last());
        }

        for (Score score : highScores) {
            data += score.getName() + ";;"
                    + score.getWins() + ";;"
                    + score.getDraws() + ";;"
                    + score.getLosses() + "::";
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("highScore", data);
        editor.commit();
        Log.d("GameActivity", "writeScore: " + data);
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }
    
    private void loadSounds() {
        soundPool = new SoundPool(10, AudioManager.STREAM_NOTIFICATION, 0);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            loaded = true;
        });
    
        soundLose = soundPool.load(this, R.raw.fuck_you, 1);
        soundWin = soundPool.load(this, R.raw.hiaa, 1);
        soundDraw = soundPool.load(this, R.raw.tutturuu, 1);
        
        AudioManager audioManager = (AudioManager)
                getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        volume = actualVolume / maxVolume;
    }
    
    private void checkForWin() {
        TextView textField = findViewById(R.id.textField);
        int rock = R.drawable.rock;
        int paper = R.drawable.paper;
        int scissors = R.drawable.scissors;
        
        if (computerChoice == playerChoice) {
            textField.setText("DRAW");
            draws++;
            
            if (loaded) {
                soundPool.play(soundDraw, volume, volume, 1, 0, 1f);
            }
        } else if ((computerChoice == rock && playerChoice == paper) ||
                (computerChoice == paper && playerChoice == scissors) ||
                (computerChoice == scissors && playerChoice == rock)) {
            textField.setText("YOU WON");
            wins++;
            
            if (loaded) {
                soundPool.play(soundWin, volume, volume, 1, 0, 1f);
            }
        } else {
            textField.setText("YOU LOST");
            losses++;
            
            if (loaded) {
                soundPool.play(soundLose, volume, volume, 1, 0, 1f);
            }
        }
    }
    
    private void buildBoard() {
        TextView textField = findViewById(R.id.textField);
        ImageView computerImage = findViewById(R.id.computer);
        ImageView playerImage = findViewById(R.id.player);
    
        textField.setText("Game " + (currentRound + 1) + "/10");
        computerImage.setImageResource(computerChoice);
        playerImage.setImageResource(playerChoice);
    
        computerImage.setRotation(90);
        playerImage.setRotation(-90);
    
        Animation computerAnim =
                AnimationUtils.loadAnimation(this, R.anim.computer_animation);
        Animation playerAnim =
                AnimationUtils.loadAnimation(this, R.anim.player_animation);
    
        computerImage.startAnimation(computerAnim);
        playerImage.startAnimation(playerAnim);
    }
    
    private int randomNumber(int min, int max) {
        int multiplier = (max - min) + 1;
        return (int)(Math.random() * multiplier + min);
    }
    
    public int getImg(int id) {
        switch (id) {
            case 0:
                return R.drawable.rock;
            case 1:
                return R.drawable.paper;
            default: return R.drawable.scissors;
        }
    }
    
    public void showSelectDialog() {
        if (currentRound < MAX_ROUNDS) {
            Log.d("GameActivity","round: " + currentRound);
            DialogFragment dialog = new SelectDialogFragment();
            dialog.show(getSupportFragmentManager(), "SelectDialogFragment");
        } else {
            gameOver();
        }
    }
    
    @Override
    public void onSelectClick(DialogFragment dialog, int id) {
        Log.d("GameActivity", "selection: " + id);
        playerChoice = getImg(id);
        computerChoice = getImg(randomNumber(0, 2));
        
        buildBoard();
        (new Handler()).postDelayed(this::checkForWin, 3000);
        currentRound++;
        (new Handler()).postDelayed(this::showSelectDialog, 4000);
    }
    
    @Override
    public void onGameOverClick(DialogFragment dialog, String name) {
        Log.d("GameActivity", "game over: " + name);
        writeHighScoresToFile(name);
    }
    
    public void gameOver() {
        Log.d("GameActivity","last round: " + currentRound);
        DialogFragment dialog = new GameOverDialogFragment();
        dialog.show(getSupportFragmentManager(), "GameOverDialogFragment");
    }
}
