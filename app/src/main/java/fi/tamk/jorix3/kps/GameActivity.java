package fi.tamk.jorix3.kps;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * KPS
 *
 * @author Jyri Virtaranta jyri.virtaranta@cs.tamk.fi
 * @version 2018.02.12
 * @since 1.8
 */
public class GameActivity extends AppCompatActivity
        implements SelectDialogFragment.SelectDialogListener{
    private SoundPool soundPool;
    private boolean loaded = false;
    private int soundWin;
    private int soundDraw;
    private int soundLose;
    private float volume;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);
        loadSounds();
        showSelectDialog();
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
    
    private void checkForWin(int computer, int player) {
        TextView textField = findViewById(R.id.textField);
        int rock = R.drawable.rock;
        int paper = R.drawable.paper;
        int scissors = R.drawable.scissors;
        
        if (computer == player) {
            textField.setText("DRAW");
            
            if (loaded) {
                soundPool.play(soundDraw, volume, volume, 1, 0, 1f);
            }
        } else if ((computer == rock && player == paper) ||
                (computer == paper && player == scissors) ||
                (computer == scissors && player == rock)) {
            textField.setText("YOU WON");
            
            if (loaded) {
                soundPool.play(soundWin, volume, volume, 1, 0, 1f);
            }
        } else {
            textField.setText("YOU LOST");
            
            if (loaded) {
                soundPool.play(soundLose, volume, volume, 1, 0, 1f);
            }
        }
    }
    
    private void buildImages(int playerChoice) {
        int computerChoice = getImg(randomNumber(0, 2));
        
        ImageView computerImage = findViewById(R.id.computer);
        ImageView playerImage = findViewById(R.id.player);
        
        checkForWin(computerChoice, playerChoice);
    
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
        DialogFragment dialog = new SelectDialogFragment();
        dialog.show(getSupportFragmentManager(), "SelectDialogFragment");
    }
    
    @Override
    public void onDialogClick(DialogFragment dialog, int id) {
        Log.d("GameActivity", "" + id);
        buildImages(getImg(id));
    }
}
