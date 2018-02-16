package fi.tamk.jorix3.kps;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView titleText = findViewById(R.id.title);
        Button startButton = findViewById(R.id.start);
        Button scoreButton = findViewById(R.id.high_score);
        
        Animation titleAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        
        titleText.startAnimation(titleAnimation);
        buttonPositionAnimation(startButton);
        buttonPositionAnimation(scoreButton);
    }
    
    public void buttonPositionAnimation(Button button) {
        Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "x", 150);
        button.startAnimation(buttonAnimation);
        animator.setDuration(5000);
        animator.start();
    }
    
    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    
    public void highScore(View view) {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }
}
