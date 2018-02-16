package fi.tamk.jorix3.kps;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * KPS
 *
 * @author Jyri Virtaranta jyri.virtaranta@cs.tamk.fi
 * @version 2018.02.16
 * @since 1.8
 */
public class HighScoreActivity extends AppCompatActivity {
    private final String PREFERENCES = "MyPreferences";
    private SharedPreferences preferences;
    private String score;
    private SortedSet<Score> highScores;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        highScores = new TreeSet<>();
    
        readHighScoresFromFile();
        //addTestData();
        populateList();
    }
    
    public void addTestData() {
        highScores.add(new Score("A", 3,3,3));
        highScores.add(new Score("B", 3,3,3));
        highScores.add(new Score("C", 3,3,3));
        highScores.add(new Score("win1", 1,1,1));
        highScores.add(new Score("win2", 2,1,1));
        highScores.add(new Score("win3", 3,1,1));
        highScores.add(new Score("draw1", 1,1,1));
        highScores.add(new Score("draw2", 1,2,1));
        highScores.add(new Score("draw3", 1,3,1));
        highScores.add(new Score("loss1", 1,1,1));
        highScores.add(new Score("loss2", 1,1,2));
        highScores.add(new Score("loss3", 1,1,3));
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
                Log.d("GameActivity", "rows: " + row);
                String[] score = row.split(";;");
                highScores.add(new Score(score[0],
                        Integer.parseInt(score[1]),
                        Integer.parseInt(score[2]),
                        Integer.parseInt(score[3])));
            }
        }
    }
    
    // not actually used.
    public void readHighScoresFromPreferences() {
        preferences = getSharedPreferences(PREFERENCES,0);
        score = preferences.getString("highScore", "");
        Log.d("HighScoreActivity", "score: " + score);
        
        if (score.contains(";;") && score.contains("::")) {
            String[] rows = score.split("::");
            
            for (String row : rows) {
                Log.d("HighScoreActivity", "rows: " + row);
                String[] data = row.split(";;");
                highScores.add(new Score(data[0],
                        Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3])));
            }
        }
        
        Log.d("HighScoreActivity", "highScores: " + highScores.toString());
    }
    
    public void populateList() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyScoreAdapter(highScores);
        recyclerView.setAdapter(adapter);
    }
}
