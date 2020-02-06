package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    Button play;
    MediaPlayer media;
    int currentPosition;
    SeekBar songSeek;
    SeekBar volumeBar;
    TextView elapsed;
    TextView remaining;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.play);

        elapsed = (TextView) findViewById(R.id.elapsed);
        remaining = (TextView) findViewById(R.id.remaining);

        media = MediaPlayer.create(this,R.raw.nazron);
        media.setLooping(true);
        media.seekTo(0);
        media.setVolume(0.5f,0.5f);
        totalTime = media.getDuration();

        songSeek = (SeekBar) findViewById(R.id.SeekBar);
        songSeek.setMax(totalTime);
        songSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    media.seekTo(progress);
                    songSeek.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
         volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 float volume = progress / 100f;
                 media.setVolume(volume,volume);
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

             }
         });

         new Thread(new Runnable() {
             @Override
             public void run() {

                 while (media != null)
                 {
                     try {
                     Message m = new Message();
                     m.what = media.getCurrentPosition();
                     handler.sendMessage(m);

                         Thread.sleep(1000);
                     } catch (InterruptedException e) {

                     }

                 }
             }
         }).start();
    }

    private Handler handler = new Handler()
    {

        @Override
        public void  handleMessage(Message m)
        {
             int position = m.what;
             songSeek.setProgress(position);

             String elapsedTime = createTimeLabel(position);
             elapsed.setText(elapsedTime);

             String remainingTime = createTimeLabel(totalTime - position);
             remaining.setText("-"+remainingTime);
        }
    };

    public String createTimeLabel(int time)
    {
        String timeLabel = "";
        int min = time / 1000 / 60 ;
        int sec = time / 1000 % 60 ;

        timeLabel = min + ":";
        if(sec < 10)
            timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public  void playBtnClick(View view)
    {
        if(!media.isPlaying())
        {
            media.start();
            play.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);

        }
        else {
            media.pause();
            play.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
        }

    }


}
