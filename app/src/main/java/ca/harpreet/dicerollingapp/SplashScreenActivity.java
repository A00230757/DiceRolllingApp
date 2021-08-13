package ca.harpreet.dicerollingapp;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
public class SplashScreenActivity extends AppCompatActivity {
    MediaPlayer mp;// media player used to play back sound in splash screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// to make splash screen full size
        getSupportActionBar().hide();// to hide action bar at top of activity
        setContentView(R.layout.activity_splash_screen);
        mp = MediaPlayer.create(this, R.raw.b);// memory given to media player
        mp.setLooping(true);// to play sound continuously set it true
        mp.start(); // start sound
        new Thread(new Runnable() {// thread is used to wait this activity till 8 seconds and then main activity will start
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);// 8 seconds sleep
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(),DiceRollingActivity.class));// main activity/dice rolling activity started
                mp.stop();// sound stopped
            }
        }).start();// to start thread
    }
}